package com.mygdx.sim.GameObjects.pathfinding;

import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;

public class DynamicPathfinder {
	DistanceMatrixCalculator calculator;
	
	public DynamicPathfinder(Map graph) {
		this.calculator = new DistanceMatrixCalculator(graph);
	}
	
	public Edge selectEdge(Node reached, Node goal) {
		List<Edge> potentialEdges = reached.getOutEdges();
		
		Edge bestEdge = null;
		float bestEdgeScore = Float.MAX_VALUE;
		
		for(int i = 0; i < potentialEdges.size(); i++) {
			Edge currentEdge = potentialEdges.get(i);
			
			int currentEdgeId = currentEdge.getId();
			int goalNodeId = goal.getId();
			
			float currentEdgeScore = calculator.getDistances()[currentEdgeId][goalNodeId];
			
			if(currentEdgeScore < bestEdgeScore) {
				bestEdge = currentEdge;
				bestEdgeScore = currentEdgeScore;
			}
				
		}
		
		return bestEdge;
	}

}
