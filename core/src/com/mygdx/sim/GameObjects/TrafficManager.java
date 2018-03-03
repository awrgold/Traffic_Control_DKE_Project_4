package com.mygdx.sim.GameObjects;

import java.util.List;

import com.mygdx.sim.GameObjects.data.Graph;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class TrafficManager {
	private Graph map;
	private List<Vehicle> vehicles;
	
	private int lastComputedTimestep = 0;
	
	public void compute(int finalTimeStep) {
		while(lastComputedTimestep < finalTimeStep) {
			
			// Set speeds for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's new speed
				double newSpeed = vehicle.getDriverModel().determineNewSpeed(this,vehicle,lastComputedTimestep);
				
				// Set the new speed
				vehicle.setSpeed(lastComputedTimestep, newSpeed);
			}
						
			// Increment the timestep
			lastComputedTimestep++;
			
			// Have the vehicles update their locations for the next timestep			
			for (Vehicle vehicle : vehicles)
				vehicle.move(lastComputedTimestep);
			
		}
	}
}
