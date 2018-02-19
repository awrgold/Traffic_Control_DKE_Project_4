package com.mygdx.sim.World.Map;

import com.badlogic.gdx.math.Rectangle;

public class Map {

	private static final int TILE_SIZE = 32;

	// Map Variables
	private Rectangle bounds;
	private int columns;
	private int rows;

	public Map(int columns, int rows) {
		this.reset(columns, rows);

		this.columns = columns;
		this.rows = rows;
	}

	public void reset(int columns, int rows) {

		this.columns = columns;
		this.rows = rows;

		bounds = new Rectangle(0f, 0f, Map.TILE_SIZE * columns, Map.TILE_SIZE * rows);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getColumns() {
		return this.columns;
	}

	public int getRows() {
		return this.rows;
	}
}
