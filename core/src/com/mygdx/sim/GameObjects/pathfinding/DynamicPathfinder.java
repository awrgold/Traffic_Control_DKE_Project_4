package com.mygdx.sim.GameObjects.pathfinding;

import java.util.Arrays;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;

public class DynamicPathfinder {
	DistanceMatrixCalculator calculator;
	private AStarPathfinder aStarPathfinder;
	private Map graph;
	
	public DynamicPathfinder(Map graph) {
		this.graph = graph;
		this.calculator = new DistanceMatrixCalculator(graph);
		aStarPathfinder = new AStarPathfinder(graph);
	}
	
	public Edge selectEdge(Node reached, Node goal) {
		List<Edge> potentialEdges = reached.getOutEdges();
		
		Edge bestEdge = null;
		float bestEdgeScore = Integer.MAX_VALUE;
		
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
	
	public static void main(String[] args) {
		Node a = new Node(0,0);
		Node b = new Node(100,0);
		Node c = new Node(0,100);
		Node d = new Node(100,100);
		Node e = new Node(-50,50);
		
		Edge ab0 = new Edge(a,b);
		Edge bd1 = new Edge(b,d);
		Edge da2 = new Edge(d,a);
		Edge ac3 = new Edge(a,c);
		Edge ca4 = new Edge(c,a);
		Edge cd5 = new Edge(c,d);
		Edge ea6 = new Edge(e,a);
		
		List<Node> nodes = Arrays.asList(a,b,c,d,e);
		List<Edge> edges = Arrays.asList(ab0,bd1,da2,ac3,ca4,cd5,ea6);
		
		Map map = new Map(nodes,edges);
		
		DynamicPathfinder test = new DynamicPathfinder(map);
		
		Edge shouldBe6 = test.selectEdge(e, c);
		Edge shouldBeNull = test.selectEdge(c, e);
		Edge shouldBe4 = test.selectEdge(c, b);
		
		System.out.println("stop");
	}

}