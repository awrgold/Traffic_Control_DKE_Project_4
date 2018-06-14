package com.mygdx.sim.GameObjects;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.data.DistanceAndVehicle;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.data.SortNode;
import com.mygdx.sim.GameObjects.data.Util;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModel;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObjectState;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Car;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class TrafficManager {

	// Turn on all print statements
	private static boolean DEBUG = true;

	// Duration of the simulation (hours, minutes, seconds)
	private final static Time DURATION = new Time(1, 0, 0);

	// Sampling frequency. Larger number means higher fidelity of the model, but
	// also more computation
	public final static int TIMESTEPS_PER_SECOND = 1;

	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 1000;

	// Temporary map bounds
	public final static int MAP_X_DIM = 100000;
	public final static int MAP_Y_DIM = 100000;
	public final static int GRID_FACTOR = 2;
	public final static int vehicleCount = 1000;
	public final static int numUrbanCenters = 5;
	public final static double mean = 0.2;
	public final static double lambda = 1.0;

	private Map map;
	private List<TrafficObject> trafficObjects;
	private List<Vehicle> vehicles;
	private static List<Node> intersections = new ArrayList<Node>();
	private static int urbanCenterWeight = 3;

	private int lastComputedTimestep = 0;
	private static double aggressionRandomizer;

	public TrafficManager(Map map, List<Vehicle> vehicles) {
		this.map = map;
		this.vehicles = vehicles;

		this.trafficObjects = new ArrayList<TrafficObject>(vehicles);

		for (Vehicle vehicle : vehicles) {

			// Validation of the path
			List<Edge> edgePath = vehicle.getEdgePath();
			if(edgePath == null) {
				continue;
			}
			
			for (int i = 0; i < edgePath.size() - 1; i++)
				if (!edgePath.get(i + 1).getFrom().equals(edgePath.get(i).getTo()))
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
	 * Gets the state (location+visibility) of all traffic objects at the given
	 * timestep.
	 * 
	 * @param timestep
	 *            - timestep for which you need the state
	 * @return a HashMap that maps TrafficObjects to their State
	 */
	public HashMap<TrafficObject, TrafficObjectState> getState(int timestep) {
		HashMap<TrafficObject, TrafficObjectState> state = new HashMap<TrafficObject, TrafficObjectState>();

		for (TrafficObject to : trafficObjects)
			state.put(to, to.getState(timestep));

		return state;
	}

	/**
	 * Run the simulation until the given timestep.
	 * 
	 * @param finalTimeStep
	 *            - timestep until which we are running the sim
	 */
	public void simulate(int finalTimeStep) {

		finalTimeStep++;

		// Ensure the map's location cache has enough memory capacity
		map.ensureCapacity(finalTimeStep);
		System.out.println("map");

		// Ensure all Vehicles have enough memory capacity
		for (Vehicle vehicle : vehicles)
		vehicle.ensureCapacity(finalTimeStep);
		System.out.println("vehicles");

		while (lastComputedTimestep < finalTimeStep - 1) {
			if (lastComputedTimestep % 100 == 0)
				System.out.println(lastComputedTimestep);

			for (Vehicle vehicle : vehicles)
				map.getLocationCache().get(vehicle.getEdgeAt(lastComputedTimestep)).get(lastComputedTimestep).add(vehicle);

			// Set accelerations for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's acceleration
				double acceleration = vehicle.getDriverModel().determineAcceleration(this, vehicle,
						lastComputedTimestep);

				// Set the acceleration
				vehicle.accelerate(lastComputedTimestep, acceleration);

				// (OPTIONAL) update the path for our vehicle based on the current location. Requires updated A* heuristics
				// vehicle.computePath(lastComputedTimestep);
			}

			// Increment the timestep for lights
			for (Node n : map.getIntersections()) {
				if (n.getLights() == null) {
					continue;
				}
				for (int i = 0; i < n.getLights().size(); i++) {
					n.getLights().get(i).incrementTimeStep();
				}
			}

			lastComputedTimestep++;

			// Have the vehicles update their locations for the next timestep
			for (Vehicle vehicle : vehicles)
				vehicle.move(lastComputedTimestep);
		}
	}

	/**
	 * Gets the distance to and speed of the closest vehicle in front of this
	 * vehicle.
	 * 
	 * @param vehicle
	 *            - vehicle in front of which we are checking
	 * @param timestep
	 *            - timestep at which we are checking
	 * @return distance and speed of closest vehicle
	 */
	public DistanceAndSpeed getDistanceAndSpeedToClosestVehicle(Vehicle vehicle, int timestep) {
		Edge edge = vehicle.getEdgeAt(timestep);
		double distance = -vehicle.getTraveledDistance(timestep);

		DistanceAndVehicle dnv = getClosestVehicle(vehicle, edge, distance, timestep);

		if (dnv == null)
			return new DistanceAndSpeed(VIEW_DISTANCE, RIDICULOUS_SPEED);

		Vehicle closest = dnv.getVehicle();
		double distanceToClosest = dnv.getDistance();

		double speedOfClosest = RIDICULOUS_SPEED;

		if (timestep > 0)
			speedOfClosest = closest.getSpeedAt(timestep - 1);

		return new DistanceAndSpeed(distanceToClosest, speedOfClosest);
	}

	private DistanceAndVehicle getClosestVehicle(Vehicle vehicle, Edge currentEdge, double distanceUntilNow,
			int timestep) {
		List<Vehicle> vehiclesOnCurrentEdge = (List<Vehicle>) map.getLocationCache().get(currentEdge).get(timestep)
				.clone();

		ArrayList<DistanceAndVehicle> candidates = new ArrayList<DistanceAndVehicle>();
		for (Vehicle vehicle2 : vehiclesOnCurrentEdge) {
			double distance = distanceUntilNow + vehicle2.getTraveledDistance(timestep);
			if (distance - Util.DELTA_EPSILON > 0 && vehicle2.isVisibleToDrivers(timestep))
				candidates.add(new DistanceAndVehicle(distance, vehicle2));
		}

		DistanceAndVehicle closest = getClosestDistanceAndVehicleFromList(candidates, timestep);

		if (closest != null) {
			Vehicle closestVehicle = closest.getVehicle();
			double distanceToClosest = (distanceUntilNow + closestVehicle.getTraveledDistance(timestep));
			return new DistanceAndVehicle(distanceToClosest, closestVehicle);
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

	public static double manhattanDistance(Node a, Node b) {
		return Math.abs((a.getY() - b.getY()) + (a.getX() - b.getX()));
	}

	public static double euclideanDistance(Node a, Node b) {
		return Math.abs(Math.sqrt(Math.pow((a.getY() - b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}

	public static TrafficManager createTestFromFile() {
		MapReader mr = new MapReader();
		HashMap<String, Node> nodeMap = mr.readNodes();
		HashMap<String, Edge> edgeMap = mr.readEdges(nodeMap);
		mr.printAll(nodeMap, edgeMap);

		List<Node> nodeList = new ArrayList<Node>(nodeMap.values());

		// Sort the nodeList in descending order based on priority
		Collections.sort(nodeList, new SortNode());
		List<Edge> edgeList = new ArrayList<Edge>(edgeMap.values());
		Map map = new Map(nodeList, edgeList);

		List<Node> destinations = new ArrayList<Node>();

		// Make small side roads the only place cars can spawn
		for (Node n : nodeList){
			int minLanes = Integer.MIN_VALUE;
			for (Edge e : n.getInEdges()){
				if (e.getNumLanes() > minLanes){
					minLanes = e.getNumLanes();
				}
			}
			if (minLanes == 1){
				n.makeDestination();
				destinations.add(n);
			}
		}

		if(DEBUG){
			System.out.println("Destination list size: " + destinations.size());
		}

		List cars = new ArrayList();

		// Create the neighborhood scheme (see methods below)
		if (DEBUG){
			System.out.println("Creating neighborhoods with " + destinations.size() + " destinations, and " + numUrbanCenters + " urban centers.");
		}
		createNeighborhoods(destinations, numUrbanCenters);

		for (int i = 0; i < vehicleCount; i++) {

			// This utilizes an exponential distribution prioritizing nodes at the start of the list
			// Nodes at the start of the list are higher priority than those at the end
			Collections.sort(destinations, new SortNode());
			Node start = destinations.get((int) (Math.floor(drawRandomExponential(lambda) * destinations.size())));
			Node end = destinations.get((int) (Math.floor(drawRandomExponential(lambda) * destinations.size())));
			while (start == end) {
				end = destinations.get((int) (Math.floor(drawRandomExponential(lambda) * destinations.size())));
			}
			// Build each car with their given destination and a randomly chosen spawn time
			int r = (int)(Math.round(Math.random()*getMaximumTimesteps()));
			Car car = new Car.Builder(start, end, map).setStartTimestep(r).build();

			while(car.getEdgePath() == null) {
				start = destinations.get((int) (Math.floor(drawRandomExponential(lambda) * destinations.size())));
				end = destinations.get((int) (Math.floor(drawRandomExponential(lambda) * destinations.size())));
				car = new Car.Builder(start, end, map).build();
			}

			// Set the driver's model
			car.setDriverModel(new IntelligentDriverModel());
			cars.add(i, car);
		}

		TrafficManager tm = new TrafficManager(map, cars);
		return tm;
	}

	/**
	 * Draws a random variate from an (negative) exponential distribution with given rate
	 * @param rate - lambda
	 * @return
	 */
	public static double drawRandomExponential(double rate) {
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate, truncated to [0,1]
		double x = -Math.log(1 - (1 - Math.pow(Math.E, -rate)) * u) / rate;
		return x;
	}

	/**
	 * Procedurally generates a grid based on static parameters, unused as of phase 2
	 * @return
	 */
	public static TrafficManager createEnvironment() {

		List<Node> mapNodes = new ArrayList<Node>();
		List<Edge> mapEdges = new ArrayList<Edge>();
		List<Node> mapDestinations = new ArrayList<Node>();

		int nodeCount = 0;
		int edgeCount = 0;
		for (int i = 0; i < MAP_X_DIM; i++) {
			for (int j = 0; j < MAP_Y_DIM; j++) {
				if (i % (MAP_X_DIM / GRID_FACTOR) == 0 && j % (MAP_Y_DIM / GRID_FACTOR) == 0) {
					Node n = new Node(i, j);
					n.makeDestination();
					mapNodes.add(n);
					System.out.println("Adding node at: (" + i + ", " + j + ")");
					nodeCount++;
					System.out.println("Nodes: " + nodeCount);
				}
			}
		}

		for (int i = 0; i < mapNodes.size(); i++) {
			for (int j = 0; j < mapNodes.size(); j++) {
				if ((euclideanDistance(mapNodes.get(i), mapNodes.get(j)) == (MAP_X_DIM / GRID_FACTOR)
						|| euclideanDistance(mapNodes.get(i), mapNodes.get(j)) == (MAP_Y_DIM / GRID_FACTOR))
						&& i != j) {
					System.out.println("Adding Edge between: (" + mapNodes.get(i).getLocation().toString() + ", "
							+ mapNodes.get(j).getLocation().toString() + ")");
					mapEdges.add(new Edge(mapNodes.get(i), mapNodes.get(j)));
					edgeCount++;
					System.out.println("Edges: " + edgeCount);

				}
			}
		}
		List<Vehicle> cars = new ArrayList<Vehicle>();
		Map map = new Map(mapNodes, mapEdges);

		for (int i = 0; i < vehicleCount; i++) {

			int x = 0;
			int y = 0;
			while (x == y) {
				x = (int) (Math.random() * mapNodes.size());
				y = (int) (Math.random() * mapNodes.size());
			}

			if (mapNodes.get(y).isDestination()) {
				Car temp = new Car.Builder(mapNodes.get(x), mapNodes.get(y), map).build();
				cars.add(temp);
				temp.setDriverModel(new IntelligentDriverModel());
			}

		}

		TrafficManager tm = new TrafficManager(map, cars);

		int y = 0;

		// createNeighborhoods(mapNodes, numUrbanCenters);

		return tm;

	}

	/**
	 * The idea is to create priority neighborhoods such as urban/suburban centers
	 * that cars are more likely to head towards.
	 * @param nodes - Map nodes
	 * @param numUrbanCenters - the number of Urban Centers designed for this map
	 */
	private static void createNeighborhoods(List<Node> nodes, int numUrbanCenters) {

		// Keep track of how many centers are left to define
		if (numUrbanCenters == 0) return;

		double d = Math.random();
		while (d >= 0.1){
			d = Math.random();
		}

		int r = (int) d*nodes.size();
		// randomly choose a node to set as an urban center, and update the weights involved
		nodes.get(r).setNodePriorityWeight(setRandomUrbanCenterWeight(urbanCenterWeight));
		setNeighborWeights(nodes.get(r), urbanCenterWeight-1);
		createNeighborhoods(nodes, numUrbanCenters-1);
		if (DEBUG){
			System.out.println("Remaining urban centers to work with: " + (numUrbanCenters - 1));
		}
	}

	/**
	 * Recursively sets the weights of all neighbors of urban centers down to 0
	 * @param node - the node we're working with
	 * @param weight - the current weight of the iteration
	 */
	public static void setNeighborWeights(Node node, int weight){
		if (weight <= 0) return;
		for (Node n : node.getNeighbors()){
			// if a node doesn't already have an assigned weight (preventing double decrements)
			if (!n.isHasWeight()){
				n.setNodePriorityWeight(weight);
				if(DEBUG){
					System.out.println("Weight of node " + n.getNodeID() + " = " + weight);
				}
				setNeighborWeights(n, weight-1);
			}
		}
	}

	/**
	 * Randomly (Gaussian) choose a weight for each urban center around the mean
	 * @param mean - defined in class
	 * @return
	 */
	public static int setRandomUrbanCenterWeight(int mean){
		Random r = new Random();
		double g = r.nextGaussian() + mean;
		if (g < 0 || g > mean*2) {
			setRandomUrbanCenterWeight(mean);
		}
		System.out.println("Urban center weight: " + (int)g);
		return (int)g;
	}

	public static double getDurationOfTimestepInSeconds() {
		return 1. / TIMESTEPS_PER_SECOND;
	}

	public static int getMaximumTimesteps() {
		return TIMESTEPS_PER_SECOND * (DURATION.getSeconds() + 60 * (DURATION.getMinutes() + 60 * DURATION.getHours()));
	}

	public static Time getTimeAtTimestep(int timestep) {
		int totalSeconds = timestep / TIMESTEPS_PER_SECOND;

		int seconds = totalSeconds % 60;

		totalSeconds -= seconds;
		totalSeconds /= 60;

		int minutes = totalSeconds % 60;

		totalSeconds -= minutes;
		totalSeconds /= 60;

		int hours = totalSeconds;

		return new Time(hours, minutes, seconds);
	}

	public String toString() {
		return "[TrafficManager]";
	}

	public static TrafficManager testcase1() {
		Node node1 = new Node(0, 0);
		Node node2 = new Node(475, 0);
		Node node3 = new Node(475, 1000);
		Edge edge1 = new Edge(node1, node2);
		Edge edge2 = new Edge(node2, node3);

		Map map = new Map(Arrays.asList(node1, node2, node3), Arrays.asList(edge1, edge2));

		Car car1 = new Car.Builder(node2, node3, map).build();
		car1.setEdgePath(Arrays.asList(edge2));
		car1.setDriverModel(new SimpleDriverModel(10));

		Car car2 = new Car.Builder(node2, node3, map).build();
		car2.setEdgePath(Arrays.asList(edge2));
		car2.setDriverModel(new IntelligentDriverModel());

		Car car3 = new Car.Builder(node1, node3, map).build();
		car3.setEdgePath(Arrays.asList(edge1, edge2));
		car3.setDriverModel(new IntelligentDriverModel());

		List cars = Arrays.asList(car1, car2, car3);

		return new TrafficManager(map, cars);
	}

	public static TrafficManager testcaseBig(int nodeN, int carsN) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vehicle> cars = new ArrayList<Vehicle>();

		for (int i = 0; i < nodeN; i++)
			nodes.add(new Node(0, i * 100));

		for (int i = 0; i < nodeN - 1; i++)
			edges.add(new Edge(nodes.get(i), nodes.get(i + 1)));

		Map map = new Map(nodes, edges);

		for (int i = 0; i < carsN; i++) {
			Car car = new Car.Builder(nodes.get(0), nodes.get(nodeN - 1), map).build();
			car.setDriverModel(new SimpleDriverModel());

			car.setEdgePath(edges);

			cars.add(car);
		}

		return new TrafficManager(map, cars);
	}

	public static void main(String[] args) {
		TrafficManager tm = testcaseBig(1000, 1000);

		System.out.println("Created test case");

		tm.simulate(getMaximumTimesteps());

		System.out.println("Done with " + tm.lastComputedTimestep + " timesteps.");

		System.out.println(getTimeAtTimestep(tm.lastComputedTimestep));
	}
}
