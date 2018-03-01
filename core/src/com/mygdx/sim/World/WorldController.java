package com.mygdx.sim.World;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.Node;
import com.mygdx.sim.World.Map.Map;

public class WorldController {

	// Map
	private Map map;

	// Camera
	private WorldCamera worldCamera;

	public WorldController() {
		map = new Map(20, 20);
		worldCamera = new WorldCamera();
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

	public Node[][] getNodes() {
		return map.getNodes();
	}
}
