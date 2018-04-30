package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class SimpleDriverModel implements DriverModel{

	private final static int SPEED = 50;
	
	int speed;
	
	public SimpleDriverModel() {
		this.speed = SPEED;
	}
	
	public SimpleDriverModel(int speed) {
		this.speed = speed;		
	}
	
	public String toString() {
		return "[SimpleDriverModel " + speed + "]";
	}

	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep) {
		if (timestep==0)
			return 0;
		
		if(vehicle.getSpeedAt(timestep-1) < speed)
			return speed/10;
		
		return 0;
	}

}
