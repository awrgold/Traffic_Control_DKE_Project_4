package com.mygdx.sim.GameObjects.trafficObject.vehicle;

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
		List<String> vehicleSpriteNames = new ArrayList<String>(Resources.world.vehicleSprites.keySet());
		
		return vehicleSpriteNames.get((new Random()).nextInt(vehicleSpriteNames.size()));
	}

	public int getMaxSpeed(){
		return MAX_SPEED;
	}
}
