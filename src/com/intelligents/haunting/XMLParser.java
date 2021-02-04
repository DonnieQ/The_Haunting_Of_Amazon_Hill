package com.intelligents.haunting;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

class XMLParser implements java.io.Serializable {

    public static NodeList readGhosts() {
        NodeList results = null;
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
            // System.out.println("Root is " + root.getNodeName());

            // Get all "ghost" elements by tag name
            results = doc.getElementsByTagName("ghost");

            // Call populateGhosts to create ghosts for game
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<Ghost> populateGhosts(NodeList nList) {
        //Instantiate new Ghost list
        ArrayList<Ghost> ghosts = new ArrayList<>();
        // With node list find each element and construct ghost object
        for (int i = 0; i < nList.getLength(); i++) {
            // Iterate through each node in nodeList
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                // Generate local variables from each "ghost" element in XML
                Element ghost = (Element) nNode;
                String name = ghost.getElementsByTagName("name").item(0).getTextContent();
                String type = ghost.getElementsByTagName("type").item(0).getTextContent();
                String background = ghost.getElementsByTagName("background").item(0).getTextContent();
                ArrayList<String> evidence = new ArrayList<>();
                String evidence1 = ghost.getElementsByTagName("evidence").item(0).getTextContent();
                String evidence2 = ghost.getElementsByTagName("evidence").item(1).getTextContent();
                evidence.add(evidence1);
                evidence.add(evidence2);
                // Construct new ghost and add to ghost list
                ghosts.add(new Ghost(name, type, background, evidence));
                // Print
                // System.out.println(name);
            }
        }
        return ghosts;
    }
}
