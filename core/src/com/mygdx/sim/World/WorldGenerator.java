package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.roads.Road;

public class WorldGenerator {
	
	// World Controller
	private WorldController worldController;
	
	// Roads
	List<Road> roads;
	
	public WorldGenerator(WorldController worldController) {
		roads = new ArrayList<Road>();
		
		generateRoads(worldController.getNodes());
	}
	
	public void generateRoads(List<Node> nodes) {
		for(Node node : nodes) {
			for(Node nodeOutgoing : node.getOutgoingNeighbors()) {
				roads.add(new Road(new Coordinates(node.getX(), node.getY()), new Coordinates(nodeOutgoing.getX(), nodeOutgoing.getY()), "road"));
			}
		}
	}

	public List<Road> getRoads() {
		return roads;
	}
}
