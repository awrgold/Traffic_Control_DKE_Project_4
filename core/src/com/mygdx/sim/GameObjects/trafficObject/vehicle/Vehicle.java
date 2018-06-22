package com.mygdx.sim.GameObjects.trafficObject.vehicle;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.driverModel.DriverModel;
import com.mygdx.sim.GameObjects.pathfinding.DynamicPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObjectState;
import com.mygdx.sim.Resources.Resources;

public abstract class Vehicle implements TrafficObject {
	
	private static int lastGivenId = 0;
	private int id;

	// The node this vehicle starts its trip at and the node it wants to reach.
	Node startNode;
	Node goalNode;
	// The "time limit" for a car to reach its destination, used for experiments
	float timeLimit;
	protected String spriteName;
	private Sprite sprite;

	// Vehicle path
//	List<Edge> edgePath;

	// The timesteps when this vehicle begins and ends its journey.
	int startTimestep;
	int endTimestep = Integer.MAX_VALUE;
	int tripDuration = -1;

	// The maximum speed that this vehicle can achieve, ever.
	int maxSpeed;

	// The physical length of the vehicle in meters.
	private float length = 4;

	/**
	 * Stores for each timestep, the index in edgesToTravel of the edge
	 * that this vehicle is located at.
	 *
	 * If we do edgesToTravel.get(edgeIndices.get(t)), we should get
	 * the edge where this vehicle is located at timestep t.
	 */
	int[] edgeIndices = new int[0];
	
	double[] accelerations = new double[0];

	/**
	 * Stores for each timestep, the distance that this vehicle has traveled
	 * on the edge it is located on.
	 *
	 * If we have a 10-long edge, and we're moving at 3 per second:
	 * 0 3 6 9 2 5 8 ...
	 */
	float[] distancesTraveledOnEdge = new float[0];

	// Stores for each timestep, the speed that this vehicle was traveling at.
	float[] speeds = new float[0];

	/**
	 * Stores the algorithm that this vehicle uses for navigation/pathfinding.
	 * By default, it uses a simple A* pathfinder at the start of the trip, and
	 * doesn't adjust its path afterwards.
	 */
	private static Pathfinder pathfinder;

	/**
	 * Stores the algorithm that this vehicle uses to determine its acceleration.
	 * By default, it uses a very simple model that maintains a constant speed.
	 */
	DriverModel driverModel;
	
	private Map map;
	
	private static DynamicPathfinder pf = null;

	public Vehicle(Node startNode, Node goalNode, int maxSpeed, String spriteName, Pathfinder deprecatedPf, Map graph, int startTimestep, DriverModel driverModel, float initialSpeed) {

		setSprite(spriteName);

		initialize();

		this.id = lastGivenId++;

		// Set Start/Goal
		this.startNode = startNode;
		this.goalNode = goalNode;

		// Set maximum speed
		this.maxSpeed = maxSpeed;
		setAggression();

//		this.pathfinder = pf;
		
		if(pf == null)		
			pf = new DynamicPathfinder(graph);
		
		this.map = graph;

		this.startTimestep = startTimestep;

		this.driverModel = driverModel;

		speeds[0] = initialSpeed;
		tripDuration = 0;
		
		Edge firstEdge = pf.selectEdge(startNode, goalNode);
		
		if(firstEdge != null)
			edgeIndices[0] = pf.selectEdge(startNode, goalNode).getId();
		else
			throw new RuntimeException("Vehicle " + id + " failed to find a path from " + startNode.getId() + " to " + goalNode.getId());

		// Find path
//		computePath(0);
	}

	public float getLength() {	return length; }

	public float getTimeLimit(){
		return timeLimit;
	}

	public TrafficObjectState getState(int timestep) {
		Coordinates coordinates = this.getCoordinates(timestep);
		Location location = this.getLocation(timestep);
		float speed = this.getSpeed(timestep);
		boolean vizualize = this.isVisibleInVisualization(timestep);
		boolean visibleToDrivers = this.isVisibleToDrivers(timestep);

		return new TrafficObjectState(coordinates,location,speed,vizualize,visibleToDrivers);
	}
	
	public Location getLocation(int timestep) {
		return new Location(this.getEdge(timestep),this.getTraveledDistance(timestep));
	}

	public DriverModel getDriverModel() {
		return driverModel;
	}

	public int getTripDuration(){
	    return tripDuration;
    }

	public int getStartTimestep(){
		return startTimestep;
	}

	public int getEndTimestep(){
		return endTimestep;
	}

	public int getMaxSpeed(int timestep) {
		return (int) Math.min(maxSpeed, getEdge(timestep).getSpeedLimit());
	}

	public float getTraveledDistance(int timestep) {
		return distancesTraveledOnEdge[timestep];
	}

//	public List<Edge> getEdgePath() {
//		return edgePath;
//	}

	public float getSpeed(int timestep){
		return speeds[timestep];
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

	public Coordinates getCoordinates(int timestep) {
		Edge edge = getEdge(timestep);
		float distance = distancesTraveledOnEdge[timestep];
		return edge.getLocationIfTraveledDistance(distance);
	}
	
	public float getDistanceOnEdge(int timestep) {
		return distancesTraveledOnEdge[timestep];
	}

	public void setTimeLimit(int timeLimit){
		this.timeLimit = timeLimit;
	}

	public void setEndTimestep(int timestep){
		this.endTimestep = timestep;
	}

	public void decrementTime(){
		if (timeLimit > 0){
			timeLimit = timeLimit -1;
		}
	}

	public void setAggression(){
		maxSpeed = (int)(maxSpeed * drawRandomExponential(-0.1));
	}

	public void setAggression(int mean){
		Random r = new Random();
		double g = r.nextGaussian() + mean;
		if (g < 0 || g > mean*2) {
			setAggression(mean);
		}
	}

	// DO NOT USE THIS! This is for testing *ONLY*.
//	public void setEdgePath(List<Edge> edgePath) {
//		this.edgePath = edgePath;
//	}

	private void setSprite(String spriteName) {
		this.spriteName = spriteName;

		this.sprite = Resources.world.vehicleSprites.get(spriteName);
		sprite.setScale(0.5f);
	}

	// TODO: Get a way to find out how much time is left
//	public float getTimeRemaining(){
//
//	}

	public static double manhattanDistance(Node a, Node b) {
		return Math.abs((a.getY() - b.getY()) + (a.getX() - b.getX()));
	}

	public boolean isVisibleToDrivers(int timestep) { return timestep >= startTimestep && timestep <= endTimestep; }

	public boolean isVisibleInVisualization(int timestep) {	return timestep >= startTimestep && timestep <= endTimestep; }
	
	public boolean isMoving(int timestep) { return timestep >= startTimestep && timestep <= endTimestep; }

	public boolean isFinished(int timestep){
	    return timestep >= endTimestep;
    }
	
	public void ensureCapacity() {
		ensureCapacity(speeds.length + 1440);
	}
	
	public void ensureCapacity(int capacity) {
		int currentCapacity = speeds.length;
		
		float[] newSpeeds = new float[capacity];
		float[] newDistances = new float[capacity];
		double[] newAccelerations = new double[capacity];
		int[] newEdgeIndices = new int[capacity];
		
		int sizeToCopy = Math.min(currentCapacity, capacity);
		
		for(int i = 0; i < sizeToCopy; i++) {
			newSpeeds[i] = speeds[i];
			newDistances[i] = distancesTraveledOnEdge[i];
			newEdgeIndices[i] = edgeIndices[i];
			newAccelerations[i] = accelerations[i];
		}
		
		for(int i = currentCapacity; i < capacity; i++) {
			newSpeeds[i] = -1;
			newDistances[i] = -1;
			newEdgeIndices[i] = -1;
			newAccelerations[i] = -1;
		}
		
		speeds = newSpeeds;
		distancesTraveledOnEdge = newDistances;
		edgeIndices = newEdgeIndices;
		accelerations = newAccelerations;
	}

	private void initialize() {
		ensureCapacity();
		
		speeds[0] = 0;
		edgeIndices[0] = 0;
		distancesTraveledOnEdge[0] = 0;
		accelerations[0] = 0;
	}

	public String toString() {
		return ("[Vehicle " + id + "]");
	}

	/**
	 * Recomputes the path after a given timestep. If this vehicle has already
	 * traveled for 10 timesteps and you're recomputing then, timestep should
	 * be 10 - otherwise, the pathfinder could recompute some edges that have
	 * already been traveled, and break EVERYTHING.
	 * @param timestep - the edges that have already been traveled at that timestep may not be changed by the pathfinder
	 */
//	public void computePath(int timestep) {
//		this.edgePath = pathfinder.findPath(this, timestep);
//	}
	
	/**
	 * Applies the acceleration provided by a DriverModel to this vehicle, setting its speed in the given timestep.
	 * @param timestep timestep to determine speed for
	 * @param acceleration acceleration to apply
	 */
	public void accelerate(int timestep, double acceleration) {
		if(timestep == 0)
			return;
		
		accelerations[timestep] = acceleration;
		
		if(speeds[timestep] != -1) {
			System.out.println("You're trying to set a speed that has already been set. You're doing something wrong.");
			return;
		}
		
		double previousSpeed = speeds[timestep-1];
		
		double accelerationPerTimestep = acceleration / TrafficManager.TIMESTEPS_PER_SECOND;
		
		double newSpeed = Math.max(previousSpeed + (acceleration / TrafficManager.TIMESTEPS_PER_SECOND),0);
		
		speeds[timestep] = ((float) newSpeed);
	}
	
	/**
	 * Applies the previous speed of this car to the previous location to compute the new location.
	 * @param timestep - the timestep up to which we are moving.
	 */
	public void move(int timestep) {
		
		if(distancesTraveledOnEdge[timestep] != -1) {
			System.out.println("You're setting a location for a timestep where the location has already been computed. You're doing something wrong.");
			return;
		}
		
		if(speeds[timestep-1] == -1) {
			System.out.println("You're setting a location for a timestep where the previous speed hasn't been computed. You're doing something wrong.");
			return;
		}
		
		// Get the speed we are moving at
		float speed = speeds[timestep-1]/TrafficManager.TIMESTEPS_PER_SECOND;
		
		// Get the index of the edge we are currently on
		int edgeIdx = edgeIndices[timestep-1];
		
		// Get the distance we have traveled on the current edge
		float distanceTraveledOnEdge = distancesTraveledOnEdge[timestep-1];
		
		// Check if the vehicle is still allowed to move.
		if(isMoving(timestep)) {
			// Add the speed we are currently moving at to our old location in order to determine
			// our new location
			distanceTraveledOnEdge += speed;
			
			// Get the length of the current edge
			float currentEdgeLength = getEdge(timestep-1).getLength();
			
			// Check if we have reached the end of the edge we were on in the last timestep. 
			// If yes, we need to move to the next edge.		
			if(distanceTraveledOnEdge >= currentEdgeLength) {
				
				Node reachedNode = getEdge(timestep-1).getTo();
				
				// Check if the destination has been reached
//				if(edgeIdx == edgePath.size()-1) {
				if(reachedNode.equals(goalNode)) {
					
					// Disallow the car from moving further
					endTimestep = timestep;
					
					// Set the distance traveled on the edge to the length on the edge
					// to indicate that we are at its end
					distanceTraveledOnEdge = currentEdgeLength;

				} else {
					
					// Increment the edge index to indicate we have moved on to the next edge from our path
					edgeIdx = pf.selectEdge(reachedNode, goalNode).getId();
					
					// Subtract the last edge's length from the distance we have traveled, so we are only storing the
					// distance traveled on the current (new) edge
					distanceTraveledOnEdge -= currentEdgeLength;
				}				
			}			
		}

		// Set the edge index and traveled distance
		edgeIndices[timestep] = edgeIdx;
		distancesTraveledOnEdge[timestep] = distanceTraveledOnEdge;
	}
	
	/**
	 * Returns the Edge that this vehicle is located on at the given timestep.
	 */
	public Edge getEdge(int timestep) {
		return map.getEdges().get(edgeIndices[timestep]);
//		return edgePath.get(edgeIndices[timestep]);
	}
	
	/**
	 * Returns the Coordinates that this vehicle is located on at the given timestep.
	 */
	public void draw(SpriteBatch spriteBatch, float x, float y, float rotation) {
		sprite.setPosition(x, y);
		sprite.setRotation(rotation);
		sprite.draw(spriteBatch);
	}

	public int hashCode() {
		return id;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Vehicle)) return false;
		Vehicle v = (Vehicle) o;
		
		return (v.id == this.id);
	}

	public static double drawRandomExponential(double mean) {
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with given mean
		double res = (1 + (-mean)*(Math.log(u)));
		//System.out.println(res);
		return res;
		}
}
