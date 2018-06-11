package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public interface DriverModel {
	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep);
}
