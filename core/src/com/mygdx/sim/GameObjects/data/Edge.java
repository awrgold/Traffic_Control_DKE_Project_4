package com.mygdx.sim.GameObjects.data;

public class Edge {

    private Node from;
    private Node to;
    private int speedLimit;
    private double lengthScore;

    public Edge(Node from, Node to){
        this(from,to,50);
    }
    
    public Edge(Node from, Node to, int speedLimit){
        this.from = from;
        from.addOutEdge(this);
        
        this.to = to;
        to.addInEdge(this);
        
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
    	return Math.sqrt(Math.pow(md.getX(),2)+Math.pow(md.getY(),2));
    }

    public void setLengthScore(double newScore){
        this.lengthScore = newScore;
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
    	return (to.getLocation().subtract(from.getLocation()));
    }
    
    private Coordinates getManhattanDistanceTraveled() {
    	return (from.getLocation().subtractAbs(to.getLocation()));
    }
    
    public String toString() {
    	return ("["+from+","+to+"]");
    }
}
