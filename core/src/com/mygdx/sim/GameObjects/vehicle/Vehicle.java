package com.mygdx.sim.GameObjects.vehicle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.driverModel.DriverModel;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;
import com.mygdx.sim.Resources.Resources;

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
	
	/**
	 * Stores the algorithm that this vehicle uses for navigation/pathfinding.
	 * By default, it uses a simple A* pathfinder at the start of the trip, and
	 * doesn't adjust its path afterwards.
	 */
	Pathfinder pathfinder = new AStarPathfinder();
	
	DriverModel model;

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
		sprite = Resources.world.vehicleSprites.get(spriteName);
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

	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {
		// All check the vehicle has to make
	}
}
