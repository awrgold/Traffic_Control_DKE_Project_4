package com.mygdx.sim.GameObjects;

public class Edge {

    private Node from;
    private Node to;
    private int speedLimit;

    public Edge(Node from, Node to){
        this.from = from;
        this.to = to;
    }
    
    public Edge(Node from, Node to, int speedLimit){
        this.from = from;
        this.to = to;
        this.speedLimit = speedLimit;
    }
    
    public Node getFrom(){
        return from;
    }

    public Node getTo(){
        return to;
    }

    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimit(){
        return speedLimit;
    }

    /**
     * Returns the length of the road section that this edge represents.
     * @return double
     */
    public double getLength(){
    	Coordinates md = getManhattanDistanceTraveled();
    	return Math.sqrt(md.getX()+md.getY());
    }
    
    /**
     * Gives you your (x,y) coordinates if you have traveled a certain distance on this edge.
     * E.g. edge is from (0,0) to (10,10) and you've traveled 5*sqrt(2) on it - that means
     * you're at (5,5)
     * @param traveledDistance - distance that you have traveled on the edge
     * @return (x,y) Coordinates
     */
    public Coordinates getLocationIfTraveledDistance(double traveledDistance) {
    	double fraction = traveledDistance/getLength();
    	return getLocationIfTraveledFraction(fraction);
    }
    
    /**
     * Gives you your (x,y) coordinates if you have traveled a certain fraction of this edge.
     * E.g. edge is from (0,0) to (10,10) and you've traveled 50% of it - that means you're at
     * (5,5)
     * @param traveledFraction - fraction of the edge's total length that you have traveled
     * @return (x,y) Coordinates
     */
    public Coordinates getLocationIfTraveledFraction(double traveledFraction) {
    	Coordinates change = getCoordinateChange();
    	double startX = from.getX();
    	double startY = from.getY();
    	return new Coordinates(startX + change.getX()*traveledFraction, 
    			startY + change.getY()*traveledFraction);
    }
    
    /** Returns a Coordinates object that describes the change in X and Y that you experience when
     * going from fromNode to toNode
     * e.g. if you're going from (5,-5) to (-5,10) this method will return (-10,15)
     */
    private Coordinates getCoordinateChange() {
    	return (from.getLocation().subtract(to.getLocation()));
    }
    
    private Coordinates getManhattanDistanceTraveled() {
    	return (from.getLocation().subtractAbs(to.getLocation()));
    }
}
