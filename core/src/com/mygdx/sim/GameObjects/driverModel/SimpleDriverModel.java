package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Car;

public class SimpleDriverModel implements DriverModel{

	private final static double SPEED = 50;

	public double determineNewSpeed(TrafficManager mgr, Car car, int timestep) {
		return SPEED;
	}

}
