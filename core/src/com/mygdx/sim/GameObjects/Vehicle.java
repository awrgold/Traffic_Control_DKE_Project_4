package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.CoreClasses.TrafficSimulator;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
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

	/**
	 * The name of the sprite that this vehicle uses.
	 */
	protected String spriteName;

	/**
	 * The sprite that this vehicle uses.
	 */
	private Sprite sprite;

	public Vehicle(Node startNode, Node goalNode, int maxSpeed) {

		// Set Sprite
		initSprite();

		// Set Start/Goal
		this.startNode = startNode;
		this.goalNode = goalNode;
		
		// Set maximum speed
		this.maxSpeed = maxSpeed;
	}
	
	public Vehicle(Node startNode, Node goalNode, int maxSpeed, Pathfinder pathfinder) {
		this(startNode,goalNode,maxSpeed);
		this.pathfinder = pathfinder;
	}

	private void initSprite() {
		sprite = Resources.world.vehicleSprites.get(spriteName);

	}

	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {
		// All check the vehicle has to make
	}
}
