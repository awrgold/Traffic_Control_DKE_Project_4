package com.mygdx.sim.GameObjects.driverModel;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class IntelligentDriverModelPlus implements DriverModel{
	
	private final static int EXPONENT = 4;
	private final static double LINEAR_JAM_DISTANCE = 2.0;
	private final static double NON_LINEAR_JAM_DISTANCE = 3.0;	
	
	private double maximumAcceleration = 1.4;
	
	private double safeDeceleration = -1.5;

	private double safetyTimeHeadway = 1.5;
	
	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep) {
		if(timestep == 18)
			System.out.println("bla");
		
		if(timestep == 0) 
			return 0;
		
		ArrayList<Edge> lanesToSynchronizeWith = new ArrayList<Edge>();
		
		for(Entry<Edge,Double> entry :	vehicle.getLaneChangeDesires().entrySet())
			if(entry.getValue() > Vehicle.DSYNC)
				lanesToSynchronizeWith.add(entry.getKey());
		
		double[] accelerationsDueToSynch = new double[10];
		
		for(int i = 0 ; i < Math.min(lanesToSynchronizeWith.size(),10); i++) {
			Location simulatedLocation = new Location(lanesToSynchronizeWith.get(i),vehicle.getDistanceOnEdge(timestep));
			accelerationsDueToSynch[i] = determineAcceleration(mgr, vehicle, simulatedLocation, timestep);
		}
		
		double[] accelerationsDueToMergers = new double[10];
		
		for(int i = 0 ; i < Math.min(vehicle.mergers.size(),10); i++)		
			accelerationsDueToMergers[i] = determineAcceleration(mgr, vehicle, vehicle.mergers.get(i), timestep);
		
		// Begin setup
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		
		double vehicleLength = vehicle.getLength();
		
		double currentSpeed = vehicle.getSpeed(timestep-1);
		double deceleration = 3;
		
		DistanceAndSpeed nextTrafficObject = mgr.getDistanceAndSpeedToClosestTrafficObject(vehicle, timestep);
		double distanceToNextTrafficObject = nextTrafficObject.getDistance();
		double speedOfNextTrafficObject = nextTrafficObject.getSpeed();
		
		// End setup; Begin calculations
		
		double approachingRate = currentSpeed - speedOfNextTrafficObject;
		
		double safetyHeadway = safetyTimeHeadway * currentSpeed;
		
		double desiredGap = LINEAR_JAM_DISTANCE
				+ vehicleLength
				+ safetyHeadway
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));		
		
		double interactionTerm = Math.max((desiredGap/distanceToNextTrafficObject),0);
		
		interactionTerm = Math.pow(interactionTerm, 2);
		
		double freeRoadTerm = Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double acceleration = maximumAcceleration * Math.min(1 - freeRoadTerm, 1 - interactionTerm);
		
		for(int i = 0; i < Math.min(vehicle.mergers.size(),10); i++)
			if(accelerationsDueToMergers[i] < acceleration && accelerationsDueToMergers[i] > safeDeceleration)
				acceleration = accelerationsDueToMergers[i];
		
		for(int i = 0 ; i < Math.min(lanesToSynchronizeWith.size(),10); i++)
			if(accelerationsDueToSynch[i] < acceleration)
				if(accelerationsDueToSynch[i] > safeDeceleration)
					acceleration = accelerationsDueToSynch[i];
				else acceleration = safeDeceleration;
		
		return acceleration;
	}
	
	private double determineAcceleration(TrafficManager mgr, Vehicle vehicle, Location simulatedLocation, int timestep) {
		if(timestep == 0) 
			return 0;
		
		// Begin setup
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		
		double vehicleLength = vehicle.getLength();
		
		double currentSpeed = vehicle.getSpeed(timestep-1);
		double deceleration = 3;
		
		DistanceAndSpeed nextTrafficObject = mgr.getDistanceAndSpeedToClosestTrafficObject(vehicle, simulatedLocation, timestep);
		double distanceToNextTrafficObject = nextTrafficObject.getDistance();
		double speedOfNextTrafficObject = nextTrafficObject.getSpeed();
		
		// End setup; Begin calculations
		
		double approachingRate = currentSpeed - speedOfNextTrafficObject;
		
		double safetyHeadway = safetyTimeHeadway * currentSpeed;
		
		double desiredGap = LINEAR_JAM_DISTANCE
				+ vehicleLength
				+ safetyHeadway
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));
		
		double interactionTerm = Math.max((desiredGap/distanceToNextTrafficObject),0);
		
		interactionTerm = Math.pow(interactionTerm, 2);
		
		double freeRoadTerm = Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double acceleration = maximumAcceleration * Math.min(1 - freeRoadTerm, 1 - interactionTerm);
		
		return acceleration;
	}
	
	private double determineAcceleration(TrafficManager mgr, Vehicle vehicle, DistanceAndSpeed simulatedTrafficObject, int timestep) {
		if(timestep == 0) 
			return 0;
		
		// Begin setup
		int desiredSpeed = vehicle.getMaxSpeed(timestep);
		
		double vehicleLength = vehicle.getLength();
		
		double currentSpeed = vehicle.getSpeed(timestep-1);
		double deceleration = 3;
		
		DistanceAndSpeed nextTrafficObject = simulatedTrafficObject;
		double distanceToNextTrafficObject = nextTrafficObject.getDistance();
		double speedOfNextTrafficObject = nextTrafficObject.getSpeed();
		
		// End setup; Begin calculations
		
		double approachingRate = currentSpeed - speedOfNextTrafficObject;
		
		double safetyHeadway = safetyTimeHeadway * currentSpeed;
		
		double desiredGap = LINEAR_JAM_DISTANCE
				+ vehicleLength
				+ safetyHeadway
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));
		
		
		double interactionTerm = Math.max((desiredGap/distanceToNextTrafficObject),0);
		
		interactionTerm = Math.pow(interactionTerm, 2);
		
		double freeRoadTerm = Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double acceleration = maximumAcceleration * Math.min(1 - freeRoadTerm, 1 - interactionTerm);
		
		return acceleration;
	}
	

}
