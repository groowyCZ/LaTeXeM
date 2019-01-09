package latex;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 *
 * @author Groowy
 */
public class Latex {

    public static TeXIcon textToTeXIcon(String math, int size) {
        TeXFormula fomule = new TeXFormula(math);
        TeXIcon ti = fomule.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        BufferedImage b = new BufferedImage(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
        return ti;
    }
    
    public static TeXIcon textToTeXIcon(String math){
        return textToTeXIcon(math, 40);
    }

    private static String getDefaultPath() {
        String pathSeparator = System.getProperty("java.class.path").contains("/") ? "/" : "\\";
        String defaultPath = "";
        String[] tmp = System.getProperty("java.class.path").split(pathSeparator.equals("\\") ? "\\\\" : "/");
        for (int i = 0; i < tmp.length - 1; i++) {
            defaultPath += tmp[i] + pathSeparator;
        }
        defaultPath += "equations.xml";
        System.out.println(defaultPath);
        
        return defaultPath;
    }

    public static void writeEquations(ArrayList<String> classes, ArrayList<String> categories, ArrayList<HashMap<String, String>> equations) throws FileNotFoundException, XMLStreamException, IOException {
        String defaultPath = getDefaultPath();
        //this will initiate the file
        Files.write(Paths.get(defaultPath), Arrays.asList(""), Charset.forName("UTF-8"));

        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = xof.createXMLStreamWriter(new FileOutputStream(defaultPath), "UTF-8");
        writer.writeStartElement("equations");

        writer.writeStartElement("classes");
        for (String cl : classes) {
            writer.writeCharacters(cl + (classes.indexOf(cl) == classes.size() - 1 ? "" : ", "));
        }
        writer.writeEndElement();

        writer.writeStartElement("classes");
        for (String cl : categories) {
            writer.writeCharacters(cl + (classes.indexOf(cl) == classes.size() - 1 ? "" : ", "));
        }
        writer.writeEndElement();

        for (HashMap equation : equations) {
            ArrayList<String> keys = new ArrayList(equation.keySet());
            writer.writeStartElement("equation");
            for (String key : keys) {
                writer.writeStartElement(key);
                writer.writeCharacters((String) equation.get(key));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public static ArrayList<HashMap<String, String>> loadEquations() throws XMLStreamException, FileNotFoundException {
        String defaultPath = getDefaultPath();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(defaultPath), "UTF-8");

        String element;
        String characters = "";
        ArrayList<HashMap<String, String>> equations = new ArrayList();
        while (reader.hasNext()) {
            int xmlEvent = reader.next();
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                element = reader.getName().getLocalPart();

                if (element.equals("equation")) {
                    HashMap<String, String> equation = new HashMap();
                    
                    //adds keys to equation
                    OUTER:
                    while (true) {
                        xmlEvent = reader.next();
                        switch (xmlEvent) {
                            case XMLStreamConstants.END_ELEMENT:
                                if (!characters.equals("")) {
                                    System.out.println("<" + element + ">" + characters + "</" + element + ">");
                                    equation.put(element, characters);
                                    characters = "";
                                }
                                if (reader.getName().getLocalPart().equals("equation")) {
                                    break OUTER;
                                }
                                break;
                            case XMLStreamConstants.START_ELEMENT:
                                element = reader.getName().getLocalPart();
                                break;
                            case XMLStreamConstants.CHARACTERS:
                                characters += reader.getText();
                                break;
                            default:
                                break;
                        }
                    }

                    equations.add(equation);
                }
            }
        }
        return equations;
    }

    public static ArrayList<String> loadClasses() throws XMLStreamException, FileNotFoundException {
        String defaultPath = getDefaultPath();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(defaultPath), "UTF-8");

        String element = "";
        ArrayList<String> classes = new ArrayList();
        while (reader.hasNext()) {
            int xmlEvent = reader.next();
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                element = reader.getName().getLocalPart();
            }
            if (xmlEvent == XMLStreamConstants.CHARACTERS && element.equals("classes")) {
                classes.addAll(Arrays.asList(reader.getText().split(", ")));
                break;
            }
        }
        return classes;
    }

    public static ArrayList<String> loadCategories() throws XMLStreamException, FileNotFoundException {
        String defaultPath = getDefaultPath();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(defaultPath), "UTF-8");

        String element = "";
        ArrayList<String> classes = new ArrayList();
        while (reader.hasNext()) {
            int xmlEvent = reader.next();
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                element = reader.getName().getLocalPart();
            }
            if (xmlEvent == XMLStreamConstants.CHARACTERS && element.equals("categories")) {
                classes.addAll(Arrays.asList(reader.getText().split(", ")));
                break;
            }
        }
        return classes;
    }
}
