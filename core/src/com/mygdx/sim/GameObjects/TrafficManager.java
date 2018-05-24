package com.mygdx.sim.GameObjects;
import java.lang.reflect.Array;
import java.util.*;

import com.mygdx.sim.GameObjects.data.*;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModel;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.vehicle.Car;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class TrafficManager {
	
	public final static int TIMESTEPS = 1000;
	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 1000;

	// Temporary map bounds
	public final static int MAP_X_DIM = 10000000;
	public final static int MAP_Y_DIM = 10000000;
	public final static int GRID_FACTOR = 2;
	public final static int vehicleCount = 750;
	public final static int numUrbanCenters = 9;
	public final static double lambda = 1.5;


	private Map map;
	private List<Vehicle> vehicles;
	private static List<Node> intersections = new ArrayList<Node>();
	
	private int lastComputedTimestep = 0;
	
	private ArrayList<HashMap<Vehicle,Coordinates>> history = new ArrayList<HashMap<Vehicle,Coordinates>>();
	
	public TrafficManager(Map map, List<Vehicle> vehicles) {
		this.map = map;
		this.vehicles = vehicles;
		
		ensureCapacity(lastComputedTimestep);
		
		for(Vehicle vehicle : vehicles) {
			history.get(lastComputedTimestep).put(vehicle, vehicle.getLocationCoordinates(lastComputedTimestep));
			
			// Validation of the path	
			List<Edge> edgePath = vehicle.getEdgePath();
			for(int i=0; i< edgePath.size()-1; i++)
				if(! edgePath.get(i+1).getFrom().equals(edgePath.get(i).getTo())) 
					throw new RuntimeException("EdgePath of vehicle " + this + " implies teleportation");
	
		}
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
				
			// Set speeds for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's new speed
				double newSpeed = vehicle.getDriverModel().determineNewSpeed(this,vehicle,lastComputedTimestep);
				
				// Set the new speed
				vehicle.setSpeed(lastComputedTimestep, newSpeed);

				/*
				 * Ask our pathfinding algorithm for a path - It can still return
				 * the very same path - we're only giving it the opportunity to
				 * change the path, not requiring it
				 */
				// vehicle.computePath(lastComputedTimestep);
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

	public static double manhattanDistance(Node a, Node b){
		return Math.abs((a.getY()-b.getY()) + (a.getX()-b.getX()));
	}

	public static double euclideanDistance(Node a, Node b){
		return Math.abs(Math.sqrt(Math.pow((a.getY()-b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}

	public static TrafficManager createTestFromFile() {
		MapReader mr = new MapReader();
		HashMap<String, Node> nodeMap = mr.readNodes();
		HashMap<String, Edge> edgeMap = mr.readEdges(nodeMap);
		mr.printAll(nodeMap,edgeMap);

        List<Node> nodeList = new ArrayList<Node>(nodeMap.values());
        // Sort the nodeList in descending order based on priority
        Collections.sort(nodeList, new SortNode());
        List<Edge> edgeList = new ArrayList<Edge>(edgeMap.values());

		Map map = new Map(nodeList,edgeList);

		List<Node> destinations = new ArrayList<Node>();


		// Make nodes that have multiple edges connecting to them destinations
		for (Node n : nodeList){
			if (n.getOutEdges().size() > 4){
				n.isDestination();
				destinations.add(n);
			}
			if (n.isIntersection()) intersections.add(n);
		}



        List cars = new ArrayList();
        for(int i = 0; i < vehicleCount; i++) {

        	// TODO: Make priorities based on neighborhood
//			createNeighborhoods(nodeList, numUrbanCenters);

			// This utilizes an exponential distribution prioritizing nodes at the start of the list
			// Nodes at the start of the list are higher priority than those at the end
			Random r = new Random();
            Node start = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));
            start.setHasCarAlready();
			Node end = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));
            while(start == end) { end = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));}
            while(start.hasCarAlready()) {start = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));}


            Car car = new Car(start,end,map);
			while (car.getEdgePath() == null){
				start = destinations.get((int)(Math.floor(r.nextDouble() * nodeList.size())));
				start.setHasCarAlready();
				while(start.hasCarAlready()) {start = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));}
				end = destinations.get((int)(Math.floor(r.nextDouble() * destinations.size())));
				car = new Car(start,end,map);
			}
            car.setDriverModel(new SimpleDriverModel(10));
            cars.add(i,car);
        }

		TrafficManager tm = new TrafficManager(map,cars);

		return tm;
	}

	public static double drawRandomExponential(double mean) {
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with given mean
		double res = (1 + (-mean * Math.log(u)));
		//System.out.println(res);
		return res;
	}


	public static TrafficManager createEnvironment() {

		List<Node> mapNodes = new ArrayList<Node>();
		List<Edge> mapEdges = new ArrayList<Edge>();
		List<Node> mapDestinations = new ArrayList<Node>();

		int nodeCount = 0;
		int edgeCount = 0;
		for (int i = 0; i < MAP_X_DIM; i++){
			for (int j = 0; j < MAP_Y_DIM; j++){
				if (i % (MAP_X_DIM/GRID_FACTOR) == 0 && j % (MAP_Y_DIM/GRID_FACTOR) == 0){
					Node n = new Node(i,j);
					n.makeDestination();
					mapNodes.add(n);
					System.out.println("Adding node at: (" + i + ", " + j + ")");
					nodeCount++;
					System.out.println("Nodes: " + nodeCount);
				}
			}
		}

		for (int i = 0; i < mapNodes.size(); i++){
			for (int j = 0; j < mapNodes.size(); j++){
				if ((euclideanDistance(mapNodes.get(i), mapNodes.get(j)) == (MAP_X_DIM/GRID_FACTOR) ||
						euclideanDistance(mapNodes.get(i), mapNodes.get(j)) == (MAP_Y_DIM/GRID_FACTOR)) && i != j)  {

					// This method doubles the edges for some reason, trying to figure out why.
					// Need to find out if mapEdges contains an edge between two points already.
					// Problem is, when doubling edges it makes an edge between node A and B, then again between
					// ... nodes B and A, which is an identical edge but cannot be easily compared.

					System.out.println("Adding Edge between: (" + mapNodes.get(i).getLocation().toString() + ", " + mapNodes.get(j).getLocation().toString() + ")");
					mapEdges.add(new Edge(mapNodes.get(i), mapNodes.get(j)));
					edgeCount++;
					System.out.println("Edges: " + edgeCount);

				}
			}
		}
		List<Vehicle> cars = new ArrayList<Vehicle>();
		Map map = new Map(mapNodes, mapEdges);


		for (int i = 0; i < vehicleCount; i++){
			int x = (int)(Math.random()*mapNodes.size());
			int y = (int)(Math.random()*mapNodes.size());

			if (mapNodes.get(y).isDestination()){
				Car temp = new Car(mapNodes.get(x), mapNodes.get(y), map);
				cars.add(temp);
				temp.setDriverModel(new IntelligentDriverModel());
			}

			if (!mapNodes.get(y).isDestination()){
				y = (int)(Math.random()*mapNodes.size());
			}



		}


		TrafficManager tm = new TrafficManager(map,cars);

		int y = 0;

		// createNeighborhoods(mapNodes, numUrbanCenters);
		
		return tm;
		
	}


	/**
	 * The idea is to create priority neighborhoods such as urban/suburban centers that cars are more likely to head
	 * towards. However, since intersections have multiple nodes, we need to redefine several parameters. First of
	 * all, we need to classify what a "destination" node is, such that a car does not decide to choose the middle of
	 * an intersection as his "destination." This is necessary because we have to define priority to a specific node
	 * (as a car chooses only a node as his destination) so only destination nodes can receive priority.
	 * @param nodes - Map nodes
	 * @param numUrbanCenters - the number of Urban Centers designed for this map
	 */
	private static void createNeighborhoods(List<Node> nodes, int numUrbanCenters){
		int centers = numUrbanCenters;

		if (centers == 0) return;
		for (Node n : nodes){
			if (n.isDestination() && Math.random() < .1 && centers > 0){
//				n.setNodePriorityWeight(uCenterWeight);
				for (Node i : n.getOutgoingNeighbors()){
					i.setNodePriorityWeight(n.getNodePriorityWeight()-1);
				}
				for (Node j : n.getIncomingNeighbors()){
					j.setNodePriorityWeight(n.getNodePriorityWeight()-1);
				}
				centers--;
			}
		}

	}

	
	public String toString() {
		return "[TrafficManager]";
	}

	public static void main(String[] args) {
		//TrafficManager tm1 = createSimpleTestcase();
		
		//tm1.simulate(TIMESTEPS);
		
		//System.out.println("Simple test case simulation completed");
		
		//TrafficManager tm2 = createEnvironment();
		
		//tm2.simulate(TIMESTEPS);

		//System.out.println("Andrew-generated test case simulation completed");

		TrafficManager tm3 = createTestFromFile();

		tm3.simulate(TIMESTEPS);
		
		int x =0;
	}

}
