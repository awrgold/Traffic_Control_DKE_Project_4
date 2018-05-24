package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.Intersection;
import com.mygdx.sim.GameObjects.IntersectionSingle;
import com.mygdx.sim.GameObjects.Stoplight;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class Map {
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private List<Node> destinations = new ArrayList<Node>();
	private List<Node> intersections = new ArrayList<Node>();
	private List<IntersectionSingle> intersectionObjects;
	private boolean DEBUG = true;

	HashMap<Edge, ArrayList<ArrayList<Vehicle>>> locationCache;

	// GUI Map Variables
	private Rectangle bounds;

	public Map(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		
		setIntersections();

		// Temporary hardcoded map bound until we have a save and load feature
		this.reset(TrafficManager.MAP_X_DIM, TrafficManager.MAP_Y_DIM);

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

			while (history.size() <= timestep)
				history.add(new ArrayList<Vehicle>());
		}
	}
	
	public void setIntersections() {
		for(Node node : nodes) {
			// If a node has 3 or more edges connected to it, we consider it large enough to deserve an intersection
			if(node.getOutgoingNeighbors().size() >= 3) {
				node.setIntersection(true);

				// Here, we place stoplights for each "row" of lanes on the node. The node contains multiple stoplights (current build)
				for (Edge e : node.getInEdges()){
					List<Edge> lanes = new ArrayList<Edge>();
					// Going through the list of lanes attached to the node,
					for (int i = 0; i < node.getInEdges().size(); i++) {
						// ... check if the lanes are adjacent (i.e. they have the same "from" node)
						if (e.getFrom().equals(node.getInEdges().get(i).getFrom())){
							// if that edge isn't already in the list of lanes, add it
							if (!lanes.contains(e)) lanes.add(e);
						}
					}
					// Add a light to this node.
					node.addLight(new Stoplight(lanes, node.getLocation()));
					if(DEBUG){
						System.out.println("Stoplight " + node.getType() + " - added at: " + node.getLocation() + " with " + lanes.size() + " edges.");
					}
				}

			}
		}
	}

	public List<Node> getIntersections(){
		return this.intersections;
	}

	public List<IntersectionSingle> getIntersectionObjects(){
		return this.intersectionObjects;
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

	public void reset(int maxX, int maxY) {
		// Init Map Bounds
		bounds = new Rectangle(0f, 0f, maxX, maxY);

	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public String toString() {
		return "[Map]";
	}
	
	public static void main(String[] args) {
		Node node1 = new Node(0,0);
		Node node2 = new Node(0,10);
		
		Edge edge = new Edge(node1,node2,50);
		
		Map map = new Map(Arrays.asList(node1,node2),Arrays.asList(edge));
		
		System.out.println("Created map");
		
		map.ensureCapacity(10);
		
		int x = 0;
	}

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
