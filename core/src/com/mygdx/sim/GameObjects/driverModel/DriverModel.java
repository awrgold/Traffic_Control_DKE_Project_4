package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Car;

public interface DriverModel {
	public double determineNewSpeed(TrafficManager mgr, Car car, int timestep);
}
