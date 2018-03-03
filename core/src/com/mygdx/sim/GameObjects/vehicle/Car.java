package com.mygdx.sim.GameObjects.vehicle;

import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public class Car extends Vehicle {
	private final static String SPRITE_NAME = "car";
	
	public Car(Node startNode, Node goalNode) {
		this(startNode,goalNode,150);
	}
	
	public Car(Node startNode, Node goalNode, int maxSpeed) {
		super(startNode, goalNode, maxSpeed,SPRITE_NAME);
	}
	
	public Car(Node startNode, Node goalNode, int maxSpeed, Pathfinder pf) {
		super(startNode,goalNode,maxSpeed,SPRITE_NAME,pf);		
	}
}
