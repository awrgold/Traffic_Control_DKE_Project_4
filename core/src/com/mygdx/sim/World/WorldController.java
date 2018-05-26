package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.roads.Road;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

public class WorldController {

	// Map
	private Map map;

	// Map Objects
	private List<Vehicle> vehicles;
	
	// World State
	private WorldState worldState;
	private WorldState previousWorldState;
	
	// Current TimeStep
	public int timeStep;
	
	// Max TimeStep
	public int timeStepMax;

	// Camera
	private WorldCamera worldCamera;

	// Generator
	private WorldGenerator worldGenerator;

	// Traffic Manager
	private TrafficManager trafficManager;

	public WorldController() {
		
		// World State
		worldState = WorldState.PAUSED;
		previousWorldState = WorldState.RUNNING;
		
		// TimeStep
		timeStep = 1;

		// Create Map Object Lists
		vehicles = new ArrayList<Vehicle>();

		// Traffic Manager
		trafficManager = TrafficManager.createTestFromFile();

		// Get Vehicles
		vehicles = trafficManager.getVehicles();

		// Get Map
		map = trafficManager.getMap();

		// Create World Camera
		worldCamera = new WorldCamera();

		// Create World Generator
		worldGenerator = new WorldGenerator(this);

		// Start Simulation
		trafficManager.simulate(timeStepMax = trafficManager.getMaximumTimesteps());
	}

	public void update() {
		updateCamera();
	}

	public void updateCamera() {
		worldCamera.update();
	}

	public WorldCamera getWorldCamera() {
		return this.worldCamera;
	}

	public WorldGenerator getWorldGenerator() {
		return this.worldGenerator;
	}

	public void createMap(int columns, int rows) {
		map.reset(columns, rows);
	}

	public Rectangle getBounds() {
		return map.getBounds();
	}

	public List<Node> getNodes() {
		return map.getNodes();
	}

	public List<Edge> getEdges() {
		return map.getEdges();
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public HashMap<Vehicle, Coordinates> getVehicleState(int timestep) {
		return trafficManager.getState(timestep);
	}

	public List<Road> getRoads() {
		return worldGenerator.getRoads();
	}
	
	public WorldState getWorldState() {
		return worldState;
	}
	
	public void setWorldState(WorldState worldState) {
		if(this.worldState != worldState) {
			previousWorldState = this.worldState;
			this.worldState = worldState;
		}
	}
	
	public WorldState getPreviousWorldState() {
		return previousWorldState;
	}
}
