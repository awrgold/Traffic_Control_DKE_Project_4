package com.mygdx.sim.GameObjects.data;

public class DistanceAndSpeed {
	double distance;
	double speed;
	
	public DistanceAndSpeed(double distance, double speed) {
		this.distance = distance;
		this.speed = speed;		
	}
	
	public double getDistance() { return distance; }
	public double getSpeed() { return speed; }

	@Override
	public String toString() {
		return "[" + distance + "," + speed + "]";
	}
	
	

}
