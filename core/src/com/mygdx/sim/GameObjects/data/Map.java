package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.Stoplight;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class Map {

	private boolean DEBUG = false;

	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private List<Node> spawnPoints = new ArrayList<Node>();

	private List<Node> destinations = new ArrayList<Node>();
	private List<Node> intersections = new ArrayList<Node>();
	private List<Stoplight> lights = new ArrayList<Stoplight>();

	// Map padding
	private int mapPadding = 200;

	// Maximum map coordinates
	private int mapMaxX;
	private int mapMaxY;

	// Minimum map coordinates
	private int mapMinX;
	private int mapMinY;

	HashMap<Edge, ArrayList<ArrayList<Vehicle>>> locationCache;
	HashMap<Edge, ArrayList<TrafficObject>> staticTrafficObjectsCache;

	// GUI Map Variables
	private Rectangle bounds;

	public Map(List<Node> nodes, List<Edge> edges) {

		List<Node> tempNodes = new ArrayList<Node>();
		List<Edge> tempEdges = new ArrayList<Edge>();

		// Put the nodes and edges in the correct index based on their ID
		for (int i = 0; i < nodes.size(); i++) {
			tempNodes.add(null);
		}
		for (int i = 0; i < edges.size(); i++) {
			tempEdges.add(null);
		}

		for (Node n : nodes) {
			tempNodes.set(n.getId(), n);
			if (DEBUG) {
				System.out.println("Node " + n.getId() + " placed at " + tempNodes.get(n.getId()).getId());
			}
		}
		for (Edge e : edges) {
			tempEdges.set(e.getId(), e);
			if (DEBUG) {
				System.out.println("Edge " + e.getId() + " placed at " + tempEdges.get(e.getId()).getId());
			}
		}

		this.nodes = tempNodes;
		this.edges = tempEdges;
		setSpawnPoints();
		setDestinations();
		setTrafficLights();

		calculateMapDimensions();

		// setIntersections();

		// Temporary hard-coded map bound until we have a save and load feature
		this.reset(mapMinX, mapMinY, mapMaxX, mapMaxY);

		locationCache = new HashMap<Edge, ArrayList<ArrayList<Vehicle>>>();

		for (Edge edge : edges) {
			locationCache.put(edge, new ArrayList<ArrayList<Vehicle>>());
		}

		staticTrafficObjectsCache = new HashMap<Edge, ArrayList<TrafficObject>>();

		for (Edge edge : edges) {
			staticTrafficObjectsCache.put(edge, new ArrayList<TrafficObject>());
		}

		// throw new runtime exception if ID doesn't match index
		checkIDs();

	}

	public ArrayList<Vehicle> getVehiclesAt(Edge edge, int timestep) {
		return locationCache.get(edge).get(timestep);
	}

	public void ensureCapacity(int timestep) {
		for (Edge edge : edges) {
			ArrayList<ArrayList<Vehicle>> history = locationCache.get(edge);

			while (history.size() <= timestep) {
				history.add(new ArrayList<Vehicle>());
			}
		}
	}

	public List<Node> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints() {
		for (Node n : nodes) {
			if ((n.getXmlID().contains("east") || n.getXmlID().contains("west") || n.getXmlID().contains("south") || n.getXmlID().contains("north")) && !n.getXmlID().contains("-out") && !n.getXmlID().contains("light")) {
				spawnPoints.add(n);
			}
		}
	}

	public void setDestinations() {
		for (Node n : nodes) {
			if (n.getXmlID().contains("east-out") || n.getXmlID().contains("west-out") || n.getXmlID().contains("north-out") || n.getXmlID().contains("south-out")) {
				destinations.add(n);
			}
		}
		System.out.println("There are " + destinations.size() + " elements in destinations");
	}
	
	public List<Stoplight> getTrafficLights() {
		return lights;
	}

	public void setTrafficLights() {
		for (Node n : nodes) {
			if (n.getXmlID().contains("light")) {
				lights.add(new Stoplight(n));
			}
		}
	}

	public void checkIDs() {
		for (int i = 0; i < nodes.size(); i++) {
			if (i != nodes.get(i).getId()) {
				throw new RuntimeException("Node ID is not Index ID");
			}
		}
		for (int i = 0; i < edges.size(); i++) {
			if (i != edges.get(i).getId()) {
				throw new RuntimeException("Edge ID is not Index ID");
			}
		}
	}

	private void calculateMapDimensions() {
		mapMaxX = (int) nodes.get(0).getX();
		mapMaxY = (int) nodes.get(0).getY();

		mapMinX = mapMaxX;
		mapMinY = mapMaxY;

		for (Node node : nodes) {
			int nodeX = (int) node.getX();
			int nodeY = (int) node.getY();

			if (nodeX > mapMaxX) {
				mapMaxX = nodeX;
			}

			if (nodeY > mapMaxY) {
				mapMaxY = nodeY;
			}

			if (nodeX < mapMinX) {
				mapMinX = nodeX;
			}

			if (nodeY < mapMinY) {
				mapMinY = nodeY;
			}
		}

		mapMaxX += mapPadding;
		mapMaxY += mapPadding;

		mapMinX -= mapPadding;
		mapMinY -= mapPadding;
	}

	public List<Node> getIntersections() {
		return this.intersections;
	}

	public HashMap<Edge, ArrayList<ArrayList<Vehicle>>> getLocationCache() {
		return locationCache;
	}

	public HashMap<Edge, ArrayList<TrafficObject>> getStaticTrafficObjectsCache() {
		return staticTrafficObjectsCache;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Node> getDestinations() {
		return destinations;
	}

	public List<Stoplight> getLights() {
		return lights;
	}

	public void reset(int minX, int minY, int maxX, int maxY) {
		// Initialise Map Bounds
		bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public String toString() {
		return "[Map]";
	}

	public static double euclideanDistance(Node a, Node b) {
		return Math.abs(Math.sqrt(Math.pow((a.getY() - b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}

	public int getNodeIndex(Node toFind) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).equals(toFind))
				return i;
		}
		return -1;
	}

	public Node getNode(Node toFind) {
		for (Node n : getNodes()) {
			if (n.equals(toFind))
				return n;
		}
		return null;
	}

	public Edge getEdge(Edge toFind) {
		for (Edge e : getEdges()) {
			if (e.equals(toFind))
				return e;
		}
		return null;
	}

	public Node getDestination(Node toFind) {
		for (Node n : getDestinations()) {
			if (n.equals(toFind))
				return n;
		}
		return null;
	}
}