package com.mygdx.sim.GameObjects.vehicle;

import com.mygdx.sim.GameObjects.data.Graph;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public class Car extends Vehicle {
	private final static String SPRITE_NAME = "car";
	private final static int MAX_SPEED = 150;
	
	public Car(Node startNode, Node goalNode, Graph graph) {
		this(startNode,goalNode,MAX_SPEED,graph);
	}
	
	public Car(Node startNode, Node goalNode, int maxSpeed, Graph graph) {
		super(startNode, goalNode, maxSpeed,SPRITE_NAME,graph);
	}
	
	public Car(Node startNode, Node goalNode, int maxSpeed, Graph graph, Pathfinder pf) {
		super(startNode,goalNode,maxSpeed,SPRITE_NAME,graph,pf);		
	}
}
