package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.Gdx;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

public class MapReader {

	public HashMap<String, Node> readNodes() {
		// Choosing the Node xml file

		HashMap<String, Node> nodeMap = new HashMap<String, Node>();

		String xmlFile = null;
		JFileChooser chooser = new JFileChooser(Gdx.files.getLocalStoragePath());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Node XML file", "xml");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			xmlFile = chooser.getSelectedFile().getAbsolutePath();
			System.out.println("Reading file : " + xmlFile);

			try {
				Scanner sc = new Scanner(new File(xmlFile));

				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					if (line.startsWith("    <node")) {
						String[] properties = line.split("\"");
						String id = properties[1];
						double x = Double.parseDouble(properties[3]) * 10;
						double y = Double.parseDouble(properties[5]) * 10;
						String type = properties[7];

                        Node n = new Node(x, y, type);

                        nodeMap.put(id, n);
                    }
                }

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return nodeMap;
	}

	public HashMap<String, Edge> readEdges(HashMap<String, Node> nodeMap) {
		HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();

		String xmlFile = null;
		JFileChooser chooser = new JFileChooser(Gdx.files.getLocalStoragePath());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Edge XML file", "xml");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			xmlFile = chooser.getSelectedFile().getAbsolutePath();
			System.out.println("Reading file : " + xmlFile);

			try {
				Scanner sc = new Scanner(new File(xmlFile));

				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					if (line.startsWith("    <edge")) {
						String[] properties = line.split("\"");
						String id = properties[1];
						String from = properties[3];
						String to = properties[5];
						// int priority = Integer.parseInt(properties[7]);
						// String type = properties[9];
						int laneNum = Integer.parseInt(properties[11]);
						double speed = 30;
						// if(properties.length >= 13) {
						// speed = Double.parseDouble(properties[13]);
						// }

                        Node a = nodeMap.get(from);
                        Node b = nodeMap.get(to);


                        /**
                         * This doubles the edges because of the edge going "from" to "to" and not vice versa, leading to issues;
                         */
                        Edge e = new Edge(nodeMap.get(from), nodeMap.get(to), (int) speed, laneNum);
                        edgeMap.put(id, e);
                        nodeMap.get(from).addOutEdge(e);
                        nodeMap.get(to).addInEdge(e);
                        // increment node's Edge counter here
                        nodeMap.get(from).setNodePriorityWeight(nodeMap.get(from).getNodePriorityWeight()+2);
                        nodeMap.get(to).setNodePriorityWeight(nodeMap.get(to).getNodePriorityWeight()+2);
                        // edgeMap.put(id + "#2", new Edge(nodeMap.get(to), nodeMap.get(from), (int) speed, laneNum));

                    }
                }

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return edgeMap;
	}

	public void printAll(HashMap<String, Node> nodeMap, HashMap<String, Edge> edgeMap) {
		int counter = 0;
		for (Node n : nodeMap.values()) {
			System.out.println("Node " + counter + " is at X: " + n.getX() + " Y: " + n.getY());
			counter++;
		}
		/*
		 * System.out.println("--------------------"); counter = 0; for(Edge e :
		 * edgeMap.values()) { System.out.println("Edge " + counter + " goes from " +
		 * e.getFrom().toString() + " to " + e.getTo().toString()); counter++; }
		 */
	}
}

