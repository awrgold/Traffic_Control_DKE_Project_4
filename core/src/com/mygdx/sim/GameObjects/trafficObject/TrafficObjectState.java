package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Location;

public class TrafficObjectState {
	private final Coordinates coordinates;
	private final Location location;
	private final float speed;
	private final boolean visibleInVisualization;
	private final boolean visibleToDrivers;
	
	public TrafficObjectState(Coordinates coordinates, Location location, float speed, boolean visibleInVisualization, boolean visibleToDrivers) {
		this.coordinates = coordinates;
		this.speed = speed;
		this.location = location;
		this.visibleInVisualization = visibleInVisualization;
		this.visibleToDrivers = visibleToDrivers;
	}
	
	public Coordinates getCoordinates() { return coordinates; }
	
	public Location getLocation() { return location; }
	
	public boolean isVisibleInVisualization() { return visibleInVisualization; }
	
	public boolean visibleToDrivers() { return visibleToDrivers; }
	
	public float getSpeed() { return speed; }
}
