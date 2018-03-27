package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;

public class Node implements Comparable<Node> {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Node previousNode;
	private Coordinates location;
	private double nodeDistanceWeight;
	private double nodeDistanceWeightEstimate;

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
		return "[Node@" + location + "]";
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setNodeDistanceWeight(double nodeDistanceWeight) {
		this.nodeDistanceWeight = nodeDistanceWeight;
	}

	public double getNodeDistanceWeight() {
		return nodeDistanceWeight;
	}

	public void setNodeDistanceWeightEstimate(double nodeDistanceWeightEstimate) {
		this.nodeDistanceWeightEstimate = nodeDistanceWeightEstimate;
	}

	public double getNodeDistanceEstimate() {
		return nodeDistanceWeightEstimate;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Node))
			return false;

		return this.getLocation().equals(((Node) o).getLocation());
	}

	public ArrayList<Node> getOutgoingNeighbors() {
		ArrayList<Node> outgoingNeighbors = new ArrayList<Node>();

		for (Edge e : outEdges) {
			outgoingNeighbors.add(e.getTo());
		}

		return outgoingNeighbors;
	}

	public int compareTo(Node node) {
		if (this.nodeDistanceWeightEstimate < node.getNodeDistanceEstimate())
			return -1;
		if (this.nodeDistanceWeightEstimate > node.getNodeDistanceEstimate())
			return 1;

		return 0;
	}
}
