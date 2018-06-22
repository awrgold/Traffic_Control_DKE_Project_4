package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.Stoplight;

public class Edge implements Comparable<Edge> {

	// Node connection
    private Node from;
    private Node to;
    
    // Edge Lists
    private List<Edge> neighbors;
    private List<Edge> toEdges;
    
    // Properties
    private int speedLimit;
    private double weight;
    private int numLanes;
    private int lane;
    
    // StopLight
    private Stoplight stopLight;
    
    private static int lastGivenId = 0;
    private int id;
    
    // UI
    private List<Coordinates> shapeCoordinates = null;

    public Edge(Node from, Node to){
        this(from,to,50);
    }
    
    public Edge(Node from, Node to, int speedLimit){
        this.from = from;
        from.addOutEdge(this);
        
        this.to = to;
        to.addInEdge(this);
        
        this.speedLimit = speedLimit;
        this.weight = (getLength()/getSpeedLimit());
        
        this.id = lastGivenId++;
    }

    public Edge(Node from, Node to, int speedLimit, int lane, int numLanes, List<Coordinates> shapeCoordinates){
        this.from = from;
        from.addOutEdge(this);

        this.to = to;
        to.addInEdge(this);

        this.speedLimit = speedLimit;
        this.lane = lane;
        this.numLanes = numLanes;
        this.shapeCoordinates = shapeCoordinates;
        
        this.weight = (getLength()/getSpeedLimit());
        
        this.toEdges = new ArrayList<Edge>();
    }
    
    public int hashCode() {
    	return id;
    }
    
    public int getId() {
    	return id;
    }
    
    public boolean equals(Object o) {
    	if(!(o instanceof Edge))
    		return false;
    	
    	Edge edge2 = (Edge) o;
    	
    	if(this.id == edge2.id)
    		return true;
    	
    	return false;
    }

    public Node getFrom(){
        return from;
    }

    public Node getTo(){
        return to;
    }

    public int getLane(){
        return lane;
    }
    
    public int getNumLanes() {
    	return numLanes;
    }
    
    public Stoplight getStopLight() {
    	return stopLight;
    }
    
    public void setStopLight(Stoplight stopLight) {
    	this.stopLight = stopLight;
    }
    
    public List<Coordinates> getShapeCoordinates() {
    	return shapeCoordinates;
    }

    public void addNeighbor(Edge neighbor){
        neighbors.add(neighbor);
    }

    public void addNeighborAt(Edge neighbor, int index){
        for (int i = 0; i < neighbors.size(); i++){
            neighbors.add(index, neighbor);
        }
    }

    public void addNeighborList(List<Edge> neighbors){
        this.neighbors.addAll(neighbors);
    }

    public double getWeight(){
        return weight;
    }


    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }

    public double getSpeedLimit(){
        return speedLimit;
    }
    
    public List<Edge> getToEdges() {
    	return toEdges;
    }
    
    public void addToEdge(Edge edge) {
    	toEdges.add(edge);
    }

    /**
     * Returns the length of the road section that this edge represents.
     * @return double
     */
    public float getLength(){
    	Coordinates md = getManhattanDistanceTraveled();
    	return ((float) Math.sqrt(Math.pow(md.getX(),2)+Math.pow(md.getY(),2)));
    }

    public void setLengthScore(double newScore){
        this.weight = newScore;
    }
    
    /**
     * Gives you your (x,y) coordinates if you have traveled a certain distance on this edge.
     * E.g. edge is from (0,0) to (10,10) and you've traveled 5*sqrt(2) on it - that means
     * you're at (5,5)
     * @param traveledDistance - distance that you have traveled on the edge
     * @return (x,y) Coordinates
     */
    public Coordinates getLocationIfTraveledDistance(float traveledDistance) {
    	float fraction = traveledDistance/getLength();
    	return getLocationIfTraveledFraction(fraction);
    }
    
    /**
     * Gives you your (x,y) coordinates if you have traveled a certain fraction of this edge.
     * E.g. edge is from (0,0) to (10,10) and you've traveled 50% of it - that means you're at
     * (5,5)
     * @param traveledFraction - fraction of the edge's total length that you have traveled
     * @return (x,y) Coordinates
     */
    public Coordinates getLocationIfTraveledFraction(float traveledFraction) {
    	Coordinates change = getCoordinateChange();
    	float startX = (float) from.getX();
    	float startY = (float) from.getY();
    	return new Coordinates(startX + change.getX()*traveledFraction, 
    			startY + change.getY()*traveledFraction);
    }
    
    /** Returns a Coordinates object that describes the change in X and Y that you experience when
     * going from fromNode to toNode
     * e.g. if you're going from (5,-5) to (-5,10) this method will return (-10,15)
     */
    private Coordinates getCoordinateChange() {
    	return (to.getLocation().subtract(from.getLocation()));
    }
    
    private Coordinates getManhattanDistanceTraveled() {
    	return (from.getLocation().subtractAbs(to.getLocation()));
    }
    
    public String toString() {
    	return ("[Edge " + id + ":"+from+","+to+"]");
    }

    public int compareTo(Edge edge) {
        if((this.getFrom().equals(edge.getFrom()) || this.getFrom().equals(edge.getTo())) &&
                (this.getTo().equals(edge.getFrom()) || this.getTo().equals(edge.getTo()))){
            return 0;

        }
        return -1;
    }

}
