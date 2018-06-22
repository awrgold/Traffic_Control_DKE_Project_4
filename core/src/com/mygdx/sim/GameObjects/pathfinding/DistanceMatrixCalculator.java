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

}
