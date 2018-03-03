package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class SimpleDriverModel implements DriverModel{

	private final static double SPEED = 50;

	public double determineNewSpeed(TrafficManager mgr, Vehicle vehicle, int timestep) {
		return SPEED;
	}

}
