package com.mygdx.sim.GameObjects.pathfinding;

import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class AStarPathfinder extends Pathfinder {

	public AStarPathfinder(Map graph) {
		super(graph);
	}

	public List<Edge> findPath(Vehicle vehicle, int timestep) {
		// TODO Auto-generated method stub
		return vehicle.getEdgePath();
	}

}
