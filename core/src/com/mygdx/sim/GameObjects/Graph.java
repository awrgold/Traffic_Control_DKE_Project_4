package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public Graph() {}
	
	public void addNode(Node node) {
		nodes.add(node);		
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);		
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public List<Edge> getEdges(){
		return edges;
	}
	
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(0,10);
		
		Edge edge = new Edge(node1,node2,50);
		
		Graph graph = new Graph();
		graph.addEdge(edge);
		graph.addNode(node1);
		graph.addNode(node2);
		
		System.out.println("Created graph");
	}
}
