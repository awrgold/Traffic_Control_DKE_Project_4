package com.mygdx.sim.GameObjects.vehicle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.driverModel.DriverModel;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;
import com.mygdx.sim.Resources.Resources;

public abstract class Vehicle {
	
	private static int lastGivenId = 0;
	
	private boolean findDifferentPathOnFail = true;
	
	private int id;
	
	/**
	 * The node this vehicle starts its trip at.
	 */
	Node startNode;
	
	/**
	 * The node this vehicle wants to reach.
	 */
	Node goalNode;
	
	/**
	 * The maximum speed that this vehicle can achieve, ever.
	 */
	int maxSpeed;
	
	/**
	 * The physical length of the vehicle in meters.
	 */
	private double length = 4;
	
	public double getLength() {
		return length;
	}
	
	/**
	 * The edges that this vehicle is supposed to travel from its start point
	 * to its endpoint.
	 */
	List<Edge> edgePath;

	
	// DO NOT USE THIS! This is for testing *ONLY*.
	public void setEdgePath(List<Edge> edgePath) {
		this.edgePath = edgePath;
	}
	
	
	/**
	 * True means the car is allowed to move, false means it is not.
	 * False if it has reached its destination.
	 */
	private boolean moving = true;
	
	/**
	 * Stores for each timestep, the index in edgesToTravel of the edge
	 * that this vehicle is located at.
	 * 
	 * If we do edgesToTravel.get(edgeIndices.get(t)), we should get
	 * the edge where this vehicle is located at timestep t.
	 */
	ArrayList<Integer> edgeIndices = new ArrayList<Integer>();
	
	/**
	 * Stores for each timestep, the distance that this vehicle has traveled
	 * on the edge it is located on.
	 * 
	 * If we have a 10-long edge, and we're moving at 3 per second:
	 * 0 3 6 9 2 5 8 ...
	 */
	ArrayList<Double> distancesTraveledOnEdge = new ArrayList<Double>();
	
	/**
	 * Stores for each timestep, the speed that this vehicle was traveling at.
	 */
	ArrayList<Double> speeds = new ArrayList<Double>();
	
	// Ignore. Keeps track of whether you're doing something very wrong.
	ArrayList<Boolean> computedSpeeds = new ArrayList<Boolean>();
	ArrayList<Boolean> computedLocations = new ArrayList<Boolean>();
	
	/**
	 * Stores the algorithm that this vehicle uses for navigation/pathfinding.
	 * By default, it uses a simple A* pathfinder at the start of the trip, and
	 * doesn't adjust its path afterwards.
	 */
	Pathfinder pathfinder;
	
	/**
	 * Stores the algorithm that this vehicle uses to determine its acceleration.
	 * By default, it uses a very simple model that maintains a constant speed.
	 */
	DriverModel driverModel = new SimpleDriverModel();
	
	public DriverModel getDriverModel() {
		return driverModel;
	}
	
	public void setDriverModel(DriverModel dm) {
		this.driverModel = dm;
	}

	/**
	 * The name of the sprite that this vehicle uses.
	 */
	protected String spriteName;

	/**
	 * The sprite that this vehicle uses.
	 */
	private Sprite sprite;
	
	private double maxAcceleration = 1.4;
	
	public double getMaxAcceleration() {
		return maxAcceleration;
	}
	
	private double safetyHeadway = 1.5;
	
	public double getSafetyHeadway() {
		return safetyHeadway;
	}
	
	private void setSprite(String spriteName) {
		this.spriteName = spriteName;
		
		sprite = Resources.world.vehicleSprites.get(spriteName);
		sprite.setScale(0.5f);
	}
	

	public Vehicle(Node startNode, Node goalNode, int maxSpeed, String spriteName, Map graph) {

		setSprite(spriteName);
		
		this.id = lastGivenId++;

		// Set Start/Goal
		this.startNode = startNode;
		this.goalNode = goalNode;
		
		// Set maximum speed
		this.maxSpeed = maxSpeed;
		setAggression();
		
		pathfinder = new AStarPathfinder(graph);
		
		// Find path
		computePath(0);
		
		initialize();
	}
	
	public String toString() {
		return ("[Vehicle " + id + "]");
	}
	
	public Vehicle(Node startNode, Node goalNode, int maxSpeed, String spriteName, Map graph, Pathfinder pathfinder) {
		this(startNode,goalNode,maxSpeed,spriteName,graph);
		this.pathfinder = pathfinder;
	}

	/**
	 * Recomputes the path after a given timestep. If this vehicle has already
	 * traveled for 10 timesteps and you're recomputing then, timestep should
	 * be 10 - otherwise, the pathfinder could recompute some edges that have
	 * already been traveled, and break EVERYTHING.
	 * @param timestep - the edges that have already been traveled at that timestep may not be changed by the pathfinder
	 */
	public void computePath(int timestep) {
		this.edgePath = pathfinder.findPath(this, timestep, findDifferentPathOnFail);
	}
	
	public void accelerate(int timestep, double acceleration) {
		if(computedSpeeds.get(timestep)) {
			System.out.println("You're trying to set a speed that has already been set. You're doing something wrong.");
			return;
		}
		
		double previousSpeed = 0;
		if(timestep!=0) 
			previousSpeed = speeds.get(timestep-1);
		
		double newSpeed = previousSpeed + acceleration * TrafficManager.TIMESTEPS_PER_SECOND;
		
		speeds.set(timestep, newSpeed);
		computedSpeeds.set(timestep, true);
	}
	
	/**
	 * Applies the previous speed of this car to the previous location to compute the new location.
	 * @param timestep - the timestep up to which we are moving.
	 */
	public void move(int timestep) {
		
		if(computedLocations.get(timestep)) {
			System.out.println("You're setting a location for a timestep where the location has already been computed. You're doing something wrong.");
			return;
		}
		
		if(!computedSpeeds.get(timestep-1)) {
			System.out.println("You're setting a location for a timestep where the previous speed hasn't been computed. You're doing something wrong.");
			return;
		}
		
		// Get the speed we are moving at
		double speed = speeds.get(timestep-1)/TrafficManager.TIMESTEPS_PER_SECOND;
		
		// Get the index of the edge we are currently on
		int edgeIdx = edgeIndices.get(timestep-1);
		
		// Get the distance we have traveled on the current edge
		double distanceTraveledOnEdge = distancesTraveledOnEdge.get(timestep-1);
		
		// Check if the vehicle is still allowed to move.
		if(moving) {
			// Add the speed we are currently moving at to our old location in order to determine
			// our new location
			distanceTraveledOnEdge += speed;
			
			// Get the length of the current edge
			double currentEdgeLength = getEdgeAt(timestep-1).getLength();
			
			// Check if we have reached the end of the edge we were on in the last timestep. 
			// If yes, we need to move to the next edge.		
			if(distanceTraveledOnEdge >= currentEdgeLength) {	
				
				// Check if the destination has been reached
				if(edgeIdx == edgePath.size()-1) {
					
					// Disallow the car from moving further
					moving = false;
					
					// Set the distance traveled on the edge to the length on the edge
					// to indicate that we are at its end
					distanceTraveledOnEdge = currentEdgeLength;
				} else {
					
					// Increment the edge index to indicate we have moved on to the next edge from our path
					edgeIdx++;
					
					// Subtract the last edge's length from the distance we have traveled, so we are only storing the
					// distance traveled on the current (new) edge
					distanceTraveledOnEdge -= currentEdgeLength;
				}				
			}			
		}
		
		// Set the edge index and traveled distance 
		edgeIndices.set(timestep, edgeIdx);
		distancesTraveledOnEdge.set(timestep, distanceTraveledOnEdge);
		
		// Indicate that the location for this timestep has been computed
		computedLocations.set(timestep, true);
	}
	
	/**
	 * Returns the Edge that this vehicle is located on at the given timestep.
	 * @param timestep
	 * @return the Edge that this vehicle is located on at the given timestep
	 */
	public Edge getEdgeAt(int timestep) {
		return edgePath.get(edgeIndices.get(timestep));
	}
	
	/**
	 * Returns the Coordinates that this vehicle is located on at the given timestep.
	 * @param timestep
	 * @return the Coordinates that this vehicle is located on at the given timestep
	 */
	public Coordinates getLocationCoordinates(int timestep) {	
		Edge edge = getEdgeAt(timestep);
		double distance = distancesTraveledOnEdge.get(timestep);
		return edge.getLocationIfTraveledDistance(distance);
	}

	public void draw(SpriteBatch spriteBatch, float x, float y, float rotation) {
		sprite.setPosition(x, y);
		sprite.setRotation(rotation);
		sprite.draw(spriteBatch);
	}
	
	public int getMaxSpeed(int timestep) {
		return (int) Math.min(maxSpeed, getEdgeAt(timestep).getSpeedLimit());
	}
	
	/**
	 * Sets this vehicle's speed at a given timestep.
	 * @param timestep - the timestep for which we're setting the speed
	 * @param speed - the speed at that timestep
	 */
//	public void setSpeed(int timestep, double speed) {
//		if(computedSpeeds.get(timestep)) {
//			System.out.println("You're trying to set a speed that has already been set. You're doing something wrong.");
//			return;
//		}
//		
//		speeds.set(timestep, speed);
//		computedSpeeds.set(timestep, true);
//	}
	
	/**
	 * Initializes the history-keeping ArrayLists to hold at least one element.
	 * Things break otherwise
	 */
	private void initialize() {
		if(edgeIndices.size() == 0) {
			addZeros();
			computedLocations.set(0, true);
		} else
			System.out.println("You're trying to initialize a vehicle that has already been initialized. You're doing something wrong.");		
	}
	
	/**
	 * Utility method used by ensureCapacity to increase the capacity of all ArrayLists.
	 */
	private void addZeros() {
		edgeIndices.add(0);
		distancesTraveledOnEdge.add(0.);
		speeds.add(0.);
		computedLocations.add(false);
		computedSpeeds.add(false);
	}
	
	/**
	 * Ensures that the history-keeping variables of this Vehicle have sufficient capacity
	 * for the given number of timesteps
	 * @param timestep - number of timesteps we need to be able to keep history of
	 */
	public void ensureCapacity(int timestep) {
		while(edgeIndices.size() <= timestep) {
			addZeros();
		}
	}

	public double getTraveledDistance(int timestep) {
		return distancesTraveledOnEdge.get(timestep);
	}
	
	public List<Edge> getEdgePath() {
		return edgePath;
	}
	
	public double getSpeedAt(int timestep){
		return speeds.get(timestep);
	}

	public Node getStartNode(){
		return startNode;
	}
	
	public void setStartNode(Node startNode){
		this.startNode = startNode;
	}

	public Node getGoalNode(){
		return goalNode;
	}
	
	public void setGoalNode(Node goalNode){
		this.goalNode = goalNode;
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Vehicle)) return false;
		Vehicle v = (Vehicle) o;
		
		return (v.id == this.id);
	}


	public void setAggression(){
		maxSpeed = (int)(maxSpeed * drawRandomExponential(-0.1));
	}

	public static double drawRandomExponential(double mean) {
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with given mean
		double res = (1 + (-mean)*(Math.log(u)));
		//System.out.println(res);
		return res;
		}

	public boolean isMoving() {
		return moving;
	}
}
