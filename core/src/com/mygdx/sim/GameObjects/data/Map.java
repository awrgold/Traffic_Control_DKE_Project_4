package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class Map {
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();

	HashMap<Edge, ArrayList<ArrayList<Vehicle>>> locationCache;

	// GUI Map Variables
	public static final int TILE_SIZE = 32;
	private Rectangle bounds;

	public Map(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;

		// Temporary hardcoded map bound until we have a save and load feature
		this.reset(10, 10);

		locationCache = new HashMap<Edge, ArrayList<ArrayList<Vehicle>>>();

		for (Edge edge : edges)
			locationCache.put(edge, new ArrayList<ArrayList<Vehicle>>());
	}

	public ArrayList<Vehicle> getVehiclesAt(Edge edge, int timestep) {
		return locationCache.get(edge).get(timestep);
	}

	public void ensureCapacity(int timestep) {
		for (Edge edge : edges) {
			ArrayList<ArrayList<Vehicle>> history = locationCache.get(edge);

			while (history.size() <= timestep)
				history.add(new ArrayList<Vehicle>());
		}
	}

	public HashMap<Edge, ArrayList<ArrayList<Vehicle>>> getLocationCache() {
		return locationCache;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void reset(int columns, int rows) {
		// Init Map Bounds
		bounds = new Rectangle(0f, 0f, Map.TILE_SIZE * columns, Map.TILE_SIZE * rows);

	}

	public Rectangle getBounds() {
		return bounds;
	}

    public int getNodeIndex(Node toFind){
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).equals(toFind)) return i;
        }
        return -1;
    }
}
