package com.mygdx.sim.GameObjects.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;

public class DistanceMatrixCalculator {
	
	//Edge - Node
	float[][] distances;
	
	Map graph;
	
	Pathfinder astar;
	
	public DistanceMatrixCalculator(Map graph) {
		this.graph = graph;
		
		distances = new float[graph.getEdges().size()][graph.getNodes().size()];
		
		for(int i = 0; i < distances.length; i++)
			for(int j = 0; j < distances[i].length; j++)
				distances[i][j] = Integer.MAX_VALUE;
		
		astar = new AStarPathfinder(graph);
		
		crawl();
	}
	
	private List<Edge> getEdges() { return graph.getEdges(); }
	private List<Node> getNodes() { return graph.getNodes(); }
	
	private void crawl() {
		for(int i = 0; i < getEdges().size(); i++) {
			int toNode = getEdges().get(i).getTo().getId();
			
			update(i, toNode, getEdges().get(i).getLength());
		}
		
		for(int i = 0; i < getEdges().size(); i++)
			for(int j = 0; j < getEdges().size(); j++)
				for(int k = 0; k < getNodes().size(); k++)					
					if(distances[i][k] > distances[i][getEdges().get(j).getFrom().getId()] + distances[j][k])
						distances[i][k] = distances[i][getEdges().get(j).getFrom().getId()] + distances[j][k];
		

	}
	
	private void update(int edgeId, int toNodeId, float distance) {
		if(distance < distances[edgeId][toNodeId])
			distances[edgeId][toNodeId] = distance;
	}
	
	public float[][] getDistances(){ return distances; }
	
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
		
		DistanceMatrixCalculator test = new DistanceMatrixCalculator(map);
		
		System.out.println("stop");
	}

}
