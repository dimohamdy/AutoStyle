
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class GeneratorStyle {

    static String basename = "";

    GeneratorStyle(String openFile, String saveFile) {
        // Assume filename argument
        basename = FilenameUtils.getBaseName(saveFile).toUpperCase();

        String styleCode = "";
        try {
            // Build the document with SAX and Xerces, no validation
            SAXBuilder builder = new SAXBuilder();
            // Create the document
//			org.jdom2.Document document = builder
//					.build(new File("ViewTest.xml"));
            org.jdom2.Document document = builder.build(new File(openFile));

           // System.out.print(openFile);
            Element root = document.getRootElement();
            ElementFilter filter = new org.jdom2.filter.ElementFilter(
                    "subviews");
            XMLOutputter fmt = new XMLOutputter();
            List<Element> uiElements = null;
            for (Element c : root.getDescendants(filter)) {
                // System.out.println(c.getTextNormalize());
                // fmt.output(c, System.out);
                uiElements = c.getChildren();
                //System.out.print(uiElements.toString()) ;
                 // System.out.print(uiElements.size()+"") ;
                for (int count = 0; count < uiElements.size(); count++) {
                 //System.out.print(uiElements.get(count).toString()) ;
                    
//                System.out.println(uiElements.get(count).getName());
//                System.out.println(uiElements.get(count).getAttributeValue("userLabel").toUpperCase());
//                System.out.println(uiElements.get( count));
//                                    System.out.println("++++++++++++++++++");
                    
                    if(uiElements.get( count).getAttribute("userLabel")!=null){
                    String result = setProperties(
                            uiElements.get(count).getName(), uiElements.get(
                            count).getAttributeValue("userLabel").toUpperCase(), uiElements.get(
                            count));

                    if (!result.equals("not")) {
                        //System.out.print(styleCode);
                        styleCode += result + "\n ==============================================\n";
                    

                    }

                    }
                }

            }


            System.out.print(styleCode);
            FileUtils.writeStringToFile(new File(saveFile), styleCode, true);
        //   out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static String setProperties(String typeUI, String name, Element element) {
     
        
        if (typeUI.equalsIgnoreCase("label")) {

           
            String result1 = "#define " + basename + "_" + name + "_LABEL_FONT_NAME @\""+element.getChild("fontDescription").getAttributeValue("name")+"\"";
            String result2 = "#define " + basename + "_" + name + "_LABEL_FONT_SIZE "+element.getChild("fontDescription").getAttributeValue("pointSize");
            String result3 = "#define " + basename + "_" + name + "_LABEL_COLOR 0x000000";
            String result4 = "#define " + basename + "_" + name + "_LABEL_BACKGROUND_COLOR 0XFFFFFF";
            String result5="";

                    
                    
            if(element.getAttribute("numberOfLines")!=null){
             result5 = "#define " + basename + "_" + name + "_LABEL_NO_OF_LINES "+element.getAttributeValue("numberOfLines");}
            else{
             result5 = "#define " + basename + "_" + name + "_LABEL_NO_OF_LINES 1";   
            }
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("textField")) {

//            String result1 = "#define " + basename + "_" + name + "_TEXT_FIELD_NAME @\"Arial\"";
//            String result2 = "#define " + basename + "_" + name + "_TEXT_FIELD_SIZE 14";
            
            String result1 = "#define " + basename + "_" + name + "_TEXT_FIELD_NAME @\""+element.getChild("fontDescription").getAttributeValue("name")+"\"";
            String result2 = "#define " + basename + "_" + name + "_TEXT_FIELD_SIZE "+element.getChild("fontDescription").getAttributeValue("pointSize");
            
            
            
            String result3 = "#define " + basename + "_" + name + "_TEXT_FIELD_COLOR 0x000000";
//            String result4 = "#define " + basename + "_" + name + "_TEXT_FIELD_HEIGHT 30";
//            String result5 = "#define " + basename + "_" + name + "_TEXT_FIELD_WIDTH 300";

            String result4 = "#define " + basename + "_" + name + "_TEXT_FIELD_HEIGHT "+element.getChild("rect").getAttributeValue("height");
            String result5 = "#define " + basename + "_" + name + "_TEXT_FIELD_WIDTH "+element.getChild("rect").getAttributeValue("width");
            
            
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("button")) {

//            String result1 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_NAME @\"Arial\"";
//            String result2 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_SIZE 14";
            
            String result1 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_NAME @\""+element.getChild("fontDescription").getAttributeValue("name")+"\"";
            String result2 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_SIZE "+element.getChild("fontDescription").getAttributeValue("pointSize");
            
            String result3 = "#define " + basename + "_" + name + "_BUTTON_TITLE_NORMAL_TEXT_COLOR 0x157DEC";
            String result4 = "#define " + basename + "_" + name + "_BUTTON_TITLE_DISABLED_TEXT_COLOR 0x157DEC";
            String result5 = "#define " + basename + "_" + name + "_BUTTON_TITLE_SELECTED_TEXT_COLOR 0x157DEC";
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("textview")) {
//            String result1 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_NAME @\"Arial\"";
//            String result2 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_SIZE 14";
             String result1 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_NAME @\""+element.getChild("fontDescription").getAttributeValue("name")+"\"";
            String result2 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_SIZE "+element.getChild("fontDescription").getAttributeValue("pointSize");
            
            String result3 = "#define " + basename + "_" + name + "_TEXTVIEW_COLOR 0x333333";
            
            
            
//            String result4 = "#define " + basename + "_" + name + "_TEXTVIEW_HIEGHT 30";
//            String result5 = "#define " + basename + "_" + name + "_TEXTVIEW_WIDTH 250";
            
            String result4 = "#define " + basename + "_" + name + "_TEXTVIEW_HIEGHT "+element.getChild("rect").getAttributeValue("height");
            String result5 = "#define " + basename + "_" + name + "_TEXTVIEW_WIDTH "+element.getChild("rect").getAttributeValue("width");
            
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        }
        return "not";

    }
}
