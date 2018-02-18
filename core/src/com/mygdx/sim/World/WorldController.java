package com.mygdx.sim.World;

import com.badlogic.gdx.math.Rectangle;
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

	public Rectangle getBounds() {
		return map.getBounds();
	}
}
