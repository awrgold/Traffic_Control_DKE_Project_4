package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class IntelligentDriverModel implements DriverModel{
	
	private final static int EXPONENT = 4;
	private final static double MINIMUM_GAP = 2.0;
	private final static double LINEAR_JAM_DISTANCE = 2.0;
	private final static double NON_LINEAR_JAM_DISTANCE = 3.0;
	
	
	public double determineNewSpeed(TrafficManager mgr, Vehicle vehicle, int timestep) {
		// Begin setup
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		double maximumAcceleration = vehicle.getMaxAcceleration();
		double safetyHeadway = vehicle.getSafetyHeadway();
		
		double currentSpeed = vehicle.getSpeedAt(timestep-1);
		
		double acceleration = 0.3;
		double deceleration = 3;
		
		DistanceAndSpeed nextVehicle = mgr.getDistanceAndSpeedToClosestVehicle(vehicle, timestep);
		double distanceToNextVehicle = nextVehicle.getDistance();
		double speedOfNextVehicle = nextVehicle.getSpeed();
		
		// End setup; Begin calculations
		
		double approachingRate = currentSpeed - speedOfNextVehicle;
		
		double desiredGap = LINEAR_JAM_DISTANCE
				+ NON_LINEAR_JAM_DISTANCE*Math.sqrt(currentSpeed/desiredSpeed)
				+ safetyHeadway * currentSpeed
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));
		
		double interactionTerm = Math.pow((desiredGap/distanceToNextVehicle),2);
		
		double freeRoadTerm = -Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double result = acceleration * (1 - freeRoadTerm - interactionTerm);
		
		return Math.max(result, 0);
	}
	

}
