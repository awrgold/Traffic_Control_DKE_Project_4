package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.PathfinderStrategy;

public class Car extends Vehicle {

	/**
	 * The edges that this car is supposed to travel from its start point
	 * to its endpoint.
	 */
	List<Edge> edgesToTravel;
	
	/**
	 * Stores for each timestep, the index in edgesToTravel of the edge
	 * that this car is located at.
	 * 
	 * If we do edgesToTravel.get(edgeIndices.get(t)), we should get
	 * the edge where this car is located at timestep t.
	 */
	ArrayList<Integer> edgeIndices = new ArrayList<Integer>();
	
	/**
	 * Stores for each timestep, the distance that this car has traveled
	 * on the edge it is located on.
	 * 
	 * If we have a 10-long edge, and we're moving at 3 per second:
	 * 0 3 6 9 2 5 8 ...
	 */
	ArrayList<Double> distancesTraveledOnEdge = new ArrayList<Double>();
	
	/**
	 * Stores for each timestep, the speed that this car was traveling at.
	 */
	ArrayList<Double> speeds = new ArrayList<Double>();
	
	/**
	 * Stores the algorithm that this car uses for navigation/pathfinding.
	 */
	PathfinderStrategy pathfinder = new AStarPathfinder();

	public Car(Node startNode, Node goalNode, int maxSpeed) {
		super(startNode, goalNode);

		// Set Max Speed
		this.maxSpeed = maxSpeed;

		// Set Sprite Name
		spriteName = "car";
	}
	
	/**
	 * Returns the Edge that this car is located on at the given timestep.
	 * @param timestep
	 * @return the Edge that this car is located on at the given timestep
	 */
	public Edge getEdgeAt(int timestep) {
		return edgesToTravel.get(edgeIndices.get(timestep));
	}
	
	/**
	 * Returns the Coordinates that this car is located on at the given timestep.
	 * @param timestep
	 * @return the Coordinates that this car is located on at the given timestep
	 */
	public Coordinates getLocationCoordinates(int timestep) {	
		Edge edge = getEdgeAt(timestep);
		double distance = distancesTraveledOnEdge.get(timestep);
		return edge.getLocationIfTraveledDistance(distance);
	}



}
