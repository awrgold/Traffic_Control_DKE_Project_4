package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;

public class TrafficObjectState {
	private final Coordinates location;
	private final boolean visibleInVisualization;
	private final boolean visibleToDrivers;
	
	public TrafficObjectState(Coordinates location, boolean visibleInVisualization, boolean visibleToDrivers) {
		this.location = location;
		this.visibleInVisualization = visibleInVisualization;
		this.visibleToDrivers = visibleToDrivers;
	}
	
	public Coordinates getLocation() { return location; }
	
	public boolean isVisibleInVisualization() { return visibleInVisualization; }
	
	public boolean visibleToDrivers() { return visibleToDrivers; }
}
