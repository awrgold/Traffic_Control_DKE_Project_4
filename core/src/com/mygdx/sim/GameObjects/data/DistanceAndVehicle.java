package com.mygdx.sim.GameObjects.data;

import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class DistanceAndVehicle {
	double distance;
	Vehicle vehicle;
	
	public DistanceAndVehicle(double distance, Vehicle vehicle) {
		this.distance = distance;
		this.vehicle = vehicle;		
	}
	
	public String toString() {
		return ("["+distance+","+vehicle+"]");
	}
	
	public double getDistance() { return distance; }
	public Vehicle getVehicle() { return vehicle; }

}
