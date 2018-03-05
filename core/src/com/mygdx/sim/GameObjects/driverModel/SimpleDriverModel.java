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

	public double determineNewSpeed(TrafficManager mgr, Vehicle vehicle, int timestep) {
		return speed;
	}

}
