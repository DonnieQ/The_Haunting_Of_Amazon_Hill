import java.io.File;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

 class XMLParser {


     public void readGhosts() {

         try {
             // class.xml is place in the folder data within the package structure

             File inputFile = new File("The_Haunting_Of_Amazon_Hill/resources/Ghosts.xml");

             // three statements that result in loading the xml file and creating a Document object

             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document doc = db.parse(inputFile);

             // get the root node of the XML document

             Element root = doc.getDocumentElement();

             // normalize standarizes the XML format
             root.normalize();

             // print out the root node of the XML document
             System.out.println("Root is " + root.getNodeName());

             // read the ghost attribute
             NodeList nList = doc.getElementsByTagName("ghost");

             for (int i = 0; i < nList.getLength(); i++) {
                 //
                 Node nNode = nList.item(i);
                 System.out.println("\nCurrent Element :" + nNode.getNodeName());

                 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                     Element ghost = (Element) nNode;
                     System.out.println("Ghost Name : "
                             + ghost.getElementsByTagName("name").item(0).getTextContent());
                     System.out.println("Ghost Type : "
                             + ghost.getElementsByTagName("type").item(0).getTextContent());
                     System.out.println("Background : "
                             + ghost.getElementsByTagName("background").item(0).getTextContent());
                     System.out.println("Evidence : "
                             + ghost
                             .getElementsByTagName("evidence")
                             .item(0)
                             .getTextContent());
                     System.out.println("Evidence : "
                             + ghost
                             .getElementsByTagName("evidence")
                             .item(1)
                             .getTextContent());
                 }
             }

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}
