package com.mygdx.sim.GameObjects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

public class MapReader {

	private static final int MAPMULTIPLIER = 10;

	private HashMap<String, Node> nodeMap = null;
	private HashMap<String, Edge> edgeMap = null;
	private List<Node> spawnPoints = new ArrayList<Node>();

	public HashMap<String, Node> getNodes() {
		return nodeMap;
	}

	public HashMap<String, Edge> getEdges() {
		return edgeMap;
	}

	public void readMap() {

		JFileChooser chooser = new JFileChooser(Gdx.files.getLocalStoragePath());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		String nodesPath = null;
		String edgesPath = null;
		String connectionsPath = null;

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File dir = new File(chooser.getSelectedFile().getPath());
			for (File file : dir.listFiles()) {
				if (file.getName().endsWith(("nod.xml"))) {
					nodesPath = file.getAbsolutePath();
				} else if (file.getName().endsWith(("edg.xml"))) {
					edgesPath = file.getAbsolutePath();
				} else if (file.getName().endsWith(("con.xml"))) {
					connectionsPath = file.getAbsolutePath();
				}
			}
		}

		nodeMap = readNodes(nodesPath);
		edgeMap = readEdges(nodeMap, edgesPath);
		readConnections(edgeMap, connectionsPath);
	}

	public HashMap<String, Node> readNodes(String path) {

		HashMap<String, Node> nodeMap = new HashMap<String, Node>();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(path);

			NodeList xmlElements = document.getElementsByTagName("node");
			for (int i = 0; i < xmlElements.getLength(); i++) {
				org.w3c.dom.Node xmlElement = xmlElements.item(i);

				String nodeID = xmlElement.getAttributes().getNamedItem("id").getTextContent();
				float nodeX = Float.parseFloat(xmlElement.getAttributes().getNamedItem("x").getTextContent()) * MAPMULTIPLIER;
				float nodeY = Float.parseFloat(xmlElement.getAttributes().getNamedItem("y").getTextContent()) * MAPMULTIPLIER;
				String nodeType = xmlElement.getAttributes().getNamedItem("type").getTextContent();

				Node node = new Node(nodeX, nodeY, nodeType, nodeID);
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

	public HashMap<String, Edge> readEdges(HashMap<String, Node> nodeMap, String path) {

		HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(path);

			NodeList xmlElements = document.getElementsByTagName("edge");
			for (int i = 0; i < xmlElements.getLength(); i++) {
				org.w3c.dom.Node xmlElement = xmlElements.item(i);

				String edgeID = xmlElement.getAttributes().getNamedItem("id").getTextContent();
				String edgeFrom = xmlElement.getAttributes().getNamedItem("from").getTextContent();
				String edgeTo = xmlElement.getAttributes().getNamedItem("to").getTextContent();
				int edgeLanes = Integer.parseInt(xmlElement.getAttributes().getNamedItem("numLanes").getTextContent());
				double edgeSpeed = Double.parseDouble(xmlElement.getAttributes().getNamedItem("speed").getTextContent());

				List<Coordinates> shapeCoordinates = null;
				if (xmlElement.getAttributes().getNamedItem("shape") != null) {
					String shape = xmlElement.getAttributes().getNamedItem("shape").getTextContent();

					String shapeArray[] = shape.split(" ");
					if (shapeArray.length > 0) {
						shapeCoordinates = new ArrayList<Coordinates>();
						for (String shapeString : shapeArray) {
							String coordinates[] = shapeString.split(",");
							shapeCoordinates.add(new Coordinates(Float.valueOf(coordinates[0]) * MAPMULTIPLIER, Float.valueOf(coordinates[1]) * MAPMULTIPLIER));
						}
					}
				}

				/*
				 * Node fromNode = nodeMap.get(edgeFrom); Node toNode = nodeMap.get(edgeTo);
				 * 
				 * Edge edge = new Edge(fromNode, toNode, (int) edgeSpeed, edgeLanes, edgeLanes,
				 * shapeCoordinates); fromNode.addOutEdge(edge); toNode.addInEdge(edge);
				 * 
				 * edgeMap.put(edgeID, edge);
				 */

				for (int lane = 0; lane < edgeLanes; lane++) {

					Node fromNode = nodeMap.get(edgeFrom);
					Node toNode = nodeMap.get(edgeTo);

					Edge edge = new Edge(fromNode, toNode, (int) edgeSpeed, lane, edgeLanes, shapeCoordinates);
					fromNode.addOutEdge(edge);
					toNode.addInEdge(edge);

					edgeID = (edgeID + "_" + lane);
					edgeMap.put(edgeID, edge);
				}
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

	public void readConnections(HashMap<String, Edge> edges, String path) {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(path);

			NodeList xmlElements = document.getElementsByTagName("connection");
			for (int i = 0; i < xmlElements.getLength(); i++) {
				org.w3c.dom.Node xmlElement = xmlElements.item(i);

				String connectionFromID = xmlElement.getAttributes().getNamedItem("from").getTextContent();
				String connectionToID = xmlElement.getAttributes().getNamedItem("to").getTextContent();
				String connectionFromLane = xmlElement.getAttributes().getNamedItem("fromLane").getTextContent();
				String connectionToLane = xmlElement.getAttributes().getNamedItem("toLane").getTextContent();

				Edge fromEdge = null;
				Edge toEdge = null;
				if ((fromEdge = edges.get(connectionFromID + "_" + connectionFromLane)) != null && (toEdge = edges.get(connectionToID + "_" + connectionToLane)) != null) {
					fromEdge.addToEdge(toEdge);
				}
			}

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addSpawn(Node node){
		spawnPoints.add(node);
	}

	public List<Node> getSpawnPoints(){
		return spawnPoints;
	}

	public void printAll(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {
		int counter = 0;
		for (Node n : nodeMap.values()) {
			System.out.println("Node " + n.getId() + " is at X: " + n.getX() + " Y: " + n.getY());
			counter++;
		}

		counter = 0;
		for (Edge edge : edgeMap.values()) {
			System.out.println("Edge " + edge.getId() + " is from: " + edge.getFrom() + " to: " + edge.getTo() + " to edges: " + edge.getToEdges().size());
			counter++;
		}
	}
}
