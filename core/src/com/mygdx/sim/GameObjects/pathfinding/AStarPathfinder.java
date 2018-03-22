package com.mygdx.sim.GameObjects.pathfinding;

import java.util.*;
import java.util.PriorityQueue.*;


import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;
import javafx.scene.layout.Priority;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

public class AStarPathfinder extends Pathfinder {

	private boolean DEBUG = true;
	private Comparator<? super Edge> comparator;

	public AStarPathfinder(Map graph) {
		super(graph);
	}


	/**
	 * Not documented yet.
	 * @param map
	 * @param start
	 * @param goal
	 * @return
	 */

	List<Edge> path = new ArrayList<Edge>();

	public void search(Map map, Node start, Node goal){

		if(DEBUG) System.out.println("Start node: " + start.toString() + " | Goal node: " + goal.toString());

		if(DEBUG) System.out.println("______________Running search______________");

		// The set of candidate nodes to be evaluated, starting with the start node
		Stack<Node> open = new Stack<Node>();
		open.push(start);
		Collections.sort(open);

		// The set of nodes already evaluated
		List<Node> closed = new ArrayList<Node>();
		// Create a list of edges we have taken to get to where we are now
		ArrayList<Node> cameFrom = new ArrayList<Node>();
		// Cost So Far: The map of distances from the starting node to nodes explored
		Map costSoFar = new Map(map.getNodes(), map.getEdges());
		// The map of distances from any given node to the goal node
		Map costRemaining = new Map(map.getNodes(), map.getEdges());

		// Initialize the distances in the costs incurred so far as -infinity
		for (Node n : costSoFar.getNodes()){
			n.setNodeDistanceWeight(MIN_VALUE);
		}

		// Initialize the distances in the remaining distances to infinity
		for (Node n : costRemaining.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}

		// Set the manhattan distance from start to goal for the starting node
		costRemaining.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(manhattanDistance(goal, start));

		if (DEBUG) System.out.println("Cost from start to goal: " + costRemaining.getNode(start).getNodeDistanceWeight());

		// Set the distance to the starting node as 0
		costSoFar.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(0);


		// While there are still candidates to explore
		while (!open.isEmpty()){

			if(DEBUG) System.out.println("While loop initiated");

			// Set the current node as the most promising candidate in Open
			Node current = open.pop();
			closed.add(current);

			// If we've reached the goal, return the path that got us there
			if (current.equals(goal)){
				if (DEBUG) System.out.println("Goal found!");
				reconstructPath(cameFrom, current);
				open.empty();
			}

			// For each neighbor of the current node
			for (Node n : current.getOutgoingNeighbors()){

				// Calculate the new cost to reach each neighbor of the current node from the start
				double newCost = costSoFar.getNodes().get(map.getNodeIndex(current)).getNodeDistanceWeight() + manhattanDistance(current, n);
				if (DEBUG) System.out.println("New cost: " + newCost);


				// If the neighbor is not evaluated yet AND newCost is less than the cost to get to the neighbor
				if (open.contains(n) && newCost < costSoFar.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight()) {
					if (DEBUG) System.out.println("Neighbor is removed, other path is better.");

					// Remove neighbor as the new path is better
					open.remove(n);
				}

				// If the neighbor is not yet a candidate and has not been evaluated yet
				if (!closed.contains(n) && !open.contains(n)){

					if (DEBUG) System.out.println("Neighbor has not been evaluated and is not a candidate yet.");

					// Update the cost to reach the neighbor n
					costSoFar.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(newCost);

					// Update the remaining cost from n to goal
					costRemaining.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(newCost + manhattanDistance(goal, n));

					if (DEBUG) System.out.println("Remaining cost from neighbor to goal: " + newCost + manhattanDistance(goal, n));

					// Add n to the candidate list
					open.add(n);
					Collections.sort(open);

					// Set the parent of n as current
					n.setParent(current);
				}
			}

			/*
			open.remove(current);
			closed.add(current);

			for (Node n : current.getOutgoingNeighbors()){

				if (!closed.contains(n) && !open.contains(n)){
					open.add(n);
					n.setParent(current);
				}

				double tempGScore = costRemaining.getNodes().get(map.getNodeIndex(start)).getNodeDistanceWeight() + manhattanDistance(current, n);

				if (tempGScore >= costRemaining.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight()) continue;

				cameFrom.set(map.getNodeIndex(n), current);
				costRemaining.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(tempGScore);
				costSoFar.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(costRemaining.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight() + manhattanDistance(n, goal));
			}
			*/
		}
		if(DEBUG){
			System.out.println("Search completed, node list size: " + cameFrom.size() + ". Nodes in cameFrom: ");
			for (Node n : cameFrom){
				System.out.println("Node: " + n.toString());
			}
		}

	}

	public ArrayList<Edge> edgeSearch(Map map, Node start, Node goal){

		if(DEBUG) System.out.println("Start node: " + start.toString() + " | Goal node: " + goal.toString());

		if(DEBUG) System.out.println("______________Running search______________");

		// The set of candidate nodes to be evaluated, starting with the start node
		Stack<Edge> open = new Stack<Edge>();

		for (Edge e : start.getOutEdges()){
			open.push(e);
			if(DEBUG) System.out.println("Pushed starting edge with length: " + e.getLength());
		}

		// Hoping this works?
		Collections.sort(open);

		// Lazy implementation of a priority queue because I hate comparators without tuples
		// This does not *robustly* sort anything beyond the top element, but we don't need a robust sorting, just a "meh" sorting (for now)


//		for (Edge e : open){
//			if (e.getLength() < open.peek().getLength()){
//				open.push(e);
//				if (DEBUG){
//					System.out.println("Sorting - edge e has length " + e.getLength() + " which is greater than " + open.peek().getLength());
//				}
//			}
//		}

		// The set of nodes already evaluated
		List<Edge> closed = new ArrayList<Edge>();
		// Create a list of edges we have taken to get to where we are now
		ArrayList<Edge> cameFrom = new ArrayList<Edge>();
		// Cost So Far: The map of distances from the starting node to nodes explored
		Map costSoFar = new Map(map.getNodes(), map.getEdges());
		// The map of distances from any given node to the goal node
		Map costRemaining = new Map(map.getNodes(), map.getEdges());




		// Set the manhattan distance from start to goal for the starting node
		costRemaining.getNode(start).setNodeDistanceWeight(manhattanDistance(start, goal));
		if (DEBUG) System.out.println("Cost from start to goal: " + costRemaining.getNode(start).getNodeDistanceWeight());

		// Initialize the distances in the costs incurred so far as -infinity
		for (Node n : costSoFar.getNodes()){
			n.setNodeDistanceWeight(MIN_VALUE);
		}

		// Initialize the distances in the remaining distances to infinity
		for (Node n : costRemaining.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}

		// Set the distance to the starting node as 0
		costSoFar.getNode(start).setNodeDistanceWeight(0);

		if(DEBUG){
			for (Edge e : costSoFar.getEdges()){
				System.out.println("costSoFar edge - " + e.toString() + " - length: " + costSoFar.getEdge(e).getLength());
			}
			for (Edge e : costRemaining.getEdges()){
				System.out.println("costRemaining edge - " + e.toString() + " - length: " + costRemaining.getEdge(e).getLength());
			}
		}


		// While there are still candidates to explore
		while (!open.isEmpty()){

			if(DEBUG) System.out.println("Search initiated");

			// Set the current node as the most promising candidate in Open
			Edge current = open.pop();
			closed.add(current);

			// If we've reached the goal, return the path that got us there
			if (current.getTo().equals(goal)){

				if (DEBUG) System.out.println("Goal found!");
				cameFrom.add(current);
//				reconstructEdgePath(cameFrom, current);
				open.clear();
			}

			// For each neighbor of the current node
			for (Edge e : current.getFrom().getOutEdges()){

				// Calculate the new cost to reach each neighbor of the current node from the start
				double newCost = costSoFar.getEdge(e).getLength() + manhattanDistance(current.getFrom(), e.getTo());
				if (DEBUG) System.out.println("New cost: " + newCost);

				// If the neighbor is not evaluated yet AND newCost is less than the cost to get to the neighbor
				if (open.contains(e) && newCost < costSoFar.getEdge(e).getLength()) {
					if (DEBUG) System.out.println("Neighbor is removed, other path is better.");
					// Remove neighbor as the new path is better
					open.remove(e);
				}

				// If the neighbor is not yet a candidate and has not been evaluated yet
				if (!closed.contains(e) && !open.contains(e)){

					if (DEBUG) System.out.println("Neighbor has not been evaluated and is not a candidate yet.");

					// Update the cost to reach the neighbor n
					costSoFar.getNodes().get(map.getNodeIndex(e.getFrom())).setNodeDistanceWeight(newCost);

					// Update the remaining cost from n to goal
					costRemaining.getNodes().get(map.getNodeIndex(e.getTo())).setNodeDistanceWeight(newCost + manhattanDistance(goal, e.getTo()));

					if (DEBUG) System.out.println("Remaining cost from neighbor to goal: " + newCost + manhattanDistance(goal, e.getTo()));

					// Add n to the candidate list
					open.push(e);
					Collections.sort(open);

					// Set the parent of n as current
					cameFrom.add(current);
				}
			}

		}

		if(DEBUG){
			System.out.println("Search completed, edge list size: " + cameFrom.size() + ". Edges in cameFrom: ");
			for (Edge e : cameFrom){
				System.out.println("Edge from: " + e.getFrom().toString() + " | To: " + e.getTo().toString());
			}
		}
		return cameFrom;
	}

	public ArrayList<Edge> reconstructEdgePath(ArrayList<Edge> cameFrom, Edge current){
		cameFrom.add(current);
		return cameFrom;
	}

	// Reconstruct the shortest path leading to the goal
	public void reconstructPath(ArrayList<Node> cameFrom, Node current){

		// This should be done recursively but I'm like balls deep in DGAF territory right now so...
		for (int i = 0; i < cameFrom.size() - 1; i ++){
			for (Edge e : cameFrom.get(i).getOutEdges()){
				if (e.getTo().equals(cameFrom.get(i+1)) || e.getTo().equals(current)){
					path.add(e);
				}
			}
		}
	}

	public double manhattanDistance(Node a, Node b){
		return Math.abs((a.getY()-b.getY()) + (a.getX()-b.getX()));
	}

	// Return the path
	public List<Edge> findPath(Vehicle vehicle, int timestep) {
//		 search(graph, vehicle.getStartNode(), vehicle.getGoalNode());
//		 return this.path;

		return edgeSearch(graph, vehicle.getStartNode(), vehicle.getGoalNode());
	}



}
