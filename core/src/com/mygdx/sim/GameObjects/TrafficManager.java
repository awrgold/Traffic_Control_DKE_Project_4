package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.data.DistanceAndVehicle;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.data.Util;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.vehicle.Car;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class TrafficManager {
	
	private final static int TIMESTEPS = 10;
	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 1000;
	
	private Map map;
	private List<Vehicle> vehicles;
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	private int lastComputedTimestep = 0;
	
	/**
	 * Run the simulation until the given timestep.
	 * @param finalTimeStep - timestep until which we are running the sim
	 */
	public void simulate(int finalTimeStep) {
		
		// Ensure the map's location cache has enough memory capacity
		map.ensureCapacity(finalTimeStep);
		
		// Ensure all Vehicles have enough memory capacity 
		for (Vehicle vehicle : vehicles) {
			vehicle.ensureCapacity(finalTimeStep);
		}
		
		while(lastComputedTimestep < finalTimeStep) {
			for(Vehicle vehicle : vehicles)
				map.getLocationCache().get(vehicle.getEdgeAt(lastComputedTimestep)).get(lastComputedTimestep).add(vehicle);
				
			// Set speeds for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's new speed
				double newSpeed = vehicle.getDriverModel().determineNewSpeed(this,vehicle,lastComputedTimestep);
				
				// Set the new speed
				vehicle.setSpeed(lastComputedTimestep, newSpeed);
				
				/* Ask our pathfinding algorithm for a path
				 It can still return the very same path - we're only giving it
				 the opportunity to change the path, not requiring it */
				vehicle.computePath(lastComputedTimestep);
			}
						
			// Increment the timestep
			lastComputedTimestep++;
			
			// Have the vehicles update their locations for the next timestep			
			for (Vehicle vehicle : vehicles)
				vehicle.move(lastComputedTimestep);
		
		}
	}
	
	/**
	 * Gets the distance to and speed of the closest vehicle in front of this vehicle.
	 * @param vehicle - vehicle in front of which we are checking
	 * @param timestep - timestep at which we are checking
	 * @return distance and speed of closest vehicle
	 */
	public DistanceAndSpeed getDistanceAndSpeedToClosestVehicle(Vehicle vehicle, int timestep) {
		Edge edge = vehicle.getEdgeAt(timestep);
		double distance = -vehicle.getTraveledDistance(timestep);
		
		DistanceAndVehicle dnv = getClosestVehicle(vehicle,edge,distance,timestep);
		
		if(dnv == null) return new DistanceAndSpeed(VIEW_DISTANCE,RIDICULOUS_SPEED);
		
		Vehicle closest = dnv.getVehicle();
		double distanceToClosest = dnv.getDistance();
		
		double speedOfClosest = RIDICULOUS_SPEED;
		
		if(timestep>0)
			speedOfClosest = closest.getSpeedAt(timestep-1);
		
		return new DistanceAndSpeed(distanceToClosest,speedOfClosest);
	}
	
	private DistanceAndVehicle getClosestVehicle(Vehicle vehicle, Edge currentEdge, double distanceUntilNow, int timestep) {
		List<Vehicle> vehiclesOnCurrentEdge = (List<Vehicle>) map.getLocationCache().get(currentEdge).get(timestep).clone();
		
		ArrayList<DistanceAndVehicle> candidates = new ArrayList<DistanceAndVehicle>();
		for (Vehicle vehicle2 : vehiclesOnCurrentEdge) {
			double distance = distanceUntilNow + vehicle2.getTraveledDistance(timestep);
			if(distance - Util.DELTA_EPSILON > 0)
				candidates.add(new DistanceAndVehicle(distance,vehicle2));			
		}
		
		DistanceAndVehicle closest = getClosestDistanceAndVehicleFromList(candidates, timestep);
		
		if(closest != null) {
			Vehicle closestVehicle = closest.getVehicle();
			double distanceToClosest = (distanceUntilNow + closestVehicle.getTraveledDistance(timestep));
			return new DistanceAndVehicle(distanceToClosest,closestVehicle);
		}
			
		
		distanceUntilNow += currentEdge.getLength();
		
		if((distanceUntilNow + Util.DELTA_EPSILON) >= VIEW_DISTANCE)
			return null;
		
		ArrayList<DistanceAndVehicle> vehiclesFromFollowingEdges = new ArrayList<DistanceAndVehicle>();
		
		for (Edge edge2 : currentEdge.getTo().getOutEdges())
			vehiclesFromFollowingEdges.add(getClosestVehicle(vehicle,edge2,distanceUntilNow,timestep));
		
		return getClosestDistanceAndVehicleFromList(vehiclesFromFollowingEdges,timestep);
	}
	
	private DistanceAndVehicle getClosestDistanceAndVehicleFromList(List<DistanceAndVehicle> list, int timestep) {

		Vehicle closestVehicle = null;
		double smallestDistance = VIEW_DISTANCE;

		for (DistanceAndVehicle dnv : list) {
			if(dnv == null) continue;
			Vehicle vehicle2 = dnv.getVehicle();
			
			double thisDistance = vehicle2.getTraveledDistance(timestep);
			if (thisDistance < smallestDistance) {
				closestVehicle = vehicle2;
				smallestDistance = thisDistance;
			}
		}
		if(closestVehicle == null) return null;
		
		return new DistanceAndVehicle(smallestDistance,closestVehicle);
	}
	
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(475,0);
		Node node3 = new Node(475,500);
		Edge edge = new Edge(node1,node2);
		Edge edge2 = new Edge(node2,node3);
		
		Map map = new Map(Arrays.asList(node1,node2,node3),Arrays.asList(edge,edge2));
		
		Car car = new Car(node1,node2,map);
		car.setEdgePath(Arrays.asList(edge,edge2));
		car.setDriverModel(new SimpleDriverModel(70));
		
		Car car2 = new Car(node1,node2,map);
		car2.setEdgePath(Arrays.asList(edge,edge2));
		car2.setDriverModel(new SimpleDriverModel(30));
		
		List cars = Arrays.asList(car,car2);
		
		TrafficManager tm = new TrafficManager();
		tm.setMap(map);
		
		tm.setVehicles(cars);
		
		tm.simulate(TIMESTEPS);
		
		int x=0;
	}
}
