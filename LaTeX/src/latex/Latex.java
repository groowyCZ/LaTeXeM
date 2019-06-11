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
    
    static final String STRING_LIST_SEPARATOR = ", ";
    static final char SEPARATOR = System.getProperty("user.dir").contains("/") ? '/' : '\\';
    public static String EQUATIONS_PATH = System.getProperty("user.dir") + Latex.SEPARATOR + "equations.xml";
    
    
    public static TeXIcon textToTeXIcon(String math, int size) {
        TeXFormula formula = new TeXFormula(math);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        icon.paintIcon(new JLabel(), img.getGraphics(), 0, 0);
        return icon;
    }
    
    public static TeXIcon textToTeXIcon(String math){
        return textToTeXIcon(math, 40);
    }
    public static void writeEquations(ArrayList<String> classes, ArrayList<String> categories, ArrayList<Equation> equations) throws FileNotFoundException, XMLStreamException, IOException {
        writeEquations(classes, categories, equations, Latex.EQUATIONS_PATH);
    }

    public static void writeEquations(ArrayList<String> classes, ArrayList<String> categories, ArrayList<Equation> equations, String path) throws FileNotFoundException, XMLStreamException, IOException {
        
        // OPEN
        Files.write(Paths.get(path), Arrays.asList(""), Charset.forName("UTF-8"));

        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(path), "UTF-8");
        writer.writeStartElement("equations");
        
        
        // CLASSES
        writer.writeStartElement("classes");
        for (String cl : classes) {
            writer.writeCharacters(cl + (classes.indexOf(cl) == classes.size() - 1 ? "" : ", "));
        }
        writer.writeEndElement();
        
        
        // CATEGORIES
        writer.writeStartElement("categories");
        for (String cl : categories) {
            writer.writeCharacters(cl + (categories.indexOf(cl) == categories.size() - 1 ? "" : ", "));
        }
        writer.writeEndElement();
        
        
        // EQUATIONS
        for (Equation tmp : equations) {
            HashMap<String, String> equation = tmp.asHashMap(STRING_LIST_SEPARATOR);
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
        writer.close();
    }

    public static ArrayList<Equation> loadEquations() throws XMLStreamException, FileNotFoundException {
        return loadEquations(Latex.EQUATIONS_PATH);
    }
    public static ArrayList<Equation> loadEquations(String path) throws XMLStreamException, FileNotFoundException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(path), "UTF-8");
        
        // r/blackmagicfuckery/
        String element;
        String characters = "";
        ArrayList<Equation> equations = new ArrayList();
        while (reader.hasNext()) {
            int xmlEvent = reader.next();
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                element = reader.getName().getLocalPart();

                if (element.equals("equation")) {
                    HashMap<String, String> equationHashMap = new HashMap();
                    
                    //adds keys to equation
                    OUTER:
                    while (true) {
                        xmlEvent = reader.next();
                        switch (xmlEvent) {
                            case XMLStreamConstants.END_ELEMENT:
                                if (!characters.equals("")) {
                                    equationHashMap.put(element, characters);
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
                    
                    equations.add(new Equation(equationHashMap, STRING_LIST_SEPARATOR));
                }
            }
        }
        return equations;
    }

    public static ArrayList<String> loadClasses() throws XMLStreamException, FileNotFoundException {
        return loadClasses(Latex.EQUATIONS_PATH);
    }
    public static ArrayList<String> loadClasses(String path) throws XMLStreamException, FileNotFoundException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(path), "UTF-8");

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
        return loadCategories(Latex.EQUATIONS_PATH);
    }
    public static ArrayList<String> loadCategories(String path) throws XMLStreamException, FileNotFoundException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(path), "UTF-8");

        String element = "";
        ArrayList<String> categories = new ArrayList();
        while (reader.hasNext()) {
            int xmlEvent = reader.next();
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                element = reader.getName().getLocalPart();
            }
            if (xmlEvent == XMLStreamConstants.CHARACTERS && element.equals("categories")) {
                categories.addAll(Arrays.asList(reader.getText().split(", ")));
                break;
            }
        }
        return categories;
    }
}
