package com.mygdx.sim.GameObjects;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

public class MapNetworkReader {

	private static final int MAPMULTIPLIER = 10;

	public void readNetwork() {
		
		// Structures of Map
		HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();
		HashMap<String, Node> nodeMap = new HashMap<String, Node>();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(chooseFile());

			// Read Edges
			readEdges();
			
			NodeList edgeElements = document.getElementsByTagName("edge");
			for (int i = 0; i < edgeElements.getLength(); i++) {
				org.w3c.dom.Node edgeElement = edgeElements.item(i);

				// Read Lanes
				NodeList edgeChildren = edgeElement.getChildNodes();
				for (int j = 0; j < edgeChildren.getLength(); j++) {
					org.w3c.dom.Node laneElement = edgeChildren.item(j);
					Element lane = (Element) laneElement;
					System.out.println(lane.getTagName());
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
