package com.mygdx.sim.GameObjects;

import java.util.Arrays;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Graph;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Car;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class TrafficManager {
	private Graph map;
	private List<Vehicle> vehicles;
	
	public Graph getMap() {
		return map;
	}

	public void setMap(Graph map) {
		this.map = map;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	private int lastComputedTimestep = 0;
	
	public void simulate(int finalTimeStep) {
		
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
	
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(500,0);
		Edge edge = new Edge(node1,node2);
		
		TrafficManager tm = new TrafficManager();
		tm.setMap(new Graph(Arrays.asList(node1,node2),Arrays.asList(edge)));
		
		Car car = new Car(node1,node2);
		
		List cars = Arrays.asList(car);
		
		tm.setVehicles(cars);
		
		tm.simulate(5);
	}
}
