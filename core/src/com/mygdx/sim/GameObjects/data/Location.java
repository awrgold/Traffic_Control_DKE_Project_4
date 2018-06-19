package com.mygdx.sim.GameObjects.data;

public class Location {
	private Edge edge;
	private float distanceOnEdge;
	
	public Location(Edge edge, float distanceOnEdge) {
		this.edge = edge;
		this.distanceOnEdge = distanceOnEdge;
	}
	
	public Edge getEdge() {
		return edge;
	}
	
	public float getDistanceOnEdge() {
		return distanceOnEdge;
	}
}
