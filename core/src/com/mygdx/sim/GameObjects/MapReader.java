package com.mygdx.sim.GameObjects;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

public class MapReader {
	
	private static final int MAPMULTIPLIER = 10;

	public HashMap<String, Node> readNodes() {
		
		HashMap<String, Node> nodeMap = new HashMap<String, Node>();
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(chooseFile());
			
			
			NodeList xmlElements = document.getElementsByTagName("node");
			for(int i = 0; i < xmlElements.getLength(); i++) {
				org.w3c.dom.Node xmlElement = xmlElements.item(i);
				
				String nodeID = xmlElement.getAttributes().getNamedItem("id").getTextContent();
				double nodeX = Double.parseDouble(xmlElement.getAttributes().getNamedItem("x").getTextContent()) * MAPMULTIPLIER;
				double nodeY = Double.parseDouble(xmlElement.getAttributes().getNamedItem("y").getTextContent()) * MAPMULTIPLIER;
				String nodeType = xmlElement.getAttributes().getNamedItem("type").getTextContent();
				
				 Node node = new Node(nodeX, nodeY, nodeType);
				 nodeMap.put(nodeID, node);
			}
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return nodeMap;
	}
	
	public HashMap<String, Edge> readEdges(HashMap<String, Node> nodeMap) {
		
		HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(chooseFile());
			
			
			NodeList xmlElements = document.getElementsByTagName("edge");
			for(int i = 0; i < xmlElements.getLength(); i++) {
				org.w3c.dom.Node xmlElement = xmlElements.item(i);
				
				String edgeID = xmlElement.getAttributes().getNamedItem("id").getTextContent();
				String edgeFrom = xmlElement.getAttributes().getNamedItem("from").getTextContent();
				String edgeTo = xmlElement.getAttributes().getNamedItem("to").getTextContent();
				int edgeLanes = Integer.parseInt(xmlElement.getAttributes().getNamedItem("numLanes").getTextContent());
				double edgeSpeed = Double.parseDouble(xmlElement.getAttributes().getNamedItem("speed").getTextContent());
				
				Node fromNode = nodeMap.get(edgeFrom);
				Node toNode = nodeMap.get(edgeTo);
				
				Edge edge = new Edge(fromNode, toNode, (int)edgeSpeed, edgeLanes);
                fromNode.addOutEdge(edge);
                toNode.addInEdge(edge);
                // Increment node's Edge counter here
                fromNode.setNodePriorityWeight(fromNode.getNodePriorityWeight() + 2);
                toNode.setNodePriorityWeight(toNode.getNodePriorityWeight() + 2);
                // edgeMap.put(id + "#2", new Edge(nodeMap.get(to), nodeMap.get(from), (int) speed, laneNum));
                
                edgeMap.put(edgeID, edge);
			}
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return edgeMap;
	}
		
	public String chooseFile() {
		JFileChooser chooser = new JFileChooser(Gdx.files.getLocalStoragePath());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Node XML file", "xml");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return null;
		}
	}
	
	public void printAll(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {
		int counter = 0;
		for (Node n : nodeMap.values()) {
			System.out.println("Node " + counter + " is at X: " + n.getX() + " Y: " + n.getY());
			counter++;
		}
		
		counter = 0;
		for (Edge edge : edgeMap.values()) {
			System.out.println("Edge " + counter + " is from: " + edge.getFrom() + " to: " + edge.getTo());
			counter++;
		}
	}
}
