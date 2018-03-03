package com.mygdx.sim.GameObjects.pathfinding;

import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Graph;
import com.mygdx.sim.GameObjects.data.Node;

public interface Pathfinder {
	
	public List<Edge> findPath(Graph graph, Node start, Node end);

}