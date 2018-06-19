package com.mygdx.sim.GameObjects.data;

import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class DistanceAndTrafficObject {
	double distance;
	TrafficObject to;
	
	public DistanceAndTrafficObject(double distance, TrafficObject to) {
		this.distance = distance;
		this.to = to;		
	}
	
	public String toString() {
		return ("["+distance+","+to+"]");
	}
	
	public double getDistance() { return distance; }
	public TrafficObject getTrafficObject() { return to; }

}
