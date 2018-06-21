package com.mygdx.sim.GameObjects.pathfinding;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class AStarPathfinder extends Pathfinder {

	private boolean DEBUG = true;

	public AStarPathfinder(Map graph) {
		super(graph);
	}

	/**
	 * A* is a large gaseous stellar body that, through nuclear fusion of hydrogen
	 * or helium, emits electromagnetic radiation in the form of light that creates
	 * beautiful little prickly dots in the night sky, not unlike the prickly dots
	 * all over my body that came about during a lusty, peyote-fueled sexual tumble
	 * with a cactus. Turns out that I hallucinate small asian men offering
	 * acupuncture while tripping balls in the Sonora Desert. Don't do drugs, kids.
	 * 
	 * @return
	 */

	// TODO: Fix A* to remove loopy paths (12 June)

	public List<Edge> searchPath(List<Node> nodes, Vehicle vehicle) {

		// Start/Goal Nodes
		Node start = vehicle.getStartNode();
		Node goal = vehicle.getGoalNode();

		// Initialise priority queue
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();

		// Set all other weights to infinity
		for (Node node : nodes) {
			node.setNodeDistanceWeight(MAX_VALUE);
			node.setNodeDistanceWeightEstimate(MAX_VALUE);
		}

		// Set start node weight to 0
		start.setNodeDistanceWeight(0);

		// Add start node to queue
		priorityQueue.add(start);

		// While the queue is not empty
		while (!priorityQueue.isEmpty()) {

			Node currentNode = priorityQueue.poll();

			// If we have found the goal
			if (currentNode.equals(goal)) {
				return createPath(goal);
			}

			// Loop through all outgoing edges
			for (Edge edge : currentNode.getOutEdges()) {
				Node nextNode = null;
				nextNode = edge.getTo();
				
				for(Edge toEdge : edge.getToEdges()) {
					if(edge.getTo().equals(toEdge.getFrom())) {
						nextNode = edge.getTo();
						break;
					}
				}
				
				if(nextNode == null) {
					continue;
				}

				// The cost is the cost from travelling to the next node (Edge length / speed limit) -> faster roads have lower weight.
				double newCost = currentNode.getNodeDistanceWeight() + (edge.getWeight());

				// If new cost is lower than the currently set cost
				if (newCost < nextNode.getNodeDistanceWeight()) {
					nextNode.setNodeDistanceWeight(newCost);
					nextNode.setPreviousNode(currentNode);
					// The estimated weight is the cost from travelling to the next edge + Manhattan distance
					nextNode.setNodeDistanceWeightEstimate(newCost + manhattanDistance(currentNode, nextNode));

					priorityQueue.add(nextNode);
				}
			}
		}

		return null;
	}

	public List<Edge> createPath(Node node) {

		List<Edge> path = new ArrayList<Edge>();

		// Construct Path from Goal to Start
		while (node.getPreviousNode() != null) {
			Node previousNode = node.getPreviousNode();

			for (Edge edge : previousNode.getOutEdges()) {
				if (edge.getTo().equals(node)) {
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

	public double manhattanDistance(Node a, Node b) {
		return Math.abs((a.getY() - b.getY()) + (a.getX() - b.getX()));
	}

	// Return the path
	public List<Edge> findPath(Vehicle vehicle, int timestep) {
		return searchPath(graph.getNodes(), vehicle);
	}

}
