package com.mygdx.sim.GameObjects.trafficObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;

public class SuperSpecialPathfinder {
	
	Map graph;
	
	// From To Over
	// Node Node Edge
	float[][][] distanceMatrix;
	
	public SuperSpecialPathfinder(Map graph) {
		
		this.graph = graph;
		
		initialize();
		
	}
	
	private void initialize() {
		int nodes = graph.getNodes().size();
		
		distanceMatrix = new float[nodes][nodes][];
		
		for(int i = 0; i < nodes; i++) {
			Node nodeI = graph.getNodes().get(i);
			for(int j = 0; j < nodes; j++)
				distanceMatrix[i][j] = new float[nodeI.getOutEdges().size()];	
		}
		
		for(int i = 0; i < distanceMatrix.length; i++)
			for(int j = 0; j < distanceMatrix[i].length; j++)
				for(int k = 0; k < distanceMatrix[i][j].length; k++)
					distanceMatrix[i][j][k] = -1;
		
		crawl();
	}
	
	private void crawlBasic() {
		List<Node> nodes = graph.getNodes();
		
		for(int i = 0; i < nodes.size(); i++) {
			HashSet<Node> foundNodes = new HashSet<Node>();
			
			foundNodes.add(nodes.get(i));
			
			crawlBasic(foundNodes,i);
		}
	}
	
	private void crawlBasic(Set<Node> foundNodes, int currentNodeID) {
		
	}
	
	private void crawl() {
		for(int i = 0; i < graph.getNodes().size(); i++) {
			Node fromNode = graph.getNodes().get(i);
			
			for(int j = 0; j < fromNode.getOutEdges().size(); j++) {
				Edge edge = fromNode.getOutEdges().get(j);
				Node toNode = edge.getTo();
				
				update(i,toNode.getId(),j,edge.getLength());
			}
		}
	}
	
	private void crawl(ArrayList<Integer> crawledNodes, int beingCrawledId) {
		Node nodeBeingCrawled = graph.getNodes().get(beingCrawledId);
		
		crawledNodes.add(beingCrawledId);
		
		for(int i = 0; i < nodeBeingCrawled.getOutEdges().size(); i++) {
			Node nextNode = nodeBeingCrawled.getOutEdges().get(i).getTo();
			int nextNodeId = nextNode.getId();
			
			crawl(crawledNodes,nextNodeId);
		}
	}
	
	private void update(int fromNode, int toNode, int edgeID, float newValue) {
		if(distanceMatrix[fromNode][toNode][edgeID] == -1 || newValue < distanceMatrix[fromNode][toNode][edgeID])
			distanceMatrix[fromNode][toNode][edgeID] = newValue;
	}
}
