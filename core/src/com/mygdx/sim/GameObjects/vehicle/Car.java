package com.mygdx.sim.GameObjects.vehicle;

import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public class Car extends Vehicle {
	private final static String SPRITE_NAME = "car1_red";
	private final static int MAX_SPEED = 150;

	public Car(Node startNode, Node goalNode, Map graph) {
		this(startNode, goalNode, MAX_SPEED, graph);
	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph) {
		super(startNode, goalNode, maxSpeed, SPRITE_NAME, graph);
	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph, Pathfinder pf) {
		super(startNode, goalNode, maxSpeed, SPRITE_NAME, graph, pf);
	}
}
