package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;

public class Node implements Comparable<Node> {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Node previousNode;
	private Coordinates location;
	private double nodeDistanceWeight;
	private double nodeDistanceWeightEstimate;
	private int nodePriorityWeight = 0;
	private boolean isDestination;
	private String type;
	private boolean isIntersection = false;
	private boolean hasCarAlready = false;

	public Node(double xCoordinate, double yCoordinate, String type) {
		location = new Coordinates(xCoordinate, yCoordinate);
		this.type = type;
	}
    public Node(double xCoordinate, double yCoordinate) {
        location = new Coordinates(xCoordinate, yCoordinate);

    }

	public Node(){}

	public Node(Coordinates coords) {
		location = coords;
	}

	public Coordinates getLocation() {
		return location;
	}

	public void setLocation(Coordinates coords){
		this.location = coords;
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

	public boolean hasCarAlready(){
		return hasCarAlready;
	}

	public void setHasCarAlready(){
		if (!hasCarAlready) hasCarAlready = true;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public int getNodePriorityWeight() {
		return nodePriorityWeight;
	}

	public void setNodePriorityWeight(int nodePriorityWeight) {
		this.nodePriorityWeight = nodePriorityWeight;
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

	public ArrayList<Node> getIncomingNeighbors() {
		ArrayList<Node> incomingNeighbors = new ArrayList<Node>();

		for (Edge e : inEdges) {
			incomingNeighbors.add(e.getFrom());
		}

		return incomingNeighbors;
	}

	public boolean isDestination() {
		return isDestination;
	}

	public void makeDestination() {
		if (!isDestination) isDestination = true;
	}


	public int compareTo(Node node) {
		if (this.nodePriorityWeight < node.getNodePriorityWeight())
			return -1;
		if (this.nodePriorityWeight > node.getNodePriorityWeight())
			return 1;
		return 0;
	}

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public boolean isIntersection() {
        return isIntersection;
    }
    
    public void setIntersection(boolean isIntersection) {
        this.isIntersection = isIntersection;
    }
}
