package com.mygdx.sim.GameObjects.trafficObject.vehicle;

import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public class Car extends Vehicle {
	private final static String SPRITE_NAME = "car1_red";
	private static int MAX_SPEED = 150;

	public Car(Node startNode, Node goalNode, Map graph, int startTimestep) {
		this(startNode, goalNode, MAX_SPEED, graph, startTimestep);

	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph, int startTimestep) {
		super(startNode, goalNode, maxSpeed, randomSprite(), graph, startTimestep);
	}

	public Car(Node startNode, Node goalNode, int maxSpeed, Map graph, Pathfinder pf, int startTimestep) {
		super(startNode, goalNode, maxSpeed, randomSprite(), graph, pf, startTimestep);
	}
	
	public static String randomSprite() {
//		List<String> vehicleSpriteNames = new ArrayList<String>(Resources.world.vehicleSprites.keySet());
		
		return null;
	}

	public int getMaxSpeed(){
		return MAX_SPEED;
	}
}
