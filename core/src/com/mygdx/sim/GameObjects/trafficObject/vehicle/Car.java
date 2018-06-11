package com.mygdx.sim.GameObjects.trafficObject.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.driverModel.DriverModel;
import com.mygdx.sim.GameObjects.driverModel.IntelligentDriverModelPlus;
import com.mygdx.sim.GameObjects.pathfinding.AStarPathfinder;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;
import com.mygdx.sim.Resources.Resources;

public class Car extends Vehicle {
	
	private final static String SPRITE_NAME = "car1_red";
	private final static int MAX_SPEED = 150;
	private final static DriverModel DRIVER_MODEL = new IntelligentDriverModelPlus();
	
	public static class Builder {
		int maxSpeed = MAX_SPEED;
		String spriteName = SPRITE_NAME;
		DriverModel driverModel = DRIVER_MODEL;
		
		final Node startNode;
		final Node goalNode;
		final Map graph;
		
		int startTimestep = 0;
		
		Pathfinder pf = null;
		
		public Builder(Node startNode, Node goalNode, Map graph) {
			this.startNode = startNode;
			this.goalNode = goalNode;
			this.graph = graph;
		}
		
		public Builder setMaxSpeed(int maxSpeed) {
			this.maxSpeed = maxSpeed;
			return this;
		}
		
		public Builder setSpriteName(String spriteName) {
			this.spriteName = spriteName;
			return this;
		}
		
		public Builder setStartTimestep(int startTimestep) {
			this.startTimestep = startTimestep;
			return this;
		}
		
		public Builder setDriverModel(DriverModel driverModel) {
			this.driverModel = driverModel;
			return this;
		}
		
		public Car build() {
			if(pf == null) 
				pf = new AStarPathfinder(graph);
			
			return new Car(startNode,goalNode,maxSpeed,graph,pf,startTimestep,driverModel);
		}
	}

	private Car(Node startNode, Node goalNode, int maxSpeed, Map graph, Pathfinder pf, int startTimestep, DriverModel driverModel) {
		super(startNode, goalNode, maxSpeed, randomSprite(), pf, graph, startTimestep,driverModel);
	}
	
	public static String randomSprite() {
		List<String> vehicleSpriteNames = new ArrayList<String>(Resources.world.vehicleSprites.keySet());
		
		return vehicleSpriteNames.get((new Random()).nextInt(vehicleSpriteNames.size()));
	}

	public int getMaxSpeed(){
		return MAX_SPEED;
	}
}
