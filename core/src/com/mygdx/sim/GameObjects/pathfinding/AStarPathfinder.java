package com.mygdx.sim.GameObjects.pathfinding;

import static java.lang.Double.MAX_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

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
	public Map search(Map map, Node start, Node goal){

		// The set of nodes already evaluated
		Stack<Node> open = new Stack<Node>();
		// The set of nodes not yet explored
		List<Node> closed = new ArrayList<Node>();
		closed.addAll(map.getNodes());
		// Create a list of edges we have taken to get to where we are now
		List<Node> cameFrom = new ArrayList<Node>();
		// The map of distances from the starting node to any other node
		Map fScore = new Map(map.getNodes(), map.getEdges());
		// The map of distances from any given node to the goal node
		Map gScore = new Map(map.getNodes(), map.getEdges());

		// Set the g and f scores to max/min value (have not been evaluated yet)
		for (Node n : fScore.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}
		gScore.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(0);
		fScore.getNodes().get(map.getNodeIndex(start)).setNodeDistanceWeight(manhattanDistance(goal, start));
		for (Node n : gScore.getNodes()){
			n.setNodeDistanceWeight(MAX_VALUE);
		}

		while (!open.isEmpty()){

			// Lazy implementation of a priority queue
			for (Node n : open){
				if (n.getNodeDistanceWeight() < open.peek().getNodeDistanceWeight()){
					Node temp = n;
					open.remove(n);
					open.push(temp);
				}
			}

			Node current = open.peek();
			if (current.equals(goal)) return reconstructPath(cameFrom, current);

			open.remove(current);
			closed.add(current);

			for (Node n : current.getOutgoingNeighbors()){

				if (!closed.contains(n)){
					open.add(n);
				}

				double tempGScore = gScore.getNodes().get(map.getNodeIndex(start)).getNodeDistanceWeight() + manhattanDistance(current, n);

				if (tempGScore >= gScore.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight()) continue;

				cameFrom.set(map.getNodeIndex(n), current);
				gScore.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(tempGScore);
				fScore.getNodes().get(map.getNodeIndex(n)).setNodeDistanceWeight(gScore.getNodes().get(map.getNodeIndex(n)).getNodeDistanceWeight() + manhattanDistance(n, goal));
			}
		}
		return null;
	}

	// Not working yet
	public Map reconstructPath(List cameFrom, Node current){
		Map path = new Map(new ArrayList<Node>(), new ArrayList<Edge>());


		return path;
	}

	public double manhattanDistance(Node a, Node b){
		return Math.abs((a.getX()-b.getX()) - (a.getY()-b.getY()));
	}

	// Not working yet

	public List<Edge> findPath(Vehicle vehicle, int timestep) {
		// TODO Auto-generated method stub

		return vehicle.getEdgePath();
	}



}
