package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class IntelligentDriverModel implements DriverModel{
	
	private final static int EXPONENT = 4;
	private final static double MINIMUM_GAP = 2.0;
	
	public double determineNewSpeed(TrafficManager mgr, Vehicle vehicle, int timestep) {
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		double maximumAcceleration = vehicle.getMaxAcceleration();
		double safetyHeadway = vehicle.getSafetyHeadway();
		
		return 0;
	}
	

}
