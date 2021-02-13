package com.intelligents.haunting;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

class XMLParser implements java.io.Serializable {


    static ArrayList<Ghost> populateGhosts(Document document, String element) {
        NodeList nList = document.getElementsByTagName(element);
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
                String backstory = ghost.getElementsByTagName("backstory").item(0).getTextContent();
                // Construct new ghost and add to ghost list
                ghosts.add(new Ghost(name, type, background, evidence, backstory));
                // Print
                // System.out.println(name);
            }
        }
        return ghosts;
    }

    static ArrayList<MiniGhost> populateMiniGhosts(Document document, String element) {
        NodeList nList = document.getElementsByTagName(element);
        //Instantiate new MiniGhost list
        ArrayList<MiniGhost> miniGhosts = new ArrayList<>();
        // With node list find each element and construct minighost object
        for (int i = 0; i < nList.getLength(); i++) {
            // Iterate through each node in nodeList
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                // Generate local variables from each "minighost" element in XML
                Element minighost = (Element) nNode;
                String name = minighost.getElementsByTagName("name").item(0).getTextContent();
                String type = minighost.getElementsByTagName("type").item(0).getTextContent();
                // Construct new minighost and add to minighost list
                miniGhosts.add(new MiniGhost(name, type));
            }
        }
        return miniGhosts;
    }

    static ArrayList<Room> populateRooms(Document document, String element) {
        NodeList nList = document.getElementsByTagName(element);
        //Instantiate new Room list
        ArrayList<Room> rooms = new ArrayList<>();
        // With node list find each element and construct room object
        for (int i = 0; i < nList.getLength(); i++) {
            // Iterate through each node in nodeList
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                // Generate local variables from each "room" element in XML
                Element roomElement = (Element) nNode;
                String roomTitle = roomElement.getElementsByTagName("title").item(0).getTextContent();
                String roomDescription = roomElement.getElementsByTagName("description").item(0).getTextContent();
                ArrayList<String> evidence = new ArrayList<>();
                // Construct new room and add to room list
                Room room = new Room(roomTitle, roomDescription);
                //for loops to read multiple exits. Return list of exits
                for (int j = 0; j < roomElement.getElementsByTagName("exit").getLength(); j++) {
                    //cast the item read back as Element from node
                    Element el = (Element) roomElement.getElementsByTagName("exit").item(j);
                    //get direction and name from element
                    String direction = el.getElementsByTagName("direction").item(0).getTextContent();
                    String name = el.getElementsByTagName("directionName").item(0).getTextContent();
                    //pointing to hashmap and mapping direction to room name
                    room.directionList.put(direction, name);

                }
                // will populate the rooms
                rooms.add(room);
            }
        }
        return rooms;
    }

    // XML reader, returns the document based on the passed in String which is the filename before the extension
    static Document readXML(String filename, ClassLoader classLoader) {
        Document doc = null;
        try {
            // class.xml is place in the folder data within the package structure
            if (filename != null) {
//            File inputFile = new File("resources/" + filename + ".xml");
                InputStream xmlToParse = classLoader.getResourceAsStream(filename + ".xml");
                // three statements that result in loading the xml file and creating a Document object

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(xmlToParse);

                // get the root node of the XML document

                Element root = doc.getDocumentElement();

                // normalize standarizes the XML format
                root.normalize();

                // print out the root node of the XML document
                //System.out.println("Root is " + root.getNodeName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }


//    static NodeList readRooms() {
//        NodeList results = null;
//        try {
//            // class.xml is place in the folder data within the package structure
//
//            File inputFile = new File("resources/Rooms.xml");
//
//            // three statements that result in loading the xml file and creating a Document object
//
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document doc = db.parse(inputFile);
//
//            // get the root node of the XML document
//
//            Element root = doc.getDocumentElement();
//
//            // normalize standarizes the XML format
//            root.normalize();
//
//            // print out the root node of the XML document
//            //System.out.println("Root is " + root.getNodeName());
//
//            // Get all "room" elements by tag name
//            results = doc.getElementsByTagName("room");
//
//            // Call populateRooms to create ghosts for game
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return results;
//    }

    //    static NodeList readGhosts() {
//        NodeList results = null;
//        try {
//            // class.xml is place in the folder data within the package structure
//
//            File inputFile = new File("resources/Ghosts.xml");
//
//            // three statements that result in loading the xml file and creating a Document object
//
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document doc = db.parse(inputFile);
//
//            // get the root node of the XML document
//
//            Element root = doc.getDocumentElement();
//
//            // normalize standarizes the XML format
//            root.normalize();
//
//            // print out the root node of the XML document
//            // System.out.println("Root is " + root.getNodeName());
//
//            // Get all "ghost" elements by tag name
//            results = doc.getElementsByTagName("ghost");
//
//            // Call populateGhosts to create ghosts for game
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return results;
//    }


}
