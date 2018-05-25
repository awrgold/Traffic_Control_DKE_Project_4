package com.mygdx.sim.GameObjects.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;
import com.mygdx.sim.Resources.Resources;

public class Car extends Vehicle {
	private final static String SPRITE_NAME = "car1_red";
	private static int MAX_SPEED = 150;

	public Car(Node startNode, Node goalNode, Map graph) {
		this(startNode, goalNode, MAX_SPEED, graph);

	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph) {
		super(startNode, goalNode, maxSpeed, randomSprite(), graph);
	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph, Pathfinder pf) {
		super(startNode, goalNode, maxSpeed, randomSprite(), graph, pf);
	}
	
	public static String randomSprite() {
//		List<String> vehicleSpriteNames = new ArrayList<String>(Resources.world.vehicleSprites.keySet());
		
		return null;
	}

	public int getMaxSpeed(){
		return MAX_SPEED;
	}
}
