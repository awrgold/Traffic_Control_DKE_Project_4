package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.mygdx.sim.GameObjects.Stoplight;

public class Node implements Comparable<Node> {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Node previousNode;
	private Coordinates location;
	private double nodeDistanceWeight;
	private double nodeDistanceWeightEstimate;
	private UUID ID;
	private int nodePriorityWeight = 0;
	private boolean isDestination;
	private String type;
	private boolean isIntersection = false;
	private List<Stoplight> lights;

	public Node(float xCoordinate, float yCoordinate, String type) {
		location = new Coordinates(xCoordinate, yCoordinate);
		this.type = type;
		ID = new UUID(Integer.MAX_VALUE, 0);
	}

    public Node(float xCoordinate, float yCoordinate) {
        location = new Coordinates(xCoordinate, yCoordinate);
		ID = new UUID(Integer.MAX_VALUE, 0);
	}

    public void setLights(List<Stoplight> lights){
		this.lights = lights;
	}



	public int getNodeID(){
		return ID.variant();
	}

	public void addLight(Stoplight light){
    	if (lights != null){
			lights.add(light);
		}else{
    		lights = new ArrayList<Stoplight>();
    		lights.add(light);
		}

	}

	public List<Stoplight> getLights(){
    	return lights;
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

	public float getX() {
		return location.getX();
	}

	public float getY() {
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

	public ArrayList<Node> getNeighbors(){
		ArrayList<Node> neighbors = new ArrayList<Node>();

		for (Edge e : inEdges){
			neighbors.add(e.getFrom());
		}
		for (Edge e : outEdges){
			neighbors.add(e.getTo());
		}
		return neighbors;
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
