package com.mygdx.sim.World.Map;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.Node;

public class Map {

	public static final int TILE_SIZE = 32;

	// Map Variables
	private Rectangle bounds;
	private int columns;
	private int rows;
	
	// Node Grid
	private Node[][] nodes;

	public Map(int columns, int rows) {
		this.reset(columns, rows);
	}

	public void reset(int columns, int rows) {

		this.columns = columns;
		this.rows = rows;
		
		// Init Node Grid
		nodes = new Node[columns][rows];

		bounds = new Rectangle(0f, 0f, Map.TILE_SIZE * columns, Map.TILE_SIZE * rows);
		
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				nodes[i][j] = new Node(i, j);
			}
		}
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
	
	public Node[][] getNodes() {
		return this.nodes;
	}
}
