package com.mygdx.sim.GameObjects.data;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapReader {
    public static void main(String[] args){
        MapReader instance = new MapReader();

        HashMap<String,Node> nodeMap = instance.readNodes();
        HashMap<String,Edge> edgeList = instance.readEdges(nodeMap);

    }

    public HashMap<String, Node> readNodes() {
        //Choosing the Node xml file

        HashMap<String, Node> nodeMap = new HashMap<String, Node>();

        String xmlFile = null;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Node XML file", "xml");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            xmlFile = chooser.getSelectedFile().getAbsolutePath();
            System.out.println("Reading file : " + xmlFile);

            try {
                Scanner sc = new Scanner(new File(xmlFile));

                while(sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if(line.startsWith("    <node")) {
                        String[] properties = line.split("\"");
                        String id = properties[1];
                        double x = Double.parseDouble(properties[3]);
                        double y = Double.parseDouble(properties[5]);
                        String type = properties[7];

                        nodeMap.put(id,new Node(x,y));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return nodeMap;
    }

    public HashMap<String,Edge> readEdges(HashMap<String, Node> nodeMap) {
        HashMap<String,Edge> edgeMap = new HashMap<String, Edge>();

        String xmlFile = null;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Edge XML file", "xml");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            xmlFile = chooser.getSelectedFile().getAbsolutePath();
            System.out.println("Reading file : " + xmlFile);

            try {
                Scanner sc = new Scanner(new File(xmlFile));

                while(sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if(line.startsWith("    <edge")) {
                        String[] properties = line.split("\"");
                        String id = properties[1];
                        String from = properties[3];
                        String to = properties[5];
                        int priority = Integer.parseInt(properties[7]);
                        String type = properties[9];
                        double speed = Double.parseDouble(properties[13]);

                        edgeMap.put(id, new Edge(nodeMap.get(from),nodeMap.get(to), (int) speed));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return edgeMap;
    }
}

