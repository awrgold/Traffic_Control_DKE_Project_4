package com.mygdx.sim.GameObjects.data;

/**
 * Immutable data class containing width and height coordinates.
 * @author bvsla
 *
 */

public class Coordinates {
	
	/** Width */
	private double x;
	/** Height */
    private double y;
    
    public Coordinates(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
    public Coordinates add(Coordinates coords) {
    	return add(coords.getX(),coords.getY());    	
    }
    
    public Coordinates add(double x, double y) {
    	return new Coordinates(this.x+x,this.y+y);
    }
    
    public Coordinates subtract(Coordinates coords) {
    	return subtract(coords.getX(),coords.getY());
    }
    
    public Coordinates subtract(double x, double y) {
    	return new Coordinates(this.x-x,this.y-y);
    }
    
    public Coordinates subtractAbs(Coordinates coords) {
    	return subtractAbs(coords.getX(),coords.getY());
    }
    
    public Coordinates subtractAbs(double x, double y) {
    	return new Coordinates(Math.abs(this.x-x),Math.abs(this.y-y));
    }
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    public boolean equals(Object o) {
    	if(!(o instanceof Coordinates)) return false;
    	
    	Coordinates coords2 = (Coordinates) o;
    	
    	if(Math.abs(getX() - coords2.getX()) > Util.DELTA_EPSILON) return false;    	
    	if(Math.abs(getY() - coords2.getY()) > Util.DELTA_EPSILON) return false;
    	
    	return true;
    }
    
    // Test of the 'equals' method
    public static void main(String[] args) {
    	Coordinates coords1 = new Coordinates(10,20);    	
    	Coordinates coords2 = new Coordinates(10,20);
    	Coordinates coords3 = new Coordinates(10,20.000000001);
    	Coordinates coords4 = new Coordinates(10,20.0001);
    	Object redFlag = new Object();
    	
    	if(coords1.equals(coords2)) 
    		System.out.println("1 and 2 are equal");
    	else System.out.println("1 and 2 are NOT equal");
    	
    	if(coords1.equals(coords3)) 
    		System.out.println("1 and 3 are equal");
    	else System.out.println("1 and 3 are NOT equal");
    	
    	if(coords1.equals(coords4)) 
    		System.out.println("1 and 4 are equal");
    	else System.out.println("1 and 4 are NOT equal");
    	
    	if(coords1.equals(redFlag))
    		System.out.println("1 equals redFlag");
    	else System.out.println("1 and redFlag are NOT equal");
    }
    
    public String toString() {
    	return ("["+x+","+y+"]");
    }
}
