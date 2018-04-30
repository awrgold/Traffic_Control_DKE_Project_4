package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public interface DriverModel {
	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep);
}
