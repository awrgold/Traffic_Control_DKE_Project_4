package com.mygdx.sim.GameObjects.vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.driverModel.DriverModel;
import com.mygdx.sim.GameObjects.driverModel.SimpleDriverModel;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public abstract class Vehicle {
	
	/**
	 * The node this vehicle starts its trip at.
	 */
	final Node startNode;
	
	/**
	 * The node this vehicle wants to reach.
	 */
	final Node goalNode;
	
	/**
	 * The maximum speed that this vehicle can achieve, ever.
	 */
	final int maxSpeed;
	
	/**
	 * The edges that this vehicle is supposed to travel from its start point
	 * to its endpoint.
	 */
	List<Edge> edgePath;
	
	//TODO: delete this, it's temporary
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
	int[] edgeIndices = new int[1000];
	//TODO: implement configurable number of timesteps
	
	/**
	 * Stores for each timestep, the distance that this vehicle has traveled
	 * on the edge it is located on.
	 * 
	 * If we have a 10-long edge, and we're moving at 3 per second:
	 * 0 3 6 9 2 5 8 ...
	 */
	double[] distancesTraveledOnEdge = new double[1000];
	
	/**
	 * Stores for each timestep, the speed that this vehicle was traveling at.
	 */
	double[] speeds = new double[1000];
	
	// Ignore. Keeps track of whether you're doing something very wrong.
	boolean[] computedSpeeds = new boolean[1000];
	boolean[] computedLocations = new boolean[1000];
	
	/**
	 * Stores the algorithm that this vehicle uses for navigation/pathfinding.
	 * By default, it uses a simple A* pathfinder at the start of the trip, and
	 * doesn't adjust its path afterwards.
	 */
	Pathfinder pathfinder = new AStarPathfinder();
	
	/**
	 * Stores the algorithm that this vehicle uses to determine its acceleration.
	 * By default, it uses a very simple model that maintains a constant speed.
	 */
	DriverModel driverModel = new SimpleDriverModel();

	/**
	 * The name of the sprite that this vehicle uses.
	 */
	protected String spriteName;

	/**
	 * The sprite that this vehicle uses.
	 */
	private Sprite sprite;

	public Vehicle(Node startNode, Node goalNode, int maxSpeed, String spriteName) {

		setSprite(spriteName);

		// Set Start/Goal
		this.startNode = startNode;
		this.goalNode = goalNode;
		
		// Set maximum speed
		this.maxSpeed = maxSpeed;
		
		
	}
	
	public Vehicle(Node startNode, Node goalNode, int maxSpeed, String spriteName, Pathfinder pathfinder) {
		this(startNode,goalNode,maxSpeed,spriteName);
		this.pathfinder = pathfinder;
	}

	private void setSprite(String spriteName) {
		this.spriteName = spriteName;
//		sprite = Resources.world.vehicleSprites.get(spriteName);
	}
	
	public void move(int timestep) {
		if(computedLocations[timestep]) {
			System.out.println("You're setting a location for a timestep where the location has already been computed. You're doing something wrong.");
			return;
		}
		// Get the speed we are moving at
		double speed = speeds[timestep-1];
		
		// Get the index of the edge we are currently on
		int edgeIdx = edgeIndices[timestep-1];
		
		// Get the distance we have traveled on the current edge
		double distanceTraveledOnEdge = distancesTraveledOnEdge[timestep-1];
		
		// Add the speed we are currently moving at to our old location in order to determine
		// our new location
		distanceTraveledOnEdge += speed;
		
		// Get the length of the current edge
		double currentEdgeLength = getEdgeAt(timestep-1).getLength();
		
		// Check if we have reached the end of the edge we were on in the last timestep. 
		// If yes, we need to move to the next edge.		
		if(distanceTraveledOnEdge > currentEdgeLength) {
			// TODO: implement stopping when goal node has been reached
			
			// Increment the edge index to indicate we have moved on to the next edge from our path
			edgeIdx++;
			
			// Subtract the last edge's length from the distance we have traveled, so we are only storing the
			// distance traveled on the current (new) edge
			distanceTraveledOnEdge -= currentEdgeLength;
			
			
		}
		
		// Set the edge index and traveled distance 
		edgeIndices[timestep] = edgeIdx;
		distancesTraveledOnEdge[timestep] = distanceTraveledOnEdge;
	}
	
	/**
	 * Returns the Edge that this vehicle is located on at the given timestep.
	 * @param timestep
	 * @return the Edge that this vehicle is located on at the given timestep
	 */
	public Edge getEdgeAt(int timestep) {
		return edgePath.get(edgeIndices[timestep]);
	}
	
	/**
	 * Returns the Coordinates that this vehicle is located on at the given timestep.
	 * @param timestep
	 * @return the Coordinates that this vehicle is located on at the given timestep
	 */
	public Coordinates getLocationCoordinates(int timestep) {	
		Edge edge = getEdgeAt(timestep);
		double distance = distancesTraveledOnEdge[timestep];
		return edge.getLocationIfTraveledDistance(distance);
	}

	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public DriverModel getDriverModel() {
		return driverModel;
	}
	
	public void setSpeed(int timestep, double speed) {
		if(computedSpeeds[timestep]) {
			System.out.println("You're trying to set a speed that has already been set. You're doing something wrong.");
			return;
		}
		
		speeds[timestep] = speed;
		computedSpeeds[timestep] = true;
	}
}
