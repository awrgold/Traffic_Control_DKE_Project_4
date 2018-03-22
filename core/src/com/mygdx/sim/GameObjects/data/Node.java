package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;

public class Node {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Node parent;
	private Coordinates location;
	private double nodeDistanceWeight;

	public Node(double xCoordinate, double yCoordinate) {
		location = new Coordinates(xCoordinate, yCoordinate);
	}

	public Node(Coordinates coords) {
		location = coords;
	}

	public Coordinates getLocation() {
		return location;
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public void addInEdge(Edge toAdd) {
		inEdges.add(toAdd);
	}

	public void addOutEdge(Edge toAdd) {
		outEdges.add(toAdd);
	}

	public ArrayList<Edge> getInEdges() {
		return inEdges;
	}

	public ArrayList<Edge> getOutEdges() {
		return outEdges;
	}
	
	public String toString() {
		return "[Node@"+location+"]";
	}

	public void setParent(Node parent){
		this.parent = parent;
	}

	public Node getParent(){
		return parent;
	}

	public void setNodeDistanceWeight(double d){
		this.nodeDistanceWeight = d;
	}

	public double getNodeDistanceWeight(){
		return nodeDistanceWeight;
	}

	public ArrayList<Node> getOutgoingNeighbors(){
		ArrayList<Node> outgoingNeighbors = new ArrayList<Node>();
		for (Edge e : outEdges){
			outgoingNeighbors.add(e.getTo());
		}
		return outgoingNeighbors;
	}



}
