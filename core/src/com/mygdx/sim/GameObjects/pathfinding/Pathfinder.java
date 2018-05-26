package com.mygdx.sim.GameObjects.pathfinding;

import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public abstract class Pathfinder {
	public Map graph;
	
	/**
	 * Finds a path for the given vehicle after the given timestep.
	 * !!!DO  NOT !!! UNDER ANY CIRCUMSTANCE !!! change parts of the
	 * edgePath that the vehicle has already traveled!
	 * @param vehicle - vehicle for which we are pathfinding
	 * @param timestep - timestep before which we are not allowed to change the path
	 * @return new path
	 */
	public abstract List<Edge> findPath(Vehicle vehicle, int timestep, boolean findDifferentPathOnFail);

//	public boolean canFindPath(Vehicle vehicle, int timestep){
//		if (findPath(vehicle, timestep).size() > 0){
//			return true;
//		}
//		return false;
//	}

	public Pathfinder(Map graph) {
		this.graph = graph;
	}

}
