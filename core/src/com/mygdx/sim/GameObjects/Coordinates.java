package com.mygdx.sim.GameObjects;

/**
 * Immutable data class containing width and height coordinates.
 * @author bvsla
 *
 */

public class Coordinates {
	/**
	 * The minimum difference between two numbers for them to be considered different.
	 * If the difference is smaller, the numbers are considered equal.
	 */
	private final static double DELTA_EPSILON = 0.00001;
	
	/** Width */
	private double x;
	/** Height */
    private double y;
    
    public Coordinates(double x, double y) {
    	this.x = x;
    	this.y = y;
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
    	
    	if(Math.abs(getX() - coords2.getX()) > DELTA_EPSILON) return false;    	
    	if(Math.abs(getY() - coords2.getY()) > DELTA_EPSILON) return false;
    	
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
}
