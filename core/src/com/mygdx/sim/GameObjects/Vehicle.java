package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.Resources.Resources;

public class Vehicle {

	// Sprite Name
	protected String spriteName;

	// Path
	private ArrayList<Node> nodePath;

	// Sprite
	private Sprite sprite;

	// Start/Goal Nodes
	private Node startNode;
	private Node goalNode;
	
	// Max Speed
	protected int maxSpeed = 80;

	public Vehicle(Node startNode, Node goalNode) {

		// Set Sprite
		initSprite();

		// Set Start/Goal
		this.startNode = startNode;
		this.goalNode = goalNode;

		// Initialize Path Search
		nodePath = new ArrayList<Node>();
		findPath();
	}

	private void initSprite() {
		sprite = Resources.world.vehicleSprites.get(spriteName);

	}

	public void findPath() {
		// TODO A*
	}

	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {
		// All check the vehicle has to make
	}
}
