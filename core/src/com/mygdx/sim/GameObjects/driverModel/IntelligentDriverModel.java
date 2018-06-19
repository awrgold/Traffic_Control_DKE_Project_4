package com.mygdx.sim.GameObjects.driverModel;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class IntelligentDriverModel implements DriverModel{
	
	private final static int EXPONENT = 4;
	private final static double LINEAR_JAM_DISTANCE = 2.0;
	private final static double NON_LINEAR_JAM_DISTANCE = 3.0;
	
	private double maximumAcceleration = 1.4;

	private double safetyTimeHeadway = 1.5;
	
	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep) {
		if(timestep == 0) 
			return 0;
		
		// Begin setup
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		
		double vehicleLength = vehicle.getLength();
		
		double currentSpeed = vehicle.getSpeedAt(timestep-1);
		
		double deceleration = 3;
		
		DistanceAndSpeed nextVehicle = mgr.getDistanceAndSpeedToClosestVehicle(vehicle, timestep);
		double distanceToNextVehicle = nextVehicle.getDistance();
		double speedOfNextVehicle = nextVehicle.getSpeed();
		
		// End setup; Begin calculations
		
		double approachingRate = currentSpeed - speedOfNextVehicle;
		
		double safetyHeadway = safetyTimeHeadway * currentSpeed;
		
		double desiredGap = LINEAR_JAM_DISTANCE
				+ vehicleLength
				+ NON_LINEAR_JAM_DISTANCE*Math.sqrt(currentSpeed/desiredSpeed)
				+ safetyHeadway
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));
		
		
		double interactionTerm = Math.max((desiredGap/distanceToNextVehicle),0);
		
		interactionTerm = Math.pow(interactionTerm, 2);
		
		double freeRoadTerm = Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double acceleration = maximumAcceleration * (1 - freeRoadTerm - interactionTerm);
		
		return acceleration;
	}
	

}
