package com.mygdx.sim.GameObjects;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.mygdx.sim.GameObjects.Controllers.ControlScheme;
import com.mygdx.sim.GameObjects.Controllers.LightController;
import com.mygdx.sim.GameObjects.data.DistanceAndSpeed;
import com.mygdx.sim.GameObjects.data.DistanceAndTrafficObject;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.data.SortNode;
import com.mygdx.sim.GameObjects.data.Util;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModel;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModelPlus;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.trafficObject.InvisibleCar;
import com.mygdx.sim.GameObjects.trafficObject.TestTrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObjectState;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Car;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;
<<<<<<< HEAD
=======
import com.mygdx.sim.World.Components.TrafficLight;
import javafx.scene.effect.Light;
import javafx.scene.paint.Stop;
>>>>>>> branch 'master' of https://github.com/awrgold/Project_2.2.git

public class TrafficManager {

	// Turn on all print statements
	private static boolean DEBUG = false;
	private static boolean DEBUG2 = false;

	// Duration of the simulation (hours, minutes, seconds)
	private final static Time DURATION = new Time(1, 0, 0);

	// Sampling frequency. Larger number means higher fidelity of the model, but
	// also more computation. !!!DO NOT CHANGE FROM 1!!!
	public final static int TIMESTEPS_PER_SECOND = 1;
	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 100;

	// Temporary map bounds
	public final static int MAP_X_DIM = 100000;
	public final static int MAP_Y_DIM = 100000;
	public final static int GRID_FACTOR = 2;
	public final static int vehicleCount = 1000;
	public final static int numUrbanCenters = 5;
	// Mean IA time of 4 seconds with 1 hour simulation is an average of 900 cars in
	// a 1 hour simulation
	public final static Time meanInterarrivalTime = new Time(0, 0, 4);
	// Mean 3.6 in a 1 hour simulation is an average of 1000 cars
	public final static double mean = 3.6;
	public static List<LightController> controllers = new ArrayList<LightController>();


	private Map map;
	private List<TrafficObject> trafficObjects;
	private List<Vehicle> vehicles;
	private static int urbanCenterWeight = 10;
	private static ControlScheme scheme = ControlScheme.BASIC;
	private int lastComputedTimestep = 0;

	/**
	 * --- Statistical parameters ---
	 * avgDriverSpeed: average speed for driver "i" on the map
	 * speedLimit: Speed limit for the map
	 * expectedTravelTimes: expected travel time for driver "i" on map (based on speed limit and distance required to travel)
	 * actualTravelTimes: actual travel time for driver "i" on map (based on endtime-starttime)
	 * numVehiclesReachedGoal: (DO WE NEED?) number of vehicles that were able to reach the goal in time
	 * numTimesSlowed: number of times vehicle "i" slowed down from their top speed during the sim
	 */
	public static List<Integer> avgDriverSpeed = new ArrayList<Integer>();
	public static int speedLimit = 0;
	public static List<Integer> expectedTravelTimes = new ArrayList<Integer>();
	public static List<Integer> actualTravelTimes = new ArrayList<Integer>();
	public static List<Vehicle> vehiclesReachedGoal = new ArrayList<Vehicle>();
	public static List<Integer> numTimesSlowed = new ArrayList<Integer>();

	public static List<Integer> getActualTravelTimes() {
		return actualTravelTimes;
	}

	public static List<Integer> getAvgDriverSpeed() {
		return avgDriverSpeed;
	}

	public static List<Integer> getNumTimesSlowed() {
		return numTimesSlowed;
	}

	public static List<Vehicle> getVehiclesReachedGoal() {
		return vehiclesReachedGoal;
	}

	public static int getSpeedLimit() {
		return speedLimit;
	}

	public static List<Integer> getExpectedTravelTimes() {
		return expectedTravelTimes;
	}

	

	public TrafficManager(Map map, List<Vehicle> vehicles, List<TrafficObject> trafficObjects, List<LightController> controllers) {
		this.map = map;
		this.vehicles = vehicles;
		this.controllers = controllers;

		this.trafficObjects = trafficObjects;

		for (Vehicle vehicle : vehicles) {

			// Validation of the path
			// List<Edge> edgePath = vehicle.getEdgePath();
			// if (edgePath == null) {
			// continue;
			// }
			//
			// for (int i = 0; i < edgePath.size() - 1; i++)
			// if (!edgePath.get(i + 1).getFrom().equals(edgePath.get(i).getTo()))
			// throw new RuntimeException("EdgePath of vehicle " + this + " implies
			// teleportation");

		}

		for (TrafficObject to : trafficObjects) {
			Edge edge = to.getEdge(0);
			map.getStaticTrafficObjectsCache().get(edge).add(to);
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

		for (Vehicle veh : vehicles)
			state.put(veh, veh.getState(timestep));

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

		// Ensure all InvisibleCars have enough memory capacity
		for (LightController l : controllers) {
			for (Stoplight stopLight : l.getLights()) {
				stopLight.getInvisibleCar().ensureCapacity(finalTimeStep);
			}
		}
		System.out.println("invisibleCars");

		while (lastComputedTimestep < finalTimeStep - 1) {
			if (lastComputedTimestep % 100 == 0)
				System.out.println(lastComputedTimestep);

			for (LightController l : controllers) {
				l.update(lastComputedTimestep);

				for (Stoplight stopLight : l.getLights()) {
					if (stopLight.getLightState() == LightState.GREEN) {
						stopLight.getInvisibleCar().setIsVisibleToDrivers(lastComputedTimestep, false);
					} else {
						stopLight.getInvisibleCar().setIsVisibleToDrivers(lastComputedTimestep, true);
					}
				}
			}

			for (Vehicle vehicle : vehicles)
				map.getLocationCache().get(vehicle.getEdge(lastComputedTimestep)).get(lastComputedTimestep)
						.add(vehicle);

			// Set accelerations for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's acceleration
				double acceleration = vehicle.getDriverModel().determineAcceleration(this, vehicle,
						lastComputedTimestep);

				// Set the acceleration
				vehicle.accelerate(lastComputedTimestep, acceleration);

				// (OPTIONAL) update the path for our vehicle based on the current location.
				// Requires updated A* heuristics
				// vehicle.computePath(lastComputedTimestep);
			}

			lastComputedTimestep++;

<<<<<<< HEAD
			// lightController.update(lastComputedTimestep);
=======
			// Update each light controller
//			for (LightController l : controllers){
//				l.update(lastComputedTimestep);
//			}
>>>>>>> branch 'master' of https://github.com/awrgold/Project_2.2.git

			// Have the vehicles update their locations for the next timestep
			for (Vehicle vehicle : vehicles)
				vehicle.move(lastComputedTimestep);
		}

		for (Vehicle v : vehicles){
			int a = 0;
			for (int i = 0; i < v.getSpeeds().length; i++) {
				a += v.getSpeeds()[i];
			}
			a = (a/v.getSpeeds().length);
			avgDriverSpeed.add(a);
		}



	}

	public List<LightController> getLightControllers() {
		return controllers;
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
	public DistanceAndSpeed getDistanceAndSpeedToClosestTrafficObject(Vehicle vehicle, int timestep) {
		Edge edge = vehicle.getEdge(timestep);
		double distance = -vehicle.getTraveledDistance(timestep);

		DistanceAndTrafficObject dnv = getClosestVehicle(vehicle, edge, distance, timestep);

		if (dnv == null)
			return new DistanceAndSpeed(VIEW_DISTANCE, RIDICULOUS_SPEED);

		TrafficObject closest = dnv.getTrafficObject();
		double distanceToClosest = dnv.getDistance();

		double speedOfClosest = RIDICULOUS_SPEED;

		if (timestep > 0)
			speedOfClosest = closest.getSpeed(timestep - 1);

		return new DistanceAndSpeed(distanceToClosest, speedOfClosest);
	}

	private DistanceAndTrafficObject getClosestVehicle(Vehicle vehicle, Edge currentEdge, double distanceUntilNow,
			int timestep) {
		List<Vehicle> vehiclesOnCurrentEdge = map.getLocationCache().get(currentEdge).get(timestep);
		List<TrafficObject> staticTrafficObjectsOnCurrentEdge = map.getStaticTrafficObjectsCache().get(currentEdge);

		ArrayList<TrafficObject> trafficObjectsOnCurrentEdge = new ArrayList<TrafficObject>();
		trafficObjectsOnCurrentEdge.addAll(staticTrafficObjectsOnCurrentEdge);
		trafficObjectsOnCurrentEdge.addAll(vehiclesOnCurrentEdge);

		ArrayList<DistanceAndTrafficObject> candidates = new ArrayList<DistanceAndTrafficObject>();
		for (TrafficObject to : trafficObjectsOnCurrentEdge) {
			double distance = distanceUntilNow + to.getDistanceOnEdge(timestep);
			if (distance - Util.DELTA_EPSILON > 0 && to.isVisibleToDrivers(timestep))
				candidates.add(new DistanceAndTrafficObject(distance, to));
		}

		DistanceAndTrafficObject closest = getClosestDistanceAndTrafficObjectFromList(candidates, timestep);

		if (closest != null) {
			TrafficObject closestVehicle = closest.getTrafficObject();
			double distanceToClosest = (distanceUntilNow + closestVehicle.getDistanceOnEdge(timestep));
			return new DistanceAndTrafficObject(distanceToClosest, closestVehicle);
		}

		distanceUntilNow += currentEdge.getLength();

		if ((distanceUntilNow + Util.DELTA_EPSILON) >= VIEW_DISTANCE)
			return null;

		ArrayList<DistanceAndTrafficObject> vehiclesFromFollowingEdges = new ArrayList<DistanceAndTrafficObject>();

		for (Edge edge2 : currentEdge.getTo().getOutEdges())
			vehiclesFromFollowingEdges.add(getClosestVehicle(vehicle, edge2, distanceUntilNow, timestep));

		return getClosestDistanceAndTrafficObjectFromList(vehiclesFromFollowingEdges, timestep);
	}

	private DistanceAndTrafficObject getClosestDistanceAndTrafficObjectFromList(List<DistanceAndTrafficObject> list, int timestep) {

		TrafficObject closestTrafficObject = null;
		double smallestDistance = VIEW_DISTANCE;

		for (DistanceAndTrafficObject dnv : list) {
			if (dnv == null)
				continue;

			TrafficObject vehicle2 = dnv.getTrafficObject();

			double thisDistance = dnv.getDistance();

			if (thisDistance < smallestDistance) {
				closestTrafficObject = vehicle2;
				smallestDistance = thisDistance;
			}
		}
		if (closestTrafficObject == null)
			return null;

		return new DistanceAndTrafficObject(smallestDistance, closestTrafficObject);
	}

	private static double euclideanDistance(Node a, Node b) {
		return Math.abs(Math.sqrt(Math.pow((a.getY() - b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}

	public static TrafficManager createSimFromFile() {
		MapReader mr = new MapReader();
		mr.readMap();
		HashMap<String, Node> nodeMap = mr.getNodes();
		HashMap<String, Edge> edgeMap = mr.getEdges();

		mr.printAll(nodeMap, edgeMap);

		List<Node> nodeList = new ArrayList<Node>(nodeMap.values());

		// Sort the nodeList in descending order based on priority
		Collections.sort(nodeList, new SortNode());
		List<Edge> edgeList = new ArrayList<Edge>(edgeMap.values());
		Map map = new Map(nodeList, edgeList);

		List<Node> spawns = map.getSpawnPoints();
		System.out.println("!!!!!!!!!!!!!SIZE OF SPAWN POINTS!!!!!!!!!!" + spawns.size());
		for (Node n : spawns){
			if (n.getXmlID().contains("south")){
				System.out.println("FOUND A NORTH NODE BITCH");
			}
		}
		List<Node> destinations = map.getDestinations();
		List cars = createCars(spawns, destinations, map);

		// createNeighborhoods(destinations, numUrbanCenters);

		if (DEBUG) {
			System.out.println("Destination list size: " + destinations.size());
			System.out.println("Creating neighborhoods with " + destinations.size() + " destinations, and " + numUrbanCenters + " urban centers.");
		}

		// Light Controller

		initiateStoplights(map);

		// Static Traffic Objects
		List<TrafficObject> staticTrafficObjects = new ArrayList<TrafficObject>();
		for (LightController l : controllers) {
			for (Stoplight stopLight : l.getLights()) {
				InvisibleCar invisibleCar = new InvisibleCar(stopLight.getParent().getOutEdges().get(0));
				stopLight.setInvisibleCar(invisibleCar);
				staticTrafficObjects.add(invisibleCar);

			}
		}

		// Traffic Manager
		TrafficManager tm = new TrafficManager(map, cars, staticTrafficObjects, controllers);
		return tm;
	}

	public static TrafficManager createTestFromFile(){
		MapReader mr = new MapReader();
		mr.readMap();
		HashMap<String, Node> nodeMap = mr.getNodes();
		HashMap<String, Edge> edgeMap = mr.getEdges();

		mr.printAll(nodeMap, edgeMap);

		List<Node> nodeList = new ArrayList<Node>(nodeMap.values());

		// Sort the nodeList in descending order based on priority
		Collections.sort(nodeList, new SortNode());
		List<Edge> edgeList = new ArrayList<Edge>(edgeMap.values());
		Map map = new Map(nodeList, edgeList);

		List<Node> spawns = map.getSpawnPoints();
		System.out.println("!!!!!!!!!!!!!SIZE OF SPAWN POINTS!!!!!!!!!!" + spawns.size());
		for (Node n : spawns){
			if (n.getXmlID().contains("south")){
				System.out.println("FOUND A NORTH NODE BITCH");
			}
		}
		List<Node> destinations = map.getDestinations();
		List cars = createCars(spawns, destinations, map);


		// createNeighborhoods(destinations, numUrbanCenters);

		if(DEBUG){
			System.out.println("Destination list size: " + destinations.size());
			System.out.println("Creating neighborhoods with " + destinations.size() + " destinations, and " + numUrbanCenters + " urban centers.");
		}

		// Light Controller

		initiateStoplights(map);

		// Static Traffic Objects
		List<TrafficObject> staticTrafficObjects = new ArrayList<TrafficObject>();
		for (LightController l : controllers){
			for(Stoplight stopLight : l.getLights()) {
				staticTrafficObjects.add(new InvisibleCar(stopLight.getParent().getOutEdges().get(0)));
			}
		}

		// Traffic Manager
		TrafficManager tm = new TrafficManager(map, cars, staticTrafficObjects, controllers);
		return tm;
	}

	/**
	 * Create spawn/despawn points for vehicles in the simulation
	 * 
	 * @param map:
	 *            The current map of the sim
	 * @return: the list of points vehicles may spawn at
	 */
	private static ArrayList<Node> createSpawnPoints(Map map) {

		ArrayList<Node> spawnPoints = new ArrayList<Node>();

		// Make small side roads the only place cars can spawn
		for (Node n : map.getNodes()) {
			int minLanes = Integer.MIN_VALUE;
			for (Edge e : n.getInEdges()) {
				if (e.getNumLanes() > minLanes) {
					minLanes = e.getNumLanes();
				}
			}
			if (minLanes == 1) {
				n.makeDestination();
				spawnPoints.add(n);
			}
		}
		return spawnPoints;
	}

	/**
	 * Create all the vehicles for the simulation
	 * 
	 * @param destinations:
	 *            the list of vehicles cars can spawn at
	 * @param map:
	 *            the map of the sim
	 * @return the list of vehicles in the sim
	 */
	private static List createCars(List<Node> spawns, List<Node> destinations, Map map) {
		List cars = new ArrayList();

		int previousArrivalTime = 0;
		while (previousArrivalTime < getMaximumTimesteps()) {

			// Collections.sort(destinations, new SortNode());

			Node start = spawns.get((int) (Math.floor(Math.random() * spawns.size())));
			Node end = destinations.get((int) (Math.floor(Math.random() * destinations.size())));

			// Ensures that cars coming from a direction can't have the same nodes as a goal
			while (start.getXmlID().charAt(0) == end.getXmlID().charAt(0)) {
				end = destinations.get((int) (Math.floor(Math.random() * destinations.size())));
			}

			System.out.println("Start: " + start.getXmlID() + " End Goal: " + end.getXmlID());

			/**
			 * Generate cars StartTimeStep: when the car spawns Need to give it a timestep
			 * based on a rush-hour based model
			 */

			// System.out.println("!!! Next arrival time: " + previousArrivalTime);

			// Build each car with their given destination via a Poisson arrival process
			previousArrivalTime = generateNextArrivalTime(previousArrivalTime);
			int r = (int) (Math.round(Math.random() * getMaximumTimesteps()));
			Car car = new Car.Builder(start, end, map).setStartTimestep(previousArrivalTime).setDriverModel(new IntelligentDriverModelPlus()).build();

			if (DEBUG) {
				System.out.println("Next arrival time: " + previousArrivalTime);
			}

			while (start.getSpawntimes().contains(r)) {
				r = (int) (Math.round(Math.random() * getMaximumTimesteps()));
				car = new Car.Builder(start, end, map).setStartTimestep(generateNextArrivalTime(previousArrivalTime)).setDriverModel(new IntelligentDriverModelPlus()).build();
			}

			start.getSpawntimes().add(r);

			cars.add(car);
			// car.setTimeLimit(determineTimeLimit(car));

		}

		return cars;
	}

	public static int generateNextArrivalTime(int currentTime) {

		double u = Math.random();
		int d = (int) Math.round(-mean * Math.log(u));
		int res = currentTime + d;

		return res;
	}

	// TODO: Make this robust - right now just arbitrarily calculating a time limit,
	// not based on something realistic
	// private static int determineTimeLimit(Vehicle car){
	// double mDist = manhattanDistance(car.getStartNode(), car.getGoalNode());
	// List<Edge> path = car.getEdgePath();
	// double actualDist = 0;
	// double avgSpeedOnPath = 0;
	// for (Edge e : path){
	// actualDist += e.getLength();
	// avgSpeedOnPath += e.getSpeedLimit();
	// }
	// avgSpeedOnPath = (avgSpeedOnPath/path.size());
	// double differential = Math.abs(actualDist/mDist);
	// int timeLimit = (int)Math.round(actualDist/((avgSpeedOnPath*1000)/60));
	//
	// if(DEBUG){
	// System.out.println("Determined time limit for car " + car.toString() + " in
	// [" + timeLimit + "] timesteps.");
	// System.out.println("Actual time taken for car + " + car.toString() + " in ["
	// + car.getEndTimestep() + "] timesteps.");
	// }
	// return timeLimit;
	// }

	/**
	 * The idea is to create priority neighborhoods such as urban/suburban centers
	 * that cars are more likely to spawn at and head towards. TODO: Update this
	 * with more specific locations. Too random right now.
	 * 
	 * @param nodes
	 *            - Map nodes
	 * @param numUrbanCenters
	 *            - the number of Urban Centers designed for this map
	 */
	private static void createNeighborhoods(List<Node> nodes, int numUrbanCenters) {

		// Keep track of how many centers are left to define
		if (numUrbanCenters == 0)
			return;

		double d = Math.random();
		while (d >= 0.1) {
			d = Math.random();
		}

		int r = (int) Math.round(d * nodes.size());
		if (DEBUG) {
			System.out.println("Random urban center index: " + r);
			System.out.println("Node " + nodes.get(r).getId() + " is chosen as an urban center.");
		}

		// randomly choose a node to set as an urban center, and update the weights
		// involved
		nodes.get(r).setNodePriorityWeight(setRandomUrbanCenterWeight(urbanCenterWeight));
		setNeighborWeights(nodes.get(r), urbanCenterWeight - 1);
		createNeighborhoods(nodes, numUrbanCenters - 1);
		if (DEBUG) {
			System.out.println("Remaining urban centers to work with: " + (numUrbanCenters - 1));
		}
	}

	/**
	 * Recursively sets the weights of all neighbors of urban centers down to 0
	 * 
	 * @param node
	 *            - the node we're working with
	 * @param weight
	 *            - the current weight of the iteration
	 */
	public static void setNeighborWeights(Node node, int weight) {
		if (weight <= 0)
			return;
		for (Node n : node.getNeighbors()) {
			// if a node doesn't already have an assigned weight (preventing double
			// decrements)
			if (!n.isHasWeight()) {
				n.setNodePriorityWeight(weight);
				if (DEBUG) {
					System.out.println("Weight of node " + n.getId() + " = " + weight);
				}
				setNeighborWeights(n, weight - 1);
			}
		}
	}

	public static void initiateStoplights(Map map) {

		List<Stoplight> lightsA = new ArrayList<Stoplight>();
		List<Stoplight> lightsB = new ArrayList<Stoplight>();

		for (Stoplight s : map.getLights()) {
			if (s.getParent().getXmlID().contains("east") || s.getParent().getXmlID().contains("west")) {
				lightsA.add(s);
			} else {
				lightsB.add(s);
			}
		}

		LightController L1 = new LightController(lightsA);
		LightController L2 = new LightController(lightsB);

		L1.setStartsGreen(true);
		L2.setStartsGreen(false);

		L1.setScheme(scheme);
		L2.setScheme(scheme);

		controllers.add(L1);
		controllers.add(L2);
	}

	/**
	 * Randomly (Gaussian) choose a weight for each urban center around the mean
	 * 
	 * @param mean
	 *            - defined in class
	 * @return
	 */
	public static int setRandomUrbanCenterWeight(int mean) {
		Random r = new Random();
		double g = r.nextGaussian() + mean;
		if (g < 0 || g > mean * 2) {
			setRandomUrbanCenterWeight(mean);
		}
		System.out.println("Urban center weight: " + (int) g);
		return (int) g;
	}

	/**
	 * Draws a random variate from an (negative) exponential distribution with given
	 * rate
	 * 
	 * @param rate
	 *            - lambda
	 * @return
	 */
	public static double drawRandomExponential(double rate) {
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate, truncated to
		// [0,1]
		double x = -Math.log(1 - (1 - Math.pow(Math.E, -rate)) * u) / rate;
		return x;
	}

	public static double drawRandomNormal(double mean, double sd) {
		Random r = new Random();
		return r.nextGaussian() * sd + mean;
	}

	/**
	 * Procedurally generates a grid based on static parameters, unused as of phase
	 * 2
	 * 
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
				Car temp = new Car.Builder(mapNodes.get(x), mapNodes.get(y), map)
						.setDriverModel(new IntelligentDriverModel()).build();
				cars.add(temp);
			}

		}

		TrafficManager tm = new TrafficManager(map, cars, new ArrayList<TrafficObject>(), controllers);

		int y = 0;

		// createNeighborhoods(mapNodes, numUrbanCenters);

		return tm;

	}

	public static double getDurationOfTimestepInSeconds() {
		return 1. / TIMESTEPS_PER_SECOND;
	}

	public static int getTimestepAtTime(Time time) {
		return (TIMESTEPS_PER_SECOND * time.getSeconds()) + (60 * time.getMinutes()) + (60 * time.getHours());
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

		Car car1 = new Car.Builder(node2, node3, map).setDriverModel(new SimpleDriverModel(10)).build();
		// car1.setEdgePath(Arrays.asList(edge2));

		Car car2 = new Car.Builder(node2, node3, map).setDriverModel(new IntelligentDriverModel()).build();
		// car2.setEdgePath(Arrays.asList(edge2));

		Car car3 = new Car.Builder(node1, node3, map).setDriverModel(new IntelligentDriverModel()).setInitialSpeed(20).build();
		// car3.setEdgePath(Arrays.asList(edge1, edge2));

		TestTrafficObject blocker = new TestTrafficObject(new Location(edge2, 300));

		List cars = Arrays.asList(car1, car2, car3);

		List<TrafficObject> staticTrafficObjects = new ArrayList<TrafficObject>();
		staticTrafficObjects.add(blocker);

		return new TrafficManager(map, cars, staticTrafficObjects, controllers);
	}

	public static TrafficManager testcaseBlocker() {
		Node node1 = new Node(0, 0);
		Node node2 = new Node(500, 0);

		Edge edge = new Edge(node1, node2);

		Map map = new Map(Arrays.asList(node1, node2), Arrays.asList(edge));

		Car car = new Car.Builder(node1, node2, map).build();

		TestTrafficObject blocker = new TestTrafficObject(new Location(edge, 200));

		List cars = Arrays.asList(car);
		List blockers = Arrays.asList(blocker);

		return new TrafficManager(map, cars, blockers, null);
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
			Car car = new Car.Builder(nodes.get(0), nodes.get(nodeN - 1), map).setDriverModel(new SimpleDriverModel())
					.build();

			// car.setEdgePath(edges);

			cars.add(car);
		}

		return new TrafficManager(map, cars, new ArrayList<TrafficObject>(), controllers);
	}

	public static TrafficManager navigationTest() {
		Node a = new Node(0, 0);
		Node b = new Node(100, 100);
		Node c = new Node(100, 0);
		Node d = new Node(0, 100);
		Node e = new Node(1000, 1000);

		Edge ad = new Edge(a, d);
		Edge ae = new Edge(a, e);
		Edge eb = new Edge(e, b);
		Edge ac = new Edge(a, c);
		Edge cb = new Edge(c, b);

		List<Node> nodes = Arrays.asList(a, b, c, d, e);
		List<Edge> edges = Arrays.asList(ad, ae, eb, ac, cb);

		Map map = new Map(nodes, edges);

		Car car = new Car.Builder(a, b, map).build();

		ArrayList<Vehicle> cars = new ArrayList<Vehicle>();
		cars.add(car);

		return new TrafficManager(map, cars, new ArrayList<TrafficObject>(), new ArrayList<LightController>());
	}

	public static void main(String[] args) {
		TrafficManager tm = testcaseBig(1000, 1000);

		System.out.println("Created test case");

		tm.simulate(getMaximumTimesteps());

		System.out.println("Done with " + tm.lastComputedTimestep + " timesteps.");

		System.out.println(getTimeAtTimestep(tm.lastComputedTimestep));
	}
}
