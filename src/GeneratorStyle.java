
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;


public class GeneratorStyle {

    static String basename = "";
    List<Connection> connections;
    //GeneratorConnection Function 
    /*
     * 
     * openFile the path of the Nib File That will Parse and Generate Connections From it
     * 
     * 
     * 
     */
    void GeneratorConnection(String openFile) {
        // Assume filename argument



        try {
            // Build the document with SAX and Xerces, no validation
            SAXBuilder builder = new SAXBuilder();
            // Create the document
            //open the nib file to get all the connections of Outlets
            org.jdom2.Document document = builder.build(new File(openFile));

            // get the root of the nib file "Root Tag of XML"
            Element root = document.getRootElement();
            //Make Filter that use to search for Tag his name is "Connections"
            ElementFilter filter = new org.jdom2.filter.ElementFilter(
                    "connections");

            //uiElement that use save the Childern UIElement result that match  Filtering
            List<Element> uiElements = null;
            //
            connections = new ArrayList<Connection>();
            for (Element c : root.getDescendants(filter)) {
                //get all Childern of Tag "Connection" 
                uiElements = c.getChildren();
                //Check the "Connections" Tag have "Outlet" Tag Childern OR Other Childern 
                if (uiElements.size() > 0) {
                    //Loop For All Childern of "Connections" Tag
                    for (int count = 0; count < uiElements.size(); count++) {
                        //Check the Child Name is Outlet
                        if (uiElements.get(count).getName().equals("outlet")) {
                            //use the Outlet values Property and destenation
                            String property = uiElements.get(count).getAttributeValue("property");
                            String destination = uiElements.get(count).getAttributeValue("destination");
                            //Generate Connection Type object and it to connections ArrayList
                            connections.add(new Connection(property, destination));
                        }
                    }
                //System.out.println("Size"+connections.size());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //Constructor of the Class
    /*
     * 
     * openFile the path of the Nib File That will Parse
     * 
     * saveFile the path of the file That will use to Save the Generated Style
     * 
     */
    GeneratorStyle(String openFile, String saveFile) {
        // Assume filename argument
        basename = FilenameUtils.getBaseName(openFile).toUpperCase();
        //Generate the Connections that will use to Generate Style
        GeneratorConnection(openFile);
        String styleCode = "";
        try {
            // Build the document with SAX and Xerces, no validation
            SAXBuilder builder = new SAXBuilder();
            //open the nib file to get all the connections of Outlets
            org.jdom2.Document document = builder.build(new File(openFile));
            // get the root of the nib file "Root Tag of XML"
            Element root = document.getRootElement();
            //Make Filter that use to search for Tag his name is "subviews"
            ElementFilter filter = new org.jdom2.filter.ElementFilter(
                    "subviews");
            List<Element> uiElements = null;
            //Loop in All Tags That mathch The Filter 
            for (Element c : root.getDescendants(filter)) {
            //get the Childern of the echc Subviews Tag "The controls That make Style for it UIButton UILable UiTextFeild"
                uiElements = c.getChildren();
                for (int count = 0; count < uiElements.size(); count++) {
             //get the id of the Control and the Check if it Match the Destenation Variable in Connection Object 
             //then using the Match Connection object and use Property Variable of it.
                    String id = uiElements.get(count).getAttributeValue("id");
                    String name = uiElements.get(count).getName();
                    for (Connection connection : connections) {
            //Check the Connection Matching of Control
                        if (connection.destination.equals(id)) {
                            String property = connection.propertyName;
                            //System.out.print(name.toUpperCase() + "   " + property + "   \n");
                            //Send the Match Control to setProperties Function That Generate the Style and Return String have Style
                            String result = setProperties(
                                    name.toUpperCase(),
                                    property.toUpperCase(), uiElements.get(
                                    count));
                            //Because We not Support All UI We Return "not" when the Control is not Supported
                            if (!result.equals("not")) {
                                //System.out.print(styleCode);
                                styleCode += result + "\n ==============================================\n";

                            }
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

        System.out.print("IN" + typeUI + "   " + name + "   \n");

        if (typeUI.equalsIgnoreCase("label")) {


            String result1 = "#define " + basename + "_" + name + "_LABEL_FONT_NAME @\"" + element.getChild("fontDescription").getAttributeValue("name") + "\"";
            String result2 = "#define " + basename + "_" + name + "_LABEL_FONT_SIZE " + element.getChild("fontDescription").getAttributeValue("pointSize");
            String result3 = "#define " + basename + "_" + name + "_LABEL_COLOR 0x000000";
            String result4 = "#define " + basename + "_" + name + "_LABEL_BACKGROUND_COLOR 0XFFFFFF";
            String result5 = "";

            if (element.getAttribute("numberOfLines") != null) {
                result5 = "#define " + basename + "_" + name + "_LABEL_NO_OF_LINES " + element.getAttributeValue("numberOfLines");
            } else {
                result5 = "#define " + basename + "_" + name + "_LABEL_NO_OF_LINES 1";
            }
            
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("textField")) {


            String result1 = "#define " + basename + "_" + name + "_TEXT_FIELD_NAME @\"" + element.getChild("fontDescription").getAttributeValue("name") + "\"";
            String result2 = "#define " + basename + "_" + name + "_TEXT_FIELD_SIZE " + element.getChild("fontDescription").getAttributeValue("pointSize");
            String result3 = "#define " + basename + "_" + name + "_TEXT_FIELD_COLOR 0x000000";
            String result4 = "#define " + basename + "_" + name + "_TEXT_FIELD_HEIGHT " + element.getChild("rect").getAttributeValue("height");
            String result5 = "#define " + basename + "_" + name + "_TEXT_FIELD_WIDTH " + element.getChild("rect").getAttributeValue("width");

            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("button")) {

            String result1 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_NAME @\"" + element.getChild("fontDescription").getAttributeValue("name") + "\"";
            String result2 = "#define " + basename + "_" + name + "_BUTTON_TITLE_FONT_SIZE " + element.getChild("fontDescription").getAttributeValue("pointSize");
            String result3 = "#define " + basename + "_" + name + "_BUTTON_TITLE_NORMAL_TEXT_COLOR 0x157DEC";
            String result4 = "#define " + basename + "_" + name + "_BUTTON_TITLE_DISABLED_TEXT_COLOR 0x157DEC";
            String result5 = "#define " + basename + "_" + name + "_BUTTON_TITLE_SELECTED_TEXT_COLOR 0x157DEC";
            
            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        } else if (typeUI.equalsIgnoreCase("textview")) {

            String result1 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_NAME @\"" + element.getChild("fontDescription").getAttributeValue("name") + "\"";
            String result2 = "#define " + basename + "_" + name + "_TEXTVIEW_FONT_SIZE " + element.getChild("fontDescription").getAttributeValue("pointSize");
            String result3 = "#define " + basename + "_" + name + "_TEXTVIEW_COLOR 0x333333";
            String result4 = "#define " + basename + "_" + name + "_TEXTVIEW_HIEGHT " + element.getChild("rect").getAttributeValue("height");
            String result5 = "#define " + basename + "_" + name + "_TEXTVIEW_WIDTH " + element.getChild("rect").getAttributeValue("width");

            return result1 + ";\n" + result2 + ";\n" + result3 + ";\n" + result4 + ";\n" + result5;

        }
        return "not";

    }
}
