package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public Graph() {}
	
	private void addNode(Node node) {
		nodes.add(node);		
	}
	
	private void addEdge(Edge edge) {
		edges.add(edge);		
	}
}
