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
	public final static Time DURATION = new Time(2, 0, 0);

	// Sampling frequency. Larger number means higher fidelity of the model, but
	// also more computation
	public final static int TIMESTEPS_PER_SECOND = 5;

	private final static int VIEW_DISTANCE = 500;
	private final static int RIDICULOUS_SPEED = 1000;

	// Temporary map bounds
	public final static int MAP_X_DIM = 2000;
	public final static int MAP_Y_DIM = 2000;
	public final static int GRID_FACTOR = 5; // GRID_FACTOR = N -> where the map is NxN grid
	public final static int vehicleCount = 10;
	public final static int numUrbanCenters = 3;
	public final static int uCenterWeight = 3;

	private static Map map;
	private static List<Vehicle> vehicles;

	private int lastComputedTimestep = 0;

	private ArrayList<HashMap<Vehicle, Coordinates>> history = new ArrayList<HashMap<Vehicle, Coordinates>>();

	public TrafficManager(Map map, List<Vehicle> vehicles) {

		this.map = map;
		this.vehicles = vehicles;

		ensureCapacity(lastComputedTimestep);

		for (Vehicle vehicle : vehicles) {
			history.get(lastComputedTimestep).put(vehicle, vehicle.getLocationCoordinates(lastComputedTimestep));

			// Validation of the path
			List<Edge> edgePath = vehicle.getEdgePath();
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
	 * Gets the locations of all vehicles at the given timestep.
	 * 
	 * @param timestep
	 *            - timestep for which you need the locations
	 * @return a HashMap that maps Vehicles to their Coordinates
	 */
	public HashMap<Vehicle, Coordinates> getState(int timestep) {
		if (timestep >= history.size())
			simulate(timestep);

		return history.get(timestep);
	}

	public ArrayList<HashMap<Vehicle, Coordinates>> getHistory() {
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

		while (lastComputedTimestep < finalTimeStep) {
			for (Vehicle vehicle : vehicles)
				map.getLocationCache().get(vehicle.getEdgeAt(lastComputedTimestep)).get(lastComputedTimestep)
						.add(vehicle);

			// Set accelerations for the current timestep
			for (Vehicle vehicle : vehicles) {
				// Use the driver model the vehicle uses to determine the vehicle's acceleration
				double acceleration = vehicle.getDriverModel().determineAcceleration(this, vehicle,
						lastComputedTimestep);

				// Set the acceleration
				vehicle.accelerate(lastComputedTimestep, acceleration);

				/*
				 * Ask our pathfinding algorithm for a path - It can still return the very same
				 * path - we're only giving it the opportunity to change the path, not requiring
				 * it
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
	 * Ensures that this TrafficManager's history has enough space for the given
	 * number of timesteps
	 * 
	 * @param timestep
	 *            - number of timesteps we need to be able to save history for
	 */
	private void ensureCapacity(int timestep) {
		while (history.size() <= timestep)
			history.add(new HashMap<Vehicle, Coordinates>());
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

	private DistanceAndVehicle getClosestVehicle(Vehicle vehicle, Edge currentEdge, double distanceUntilNow, int timestep) {
		List<Vehicle> vehiclesOnCurrentEdge = (List<Vehicle>) map.getLocationCache().get(currentEdge).get(timestep).clone();

		ArrayList<DistanceAndVehicle> candidates = new ArrayList<DistanceAndVehicle>();
		for (Vehicle vehicle2 : vehiclesOnCurrentEdge) {
			double distance = distanceUntilNow + vehicle2.getTraveledDistance(timestep);
			if (distance - Util.DELTA_EPSILON > 0 && vehicle2.isMoving())
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

	public static TrafficManager createSimpleTestcase() {
		Node node1 = new Node(0, 0);
		Node node2 = new Node(475, 0);
		Node node3 = new Node(475, 1000);
		Edge edge1 = new Edge(node1, node2);
		Edge edge2 = new Edge(node2, node3);

		Map map = new Map(Arrays.asList(node1, node2, node3), Arrays.asList(edge1, edge2));

		Car car1 = new Car(node2, node3, map);
		car1.setEdgePath(Arrays.asList(edge2));
		car1.setDriverModel(new SimpleDriverModel(10));

		Car car2 = new Car(node2, node3, map);
		car2.setEdgePath(Arrays.asList(edge2));
		car2.setDriverModel(new IntelligentDriverModel());

		Car car3 = new Car(node1, node3, map);
		car3.setEdgePath(Arrays.asList(edge1, edge2));
		car3.setDriverModel(new IntelligentDriverModel());

		List cars = Arrays.asList(car1, car2, car3);

		TrafficManager tm = new TrafficManager(map, cars);
		int y = 0;

		return tm;
	}

	public static TrafficManager createGrid() {

		List<Node> mapNodes = new ArrayList<Node>();
		List<Edge> mapEdges = new ArrayList<Edge>();
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
						|| euclideanDistance(mapNodes.get(i), mapNodes.get(j)) == (MAP_Y_DIM / GRID_FACTOR))) {

					System.out.println("Adding Edge between: (" + mapNodes.get(i).getLocation().toString() + ", "
							+ mapNodes.get(j).getLocation().toString() + ")");
					mapEdges.add(new Edge(mapNodes.get(i), mapNodes.get(j)));
					edgeCount++;
					System.out.println("Edges: " + edgeCount);

				}
			}
		}

		vehicles = new ArrayList<Vehicle>();
		map = new Map(mapNodes, mapEdges);

		for (int i = 0; i < vehicleCount; i++) {
			int x = (int) (Math.random() * mapNodes.size());
			int y = (int) (Math.random() * mapNodes.size());

			if (mapNodes.get(y).isDestination() && !mapNodes.get(x).equals(mapNodes.get(y))) {
				Car temp = new Car(mapNodes.get(x), mapNodes.get(y), map);
				vehicles.add(temp);
				temp.setDriverModel(new IntelligentDriverModel());
			}

		}

		TrafficManager tm = new TrafficManager(map, vehicles);
		tm.createIntersections();

		int y = 0;

		// createNeighborhoods(mapNodes, numUrbanCenters);

		return tm;

	}

	public static void createIntersections() {

		// Lets make a list of nodes that need to become intersections
		ArrayList<Node> nodesToIntersection = new ArrayList<Node>();

		// Iterate through all nodes
		for (Node node : map.getNodes()) {

			// Iterate through all neighbours
			if (isNodeToIntersection(node)) {
				nodesToIntersection.add(node);
			}
		}

		boolean hasBeenChecked[][] = new boolean[nodesToIntersection.size()][nodesToIntersection.size()];

		// So now we have the nodes that need to be converted into intersections
		// Lets do that:
		for (Node node : nodesToIntersection) {
			nodeToIntersection(node, hasBeenChecked, nodesToIntersection);
		}
		
		System.out.println("Node Count: " + map.getNodes().size());
	}

	public static boolean isNodeToIntersection(Node node) {

		// Collect all neighbour nodes of the node to be checked
		ArrayList<Node> nodeNeighbours = new ArrayList<Node>();

		nodeNeighbours.addAll(node.getIncomingNeighbors());
		nodeNeighbours.addAll(node.getOutgoingNeighbors());

		// Iterate through all neighbours
		for (Node nodeNeighbour : nodeNeighbours) {
			for (Node nodeNeighbour2 : nodeNeighbours) {

				// Here we want to check if our node has at least 2 distinct neighbours, which
				// makes it a node that needs to become an intersection
				if (!(nodeNeighbour.equals(nodeNeighbour2))) {
					// Node has two distinct neighbours
					return true;
				}
			}
		}

		// Node does not have two distinct neighbours
		return false;
	}

	public static void nodeToIntersection(Node node, boolean[][] hasBeenChecked, ArrayList<Node> nodes) {

		// Lets start with incoming neighbours
		for (Node neighbourNode : node.getIncomingNeighbors()) {
			
			Node newNode1 = null;
			Node newNode2 = null;
			Node newNode3 = null;
			Node newNode4 = null;

			if (!hasBeenChecked[nodes.indexOf(neighbourNode)][nodes.indexOf(node)] && !hasBeenChecked[nodes.indexOf(node)][nodes.indexOf(neighbourNode)]) {
				
				// Node 1
				double distanceX = neighbourNode.getX() - node.getX();
				double distanceY = neighbourNode.getY() - node.getY();

				int offset = (int) (euclideanDistance(neighbourNode, node) / 10);
				int offsetX = 0;
				int offsetY = 0;

				if (distanceX != 0) {
					offsetY = (distanceX > 0) ? offset : -offset;
				}

				if (distanceY != 0) {
					offsetX = (distanceY > 0) ? -offset : offset;
				}

				map.getNodes().add((newNode1 = new Node(node.getX() + distanceX / 5 + offsetX, node.getY() + distanceY / 5 + offsetY)));

				// Node 2
				distanceX = node.getX() - neighbourNode.getX();
				distanceY = node.getY() - neighbourNode.getY();

				offsetX = 0;
				offsetY = 0;

				if (distanceX != 0) {
					offsetY = (distanceX > 0) ? -offset : offset;
				}

				if (distanceY != 0) {
					offsetX = (distanceY > 0) ? offset : -offset;
				}

				map.getNodes().add((newNode2 = new Node(neighbourNode.getX() + distanceX / 5 + offsetX, neighbourNode.getY() + distanceY / 5 + offsetY)));
				
				// Node 3
				distanceX = neighbourNode.getX() - node.getX();
				distanceY = neighbourNode.getY() - node.getY();

				offset = (int) (euclideanDistance(neighbourNode, node) / 10);
				offsetX = 0;
				offsetY = 0;

				if (distanceX != 0) {
					offsetY = (distanceX > 0) ? -offset : offset;
				}

				if (distanceY != 0) {
					offsetX = (distanceY > 0) ? offset : -offset;
				}

				map.getNodes().add((newNode3 = new Node(node.getX() + distanceX / 5 + offsetX, node.getY() + distanceY / 5 + offsetY)));
				
				// Node 4
				distanceX = node.getX() - neighbourNode.getX();
				distanceY = node.getY() - neighbourNode.getY();

				offset = (int) (euclideanDistance(neighbourNode, node) / 10);
				offsetX = 0;
				offsetY = 0;

				if (distanceX != 0) {
					offsetY = (distanceX > 0) ? offset : -offset;
				}

				if (distanceY != 0) {
					offsetX = (distanceY > 0) ? -offset : offset;
				}

				map.getNodes().add((newNode4 = new Node(neighbourNode.getX() + distanceX / 5 + offsetX, neighbourNode.getY() + distanceY / 5 + offsetY)));
				
				hasBeenChecked[nodes.indexOf(neighbourNode)][nodes.indexOf(node)] = true;
				hasBeenChecked[nodes.indexOf(node)][nodes.indexOf(neighbourNode)] = true;
			}
			
			if(newNode1 != null && newNode2 != null) {
				map.getEdges().add(new Edge(newNode2, newNode1));
				map.getEdges().add(new Edge(newNode3, newNode4));
			}
			
			// Remove edges we don't 
			ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
			for(Edge edge : node.getInEdges()) {
				if((edge.getFrom() == node && edge.getTo() == neighbourNode) || edge.getFrom().equals(neighbourNode) && edge.getTo().equals(node)) {
					edgesToRemove.add(edge);
				}
			}
			
			map.getEdges().removeAll(edgesToRemove);
		}
	}

	/**
	 * The idea is to create priority neighbourhoods such as urban/suburban centres
	 * that cars are more likely to head towards. However, since intersections have
	 * multiple nodes, we need to redefine several parameters. First of all, we need
	 * to classify what a "destination" node is, such that a car does not decide to
	 * choose the middle of an intersection as his "destination." This is necessary
	 * because we have to define priority to a specific node (as a car chooses only
	 * a node as his destination) so only destination nodes can receive priority.
	 * 
	 * @param nodes
	 *            - Map nodes
	 * @param numUrbanCenters
	 *            - the number of Urban Centers designed for this map
	 */
	private static void createNeighborhoods(List<Node> nodes, int numUrbanCenters) {
		int centers = numUrbanCenters;

		if (centers == 0)
			return;
		for (Node n : nodes) {
			if (n.isDestination() && Math.random() < .1 && centers > 0) {
				n.setNodePriorityWeight(uCenterWeight);
				for (Node i : n.getOutgoingNeighbors()) {
					i.setNodePriorityWeight(n.getNodePriorityWeight() - 1);
				}
				for (Node j : n.getIncomingNeighbors()) {
					j.setNodePriorityWeight(n.getNodePriorityWeight() - 1);
				}
				centers--;
			}
		}

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

	// public static void main(String[] args) {
	// TrafficManager tm1 = createSimpleTestcase();
	//
	// tm1.simulate(getMaximumTimesteps());
	//
	// System.out.println("Simple test case simulation completed");
	//
	// TrafficManager tm2 = createGrid();
	//
	// tm2.simulate(getMaximumTimesteps());
	//
	// System.out.println("Andrew-generated test case simulation completed");
	//
	// TrafficManager tm = new TrafficManager(map,cars);
	//
	// tm.simulate(getMaximumTimesteps());
	//
	// System.out.println("Done with " + tm2.lastComputedTimestep + " timesteps.");
	//
	// System.out.println(getTimeAtTimestep(tm2.lastComputedTimestep));
	// }

}
