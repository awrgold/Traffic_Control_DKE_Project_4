package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.roads.Road;

public class WorldGenerator {

	// World Controller
	private WorldController worldController;

	// Roads
	List<Road> roads;

	public WorldGenerator(WorldController worldController) {
		this.worldController = worldController;

		roads = new ArrayList<Road>();

		generateRoads(worldController.getEdges());
	}

	public void generateRoads(List<Edge> edges) {
		for (Edge edge : edges) {
			roads.add(new Road(edge, "road"));
		}
	}

	public List<Road> getRoads() {
		return roads;
	}
}
