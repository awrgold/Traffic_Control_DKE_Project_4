package com.mygdx.sim.World.Map;

import com.badlogic.gdx.math.Rectangle;

public class Map {
	
	private static final int TILE_SIZE = 32;
	
	// Map Variables
	private Rectangle bounds;
	
	public Map(int columns, int rows) {
		this.reset(columns, rows);
	}
	
	public void reset(int columns, int rows) {
		bounds = new Rectangle(0f, 0f, Map.TILE_SIZE * columns, Map.TILE_SIZE * rows);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
}
