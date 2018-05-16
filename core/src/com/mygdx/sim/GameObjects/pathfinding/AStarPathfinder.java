package com.mygdx.sim.GameObjects.pathfinding;

import static java.lang.Double.MAX_VALUE;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Double.MIN_VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class AStarPathfinder extends Pathfinder {

	private boolean DEBUG = true;

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
	public List<Edge> searchPath(List<Node> nodes, Node start, Node goal) {
		
		// Initialize priority queue
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();

		// Set all other weights to infinity
		for(Node node : nodes) {
			node.setNodeDistanceWeight(MAX_VALUE);
			node.setNodeDistanceWeightEstimate(MAX_VALUE);
		}
		
		// Set start node weight to 0
		start.setNodeDistanceWeight(0);

		// Add start node to queue
		priorityQueue.add(start);
		
		// While the queue is not empty
		while(!priorityQueue.isEmpty()) {
			
			Node currentNode = priorityQueue.poll();
			
			// If we have found the goal
			if(currentNode.equals(goal)) {
				return createPath(goal);
			}
			
			// Loop through all outgoing edges
			for(Edge edge : currentNode.getOutEdges()) {
				Node nextNode = edge.getTo();
				
				// The cost is the cost from traveling to the next node (Edge length)
				double newCost = currentNode.getNodeDistanceWeight() + edge.getLength();
				
				// If new cost is lower than the currently set cost
				if(newCost < nextNode.getNodeDistanceWeight()) {
					nextNode.setNodeDistanceWeight(newCost);
					nextNode.setPreviousNode(currentNode);
					// The estimated weight is the cost from traveling to the next edge + manhattan distance
					nextNode.setNodeDistanceWeightEstimate(newCost + manhattanDistance(currentNode, nextNode));
					
					priorityQueue.add(nextNode);
				}
			}
		}
		
		throw new NullPointerException("No path found");
	}
	
	public List<Edge> createPath(Node node) {
		
		List<Edge> path = new ArrayList<Edge>();
		
		// Construct Path from Goal to Start
		while(node.getPreviousNode() != null) {
			Node previousNode = node.getPreviousNode();
			
			for(Edge edge : previousNode.getOutEdges()) {
				if(edge.getTo().equals(node)) {
					path.add(edge);

					break;
				}
			}
			node.setPreviousNode(null);
			node = previousNode;
		}
		
		// Reverse List so List goes from Start to Goal
		Collections.reverse(path);
		
		return path;
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


	public double manhattanDistance(Node a, Node b){
		return Math.abs((a.getY()-b.getY()) + (a.getX()-b.getX()));
	}

	// Return the path
	public List<Edge> findPath(Vehicle vehicle, int timestep) {

		System.out.println("Path size for: " + vehicle.toString() + " = " + searchPath(graph.getNodes(), vehicle.getStartNode(), vehicle.getGoalNode()).size());
		return searchPath(graph.getNodes(), vehicle.getStartNode(), vehicle.getGoalNode());
	}



}
