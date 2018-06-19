package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class Map {
	private boolean DEBUG = true;
	
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	
	private List<Node> destinations = new ArrayList<Node>();
	private List<Node> intersections = new ArrayList<Node>();
	
	// Map padding
	private int mapPadding = 200;
	
	// Maximum map coordinates
	private int mapMaxX;
	private int mapMaxY;
	
	// Minimum map coordinates
	private int mapMinX;
	private int mapMinY;

	HashMap<Edge, ArrayList<ArrayList<Vehicle>>> locationCache;

	// GUI Map Variables
	private Rectangle bounds;

	public Map(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		
		calculateMapDimensions();

		//setIntersections();

		// Temporary hard-coded map bound until we have a save and load feature
		this.reset(mapMinX, mapMinY, mapMaxX, mapMaxY);

		locationCache = new HashMap<Edge, ArrayList<ArrayList<Vehicle>>>();

		for (Edge edge : edges) {
			locationCache.put(edge, new ArrayList<ArrayList<Vehicle>>());
		}
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


	/*public void setIntersections() {
		for(Node node : nodes) {
			int minLanes = Integer.MAX_VALUE;
			for (Edge e : node.getInEdges()) {
				if (e.getNumLanes() < minLanes) {
					minLanes = e.getNumLanes();
				}
			}


			// If a node has 3 or more edges connected to it, we consider it large enough to deserve an intersection
			if(node.getOutgoingNeighbors().size() >= 3 && minLanes >= 3) {
				node.setIntersection(true);
				intersections.add(node);
			}
		}

		for (Node m : intersections){

			// Here, we place stoplights for each "row" of lanes on the node. The node contains multiple stoplights (current build)
			for (Edge e : m.getInEdges()){
				List<Edge> lanes = new ArrayList<Edge>();
				// Going through the list of lanes attached to the node,
				for (int i = 0; i < m.getInEdges().size(); i++) {
					// ... check if the lanes are adjacent (i.e. they have the same "from" node)
					if (e.getFrom().equals(m.getInEdges().get(i).getFrom())){
						// if that edge isn't already in the list of lanes, add it
						if (!lanes.contains(e)) lanes.add(e);
					}
				}
				// Add a light to this node.
				m.addLight(new Stoplight(lanes, m.getLocation()));
				if(DEBUG){
					System.out.println("Stoplight added at: " + m.getLocation() + " with " + e.getNumLanes() + " lanes.");
				}
			}
		}
	}*/
	
	private void calculateMapDimensions() {
		mapMaxX = (int)nodes.get(0).getX();
		mapMaxY = (int)nodes.get(0).getY();
		
		mapMinX = mapMaxX;
		mapMinY = mapMaxY;
		
		for(Node node : nodes) {
			int nodeX = (int)node.getX();
			int nodeY = (int)node.getY();
			
			if(nodeX > mapMaxX) {
				mapMaxX = nodeX;
			}
			
			if(nodeY > mapMaxY) {
				mapMaxY = nodeY;
			}
			
			if(nodeX < mapMinX) {
				mapMinX = nodeX;
			}
			
			if(nodeY < mapMinY) {
				mapMinY = nodeY;
			}
		}
		
		mapMaxX += mapPadding;
		mapMaxY += mapPadding;
		
		mapMinX -= mapPadding;
		mapMinY -= mapPadding;
	}


	public List<Node> getIntersections(){
		return this.intersections;
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

	public List<Node> getDestinations() {
		return destinations;
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

	/*
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(0,10);
		
		Edge edge = new Edge(node1,node2,50);
		
		Map map = new Map(Arrays.asList(node1,node2),Arrays.asList(edge));
		
		System.out.println("Created map");
		
		map.ensureCapacity(10);
		
		int x = 0;
	}
	*/

	public static double euclideanDistance(Node a, Node b){
		return Math.abs(Math.sqrt(Math.pow((a.getY()-b.getY()), 2) + Math.pow((a.getX() - b.getX()), 2)));
	}


	public int getNodeIndex(Node toFind){
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).equals(toFind)) return i;
        }
        return -1;
    }

    public Node getNode(Node toFind){
    	for (Node n : getNodes()){
    		if (n.equals(toFind)) return n;
		}
		return null;
	}

	public Edge getEdge(Edge toFind){
    	for (Edge e : getEdges()){
    		if (e.equals(toFind)) return e;
		}
		return null;
	}

	public Node getDestination(Node toFind){
		for (Node n : getDestinations()){
			if (n.equals(toFind)) return n;
		}
		return null;
	}
}