package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Node {

	// Do we need to differentiate between the two edge types?
	private ArrayList<Edge> inEdges;
	private ArrayList<Edge> outEdges;
	private Coordinates location;

	public Node(double xCoordinate, double yCoordinate) {
		location = new Coordinates(xCoordinate, yCoordinate);
	}

	public Node(Coordinates coords) {
		location = coords;
	}

	public Coordinates getLocation() {
		return location;
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public void addInEdge(Edge toAdd) {
		inEdges.add(toAdd);
	}

	public void addOutEdge(Edge toAdd) {
		outEdges.add(toAdd);
	}

	public ArrayList<Edge> getInEdges() {
		return inEdges;
	}

	public ArrayList<Edge> getOutEdges() {
		return outEdges;
	}
}
