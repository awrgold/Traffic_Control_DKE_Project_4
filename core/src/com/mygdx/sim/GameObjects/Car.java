package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.PathfinderStrategy;

public class Car extends Vehicle {
	
	public Car(Node startNode, Node goalNode, int maxSpeed) {
		super(startNode, goalNode, maxSpeed);

		// Set Sprite Name
		spriteName = "car";		
	}
	
	public Car(Node startNode, Node goalNode, int maxSpeed, PathfinderStrategy pf) {
		this(startNode,goalNode,maxSpeed);		
		this.pathfinder = pf;
	}
	
	/**
	 * Returns the Edge that this car is located on at the given timestep.
	 * @param timestep
	 * @return the Edge that this car is located on at the given timestep
	 */
	public Edge getEdgeAt(int timestep) {
		return edgePath.get(edgeIndices.get(timestep));
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
