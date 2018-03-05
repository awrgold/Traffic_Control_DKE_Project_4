package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class Map {
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	
	HashMap<Edge,ArrayList<List<Vehicle>>> vehiclesAtEdgeAtTimestep;
	
	public Map(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
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
		
		Map map = new Map(Arrays.asList(node1,node2),Arrays.asList(edge));
		
		System.out.println("Created map");
	}
}
