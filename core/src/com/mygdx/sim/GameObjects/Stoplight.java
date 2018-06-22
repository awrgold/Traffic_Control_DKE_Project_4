package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.trafficObject.InvisibleCar;

public class Stoplight {

	// Timer
	private int timer;

	// UI
	private Sprite sprite;

	// Properties
	private Edge lane;
	private Node parent;
	private ArrayList<Stoplight> linked;
	private LightState lightState;
	private Location location;
	private InvisibleCar invisibleCar;

	/**
	 * The stoplight is an array of Enumerable Colors, where position N (# of lanes)
	 * is the left-most lane, and position 0 is the right most lane.
	 * 
	 * @param coords:
	 *            the location of the stoplight in 2D Euclidean space
	 */

	public Stoplight(Edge lane) {
		this.lane = lane;
		this.parent = lane.getTo();
		this.location = new Location(lane, 0);
		this.lightState = LightState.GREEN;
	}

	public Edge getLane() {
		return lane;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Stoplight> getLinked() {
		return linked;
	}

	public void setLinked(ArrayList<Stoplight> neighbors) {
		this.linked = neighbors;
	}

	public void addLinked(Stoplight neighbor) {
		linked.add(neighbor);
	}

	public void addLinkedAt(int index, Stoplight neighbor) {
		linked.add(index, neighbor);
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public LightState getLightState() {
		return lightState;
	}

	public void setLightState(LightState lightState) {
		this.lightState = lightState;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * This only controls the single stoplight. Traffic controller must switch
	 * lights for all linked. Use forEach in getLinked() to switch lights
	 */
	public void switchLight() {
		switch (lightState) {

		case RED: {
			setLightState(LightState.GREEN);
			invisibleCar = null;
			break;
		}

		case GREEN: {
			setLightState(LightState.RED);
			// TODO Place invisible cars properly
			invisibleCar = new InvisibleCar(getParent());
		}

		}
	}

}
