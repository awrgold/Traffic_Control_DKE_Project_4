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

		// The set of candidate nodes to be evaluated, starting with the start node
		Stack<Node> open = new Stack<Node>();
		open.push(start);
		// The set of nodes already evaluated
		List<Node> closed = new ArrayList<Node>();
		// Create a list of edges we have taken to get to where we are now
		ArrayList<Node> cameFrom = new ArrayList<Node>();
		// Cost So Far: The map of distances from the starting node to nodes explored
		Map costSoFar = new Map(map.getNodes(), map.getEdges());
		// The map of distances from any given node to the goal node
		Map costRemaining = new Map(map.getNodes(), map.getEdges());

		// Set the g and f scores to max/min value (have not been evaluated yet)
		for (Node n : costSoFar.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}

		// Set the manhattan distance from start to goal for the starting node
		costRemaining.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(manhattanDistance(goal, start));

		// Set the distance to the starting node as 0
		costSoFar.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(0);

		// Initialize the distances in gScore to infinity
		for (Node n : costRemaining.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}

		while (!open.isEmpty()){

			// Lazy implementation of a priority queue because I hate comparators without tuples
			// This does not *robustly* sort anything beyond the top element, but we don't need a robust sorting, just a "meh" sorting (for now)
			for (Node n : open){
				if (!n.equals(goal) && (n.getNodeDistanceWeight() < open.peek().getNodeDistanceWeight()) ){
					Node temp = n;
					open.remove(n);
					open.push(temp);
				}
			}

			// Set the current node as the most promising candidate in Open
			Node current = open.pop();
			closed.add(current);

			// If we've reached the goal, return the path that got us there
			if (current.equals(goal)) reconstructPath(cameFrom, current);

			// For each neighbor of the current node
			for (Node n : current.getOutgoingNeighbors()){

				// Calculate the new cost to reach each neighbor of the current node from the start
				double newCost = costSoFar.getNodes().get(map.getNodeIndex(current)).getNodeDistanceWeight() + manhattanDistance(current, n);

				// If the neighbor is not evaluated yet OR newCost is less than the cost to get to the neighbor
				if (open.contains(n) && newCost < costSoFar.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight()) {

					// Remove neighbor as the new path is better
					open.remove(n);
				}

				if (!closed.contains(n) && !open.contains(n)){

					// Update the cost to reach the neighbor n
					costSoFar.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(newCost);

					// Update the remaining cost from n to goal
					costRemaining.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(newCost + manhattanDistance(goal, n));

					// Add n to the candidate list
					open.add(n);

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

	}

	// Not working yet
	public void reconstructPath(ArrayList<Node> cameFrom, Node current){

		for (int i = 0; i < cameFrom.size() - 1; i ++){
			for (Edge e : cameFrom.get(i).getOutEdges()){
				if (e.getTo().equals(cameFrom.get(i+1)) || e.getTo().equals(current)){
					path.add(e);
				}
			}
		}
	}

	public double manhattanDistance(Node a, Node b){
		return Math.abs((a.getX()-b.getX()) - (a.getY()-b.getY()));
	}

	// Not working yet
	public List<Edge> findPath(Vehicle vehicle, int timestep) {
		search(graph, vehicle.getStartNode(), vehicle.getGoalNode());
		return this.path;
	}



}
