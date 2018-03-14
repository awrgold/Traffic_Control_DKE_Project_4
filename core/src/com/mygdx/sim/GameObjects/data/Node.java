package com.mygdx.sim.GameObjects.data;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Float.MAX_VALUE;

public class Node {

	private ArrayList<Edge> inEdges = new ArrayList<Edge>();
	private ArrayList<Edge> outEdges = new ArrayList<Edge>();
	private Coordinates location;
	private double nodeDistanceWeight;

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
	
	public String toString() {
		return "[Node@"+location+"]";
	}
}
