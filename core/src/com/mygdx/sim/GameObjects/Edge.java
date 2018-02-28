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

    public double getLength(){
        return Math.abs(Math.sqrt((to.getX() - from.getX()) + to.getY() - from.getY()));
    }
    
    public Coordinates getLocationIfTraveledDistance(double traveledDistance) {
    	double fraction = traveledDistance/getLength();
    	return getLocationIfTraveledFraction(fraction);
    }
    
    public Coordinates getLocationIfTraveledFraction(double traveledFraction) {
    	Coordinates change = getCoordinateChange();
    	double startX = from.getX();
    	double startY = from.getY();
    	return new Coordinates(startX + change.getX()*traveledFraction, 
    			startY + change.getY()*traveledFraction);
    }
    
    /** Returns a Coordinates object that describes the change in X and Y that you experience when
     * going from fromNode to toNode 
     */
    private Coordinates getCoordinateChange() {
    	return (from.getLocation().subtract(to.getLocation()));
    }






}
