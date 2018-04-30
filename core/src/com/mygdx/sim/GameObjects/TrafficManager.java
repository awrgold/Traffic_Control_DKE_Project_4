package com.mygdx.sim.GameObjects;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.data.DistanceAndVehicle;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.data.Util;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModel;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.vehicle.Car;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class TrafficManager {
	
	// Duration of the simulation (hours, minutes, seconds)
	public final static Time DURATION = new Time(8,0,0);
	
	// Sampling frequency. Larger number means higher fidelity of the model, but also more computation
	public final static int TIMESTEPS_PER_SECOND = 2;
	
	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 1000;

	private Map map;
	private List<Vehicle> vehicles;
	
	private int lastComputedTimestep = 0;
	
	private ArrayList<HashMap<Vehicle,Coordinates>> history = new ArrayList<HashMap<Vehicle,Coordinates>>();
	
	public TrafficManager(Map map, List<Vehicle> vehicles) {
		this.map = map;
		this.vehicles = vehicles;
		
		ensureCapacity(lastComputedTimestep);
		
		for(Vehicle vehicle : vehicles)
			history.get(lastComputedTimestep).put(vehicle, vehicle.getLocationCoordinates(lastComputedTimestep));
	}
	
	public Map getMap() {
		return map;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	
	/**
	 * Gets the locations of all vehicles at the given timestep.
	 * @param timestep - timestep for which you need the locations
	 * @return a HashMap that maps Vehicles to their Coordinates
	 */
	public HashMap<Vehicle,Coordinates> getState(int timestep){
		if(timestep >= history.size()) simulate(timestep);
		
		return history.get(timestep);
	}
	
	public ArrayList<HashMap<Vehicle,Coordinates>> getHistory(){
		return history;
	}
	
	/**
	 * Run the simulation until the given timestep.
	 * 
	 * @param finalTimeStep
	 *            - timestep until which we are running the sim
	 */
	public void simulate(int finalTimeStep) {

		// Ensure the map's location cache has enough memory capacity
		map.ensureCapacity(finalTimeStep);
		
		// Ensure the TrafficManager's car-location history has enough memory capacity
		this.ensureCapacity(finalTimeStep);
		
		// Ensure all Vehicles have enough memory capacity 
		for (Vehicle vehicle : vehicles)
			vehicle.ensureCapacity(finalTimeStep);
		
		while(lastComputedTimestep < finalTimeStep) {
			for(Vehicle vehicle : vehicles)
				map.getLocationCache().get(vehicle.getEdgeAt(lastComputedTimestep)).get(lastComputedTimestep).add(vehicle);
				
			// Set accelerations for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's acceleration
				double acceleration = vehicle.getDriverModel().determineAcceleration(this,vehicle,lastComputedTimestep);
				
				// Set the acceleration
				vehicle.accelerate(lastComputedTimestep, acceleration);

				/*
				 * Ask our pathfinding algorithm for a path It can still return
				 * the very same path - we're only giving it the opportunity to
				 * change the path, not requiring it
				 */
				vehicle.computePath(lastComputedTimestep);
			}

			// Increment the timestep
			lastComputedTimestep++;
			
			// Have the vehicles update their locations for the next timestep			
			for (Vehicle vehicle : vehicles) {
				vehicle.move(lastComputedTimestep);
				history.get(lastComputedTimestep).put(vehicle, vehicle.getLocationCoordinates(lastComputedTimestep));
			}
		}
	}
	
	/**
	 * Ensures that this TrafficManager's history has enough space for the given number
	 * of timesteps
	 * @param timestep - number of timesteps we need to be able to save history for
	 */
	private void ensureCapacity(int timestep) {
		while(history.size() <= timestep)
			history.add(new HashMap<Vehicle,Coordinates>());
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
		
		DistanceAndVehicle dnv = 
				getClosestVehicle(vehicle,edge,distance,timestep);
		
		if(dnv == null) return new DistanceAndSpeed(VIEW_DISTANCE,RIDICULOUS_SPEED);
		
		Vehicle closest = dnv.getVehicle();
		double distanceToClosest = dnv.getDistance();

		double speedOfClosest = RIDICULOUS_SPEED;

		if (timestep > 0)
			speedOfClosest = closest.getSpeedAt(timestep - 1);

		return new DistanceAndSpeed(distanceToClosest, speedOfClosest);
	}
	
	private DistanceAndVehicle getClosestVehicle(Vehicle vehicle, Edge currentEdge, double distanceUntilNow, int timestep) {
		List<Vehicle> vehiclesOnCurrentEdge = (List<Vehicle>) map.getLocationCache().get(currentEdge).get(timestep).clone();
		
		ArrayList<DistanceAndVehicle> candidates = new ArrayList<DistanceAndVehicle>();
		for (Vehicle vehicle2 : vehiclesOnCurrentEdge) {
			double distance = distanceUntilNow + vehicle2.getTraveledDistance(timestep);
			if(distance - Util.DELTA_EPSILON > 0 && vehicle2.isMoving())
				candidates.add(new DistanceAndVehicle(distance,vehicle2));			
		}
		
		DistanceAndVehicle closest = getClosestDistanceAndVehicleFromList(candidates, timestep);
		
		if(closest != null) {
			Vehicle closestVehicle = closest.getVehicle();
			double distanceToClosest = (distanceUntilNow + closestVehicle.getTraveledDistance(timestep));
			return new DistanceAndVehicle(distanceToClosest,closestVehicle);
		}			
		
		distanceUntilNow += currentEdge.getLength();

		if ((distanceUntilNow + Util.DELTA_EPSILON) >= VIEW_DISTANCE)
			return null;

		ArrayList<DistanceAndVehicle> vehiclesFromFollowingEdges = new ArrayList<DistanceAndVehicle>();

		for (Edge edge2 : currentEdge.getTo().getOutEdges())
			vehiclesFromFollowingEdges.add(getClosestVehicle(vehicle, edge2, distanceUntilNow, timestep));

		return getClosestDistanceAndVehicleFromList(vehiclesFromFollowingEdges, timestep);
	}

	private DistanceAndVehicle getClosestDistanceAndVehicleFromList(List<DistanceAndVehicle> list, int timestep) {

		Vehicle closestVehicle = null;
		double smallestDistance = VIEW_DISTANCE;

		for (DistanceAndVehicle dnv : list) {
			if (dnv == null)
				continue;
			Vehicle vehicle2 = dnv.getVehicle();
			
			double thisDistance = dnv.getDistance();
      
			if (thisDistance < smallestDistance) {
				closestVehicle = vehicle2;
				smallestDistance = thisDistance;
			}
		}
		if (closestVehicle == null)
			return null;

		return new DistanceAndVehicle(smallestDistance, closestVehicle);
	}

	public static TrafficManager createEnironment() {
		Node node1 = new Node(0,0);
		Node node2 = new Node(475,0);
		Node node3 = new Node(475,1000);
		Edge edge1 = new Edge(node1,node2);
		Edge edge2 = new Edge(node2,node3);
		
		Map map = new Map(Arrays.asList(node1,node2,node3),Arrays.asList(edge1,edge2));
		
		Car car1 = new Car(node2,node3,map);
		car1.setEdgePath(Arrays.asList(edge2));
		car1.setDriverModel(new SimpleDriverModel(10));
		
		Car car2 = new Car(node2,node3,map);
		car2.setEdgePath(Arrays.asList(edge2));
		car2.setDriverModel(new IntelligentDriverModel());
		
		Car car3 = new Car(node1,node3,map);
		car3.setEdgePath(Arrays.asList(edge1,edge2));
		car3.setDriverModel(new IntelligentDriverModel());
		
		List cars = Arrays.asList(car1,car2,car3);
		
		TrafficManager tm = new TrafficManager(map,cars);
		int y= 0;
		
		return tm;
		
	}
	
	public static double getDurationOfTimestepInSeconds() {
		return 1./TIMESTEPS_PER_SECOND;
	}
	
	public static int getMaximumTimesteps() {
		return TIMESTEPS_PER_SECOND*(DURATION.getSeconds() + 60 * (DURATION.getMinutes() + 60 * DURATION.getHours()));
	}
	
	public String toString() {
		return "[TrafficManager]";
	}
	
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(475,0);
		Node node3 = new Node(475,1000);
		Edge edge1 = new Edge(node1,node2);
		Edge edge2 = new Edge(node2,node3);
		
		Map map = new Map(Arrays.asList(node1,node2,node3),Arrays.asList(edge1,edge2));
		
		Car car1 = new Car(node2,node3,map);
		car1.setEdgePath(Arrays.asList(edge2));
		car1.setDriverModel(new SimpleDriverModel(10));
		
		Car car2 = new Car(node2,node3,map);
		car2.setEdgePath(Arrays.asList(edge2));
		car2.setDriverModel(new IntelligentDriverModel());
		
		Car car3 = new Car(node1,node3,map);
		car3.setEdgePath(Arrays.asList(edge1,edge2));
		car3.setDriverModel(new IntelligentDriverModel());
		
		List cars = Arrays.asList(car1,car2,car3);
		
		TrafficManager tm = new TrafficManager(map,cars);
		
		tm.simulate(getMaximumTimesteps());
		
		System.out.println("Done with " + tm.lastComputedTimestep + " timesteps.");
	}
}
