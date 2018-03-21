package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

public class WorldController {

	// Map
	private Map map;

	// Map Objects
	private List<Vehicle> vehicles;

	// Camera
	private WorldCamera worldCamera;
	
	// Traffic Manager
	private TrafficManager trafficManager;

	public WorldController() {

		// Create Map Object Lists
		vehicles = new ArrayList<Vehicle>();

		// Create World Camera
		worldCamera = new WorldCamera();

		// Traffic Manager
		trafficManager = TrafficManager.createEnvironment();
		
		// Get Vehicles
		vehicles = trafficManager.getVehicles();
		
		// Get Map
		map = trafficManager.getMap();
		
		// Start Simulation
		trafficManager.simulate(TrafficManager.TIMESTEPS);
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

	public void createMap(int columns, int rows) {
		map.reset(columns, rows);
	}

	public Rectangle getBounds() {
		return map.getBounds();
	}

	public List<Node> getNodes() {
		return map.getNodes();
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}
}
