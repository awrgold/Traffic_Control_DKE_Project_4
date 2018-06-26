package com.mygdx.sim.GameObjects.driverModel;

import java.util.Random;

import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class IntelligentDriverModelPlus implements DriverModel{
	
	private final static int EXPONENT = 4;
	private final static double LINEAR_JAM_DISTANCE = 2.0;
	private final static double NON_LINEAR_JAM_DISTANCE = 3.0;
	private static final boolean DEBUG = false;

	private double maximumAcceleration = 1.4;
	private double safetyTimeHeadway = 1.5;

	/**
	 * Adherence: how closely the driver adheres to speed limit, 1.0 = 100% adherence. 1.1 = 10% over speed limit
	 * Variance: standard deviation of the normal curve, how far the drivers will deviate.
	 */
	private double speedLimitAdherence = 1.0;
	private double speedLimitVariance = 0.1;

	public double determineAcceleration(TrafficManager mgr, Vehicle vehicle, int timestep) {
		if(timestep == 0) 
			return 0;
		
		// Begin setup
		// setSpeedLimitAdherence();
		int desiredSpeed = (int)Math.round(vehicle.getMaxSpeed(timestep)*speedLimitAdherence);
		
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
				+ NON_LINEAR_JAM_DISTANCE*Math.sqrt(currentSpeed/desiredSpeed)
				+ currentSpeed*approachingRate/(2*Math.sqrt(maximumAcceleration*deceleration));
		
		
		double interactionTerm = Math.max((desiredGap/distanceToNextTrafficObject),0);
		
		interactionTerm = Math.pow(interactionTerm, 2);
		
		double freeRoadTerm = Math.pow((currentSpeed / desiredSpeed),EXPONENT);
		
		double acceleration = maximumAcceleration * Math.min(1 - freeRoadTerm, 1 - interactionTerm);
		
		return acceleration;
	}

	public void setSpeedLimitAdherence(){
		this.speedLimitAdherence = drawRandomNormal(speedLimitAdherence, speedLimitVariance);
	}

	public static double drawRandomNormal(double mean, double sd){
		Random r = new Random();
		double d = r.nextGaussian()*sd + mean;
		while (d < 0){
			d = r.nextGaussian()*sd + mean;
		}
		if(DEBUG){
			System.out.println("Speed multiplier: " + d );
		}
		return d;
	}
	

}
