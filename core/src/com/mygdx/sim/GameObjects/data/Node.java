package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.Stoplight;

public class Node implements Comparable<Node> {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Node previousNode;
	private Coordinates location;
	private double nodeDistanceWeight;
	private double nodeDistanceWeightEstimate;
	private int numLanesAttached;
	
	private int nodePriorityWeight = 0;
	public boolean hasWeight = false;
	private boolean isDestination;
	private String type;
	private String xmlID;
	private boolean isIntersection = false;

	private List<Integer> spawntimes = new ArrayList<Integer>();
	
	public static int lastGivenId = 0;
	
	private int id;

	public Node(float xCoordinate, float yCoordinate, String type, String xmlID) {
		location = new Coordinates(xCoordinate, yCoordinate);
		this.type = type;
		this.xmlID = xmlID;
		this.id = lastGivenId++;
	}

    public Node(float xCoordinate, float yCoordinate) {
        location = new Coordinates(xCoordinate, yCoordinate);
        this.id = lastGivenId++;
	}

	public Node(Coordinates coords) {
		location = coords;
	}

	public boolean isHasWeight(){
		return hasWeight;
	}

	public String getXmlID() {
		return xmlID;
	}

	public void setNumLanesAttached(int numLanes){
		numLanesAttached = numLanes;
	}

	public int getNumLanesAttached(){
		return numLanesAttached;
	}

//	public Node(Coordinates coords) {
//		this(coords.getX(),coords.getY());
//	}
	
	public int getId() { return id; }
	
	public int hashCode() { return id; }

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
		return "[Node " + id + " @" + location + "]";
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
		hasWeight = true;
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

		neighbors.addAll(getOutgoingNeighbors());
		neighbors.addAll(getIncomingNeighbors());

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

	public List<Integer> getSpawntimes() {
		return spawntimes;
	}

	public void setSpawntimes(List<Integer> spawntimes) {
		this.spawntimes = spawntimes;
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
