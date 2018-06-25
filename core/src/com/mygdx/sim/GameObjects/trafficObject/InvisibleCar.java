package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;

public class InvisibleCar implements TrafficObject {

	public Edge edge;
	public int startTimestep;
	public int endTimestep;
	public Location location;

	boolean[] visibleToDrivers = new boolean[0];

	public InvisibleCar(Edge edge) {
		this.edge = edge;
		this.location = new Location(edge, 0);
	}

	public void ensureCapacity(int capacity) {
		visibleToDrivers = new boolean[capacity];
	}

	public TrafficObjectState getState(int timestep) {
		Coordinates coordinates = this.getCoordinates(timestep);
		Location location = this.getLocation(timestep);
		float speed = this.getSpeed(timestep);
		boolean vizualize = this.isVisibleInVisualization(timestep);
		boolean visibleToDrivers = this.isVisibleToDrivers(timestep);

		return new TrafficObjectState(coordinates, location, speed, vizualize, visibleToDrivers);
	}

	public void setStartTimestep(int startTimestep) {
		this.startTimestep = startTimestep;
	}

	public int getStartTimestep() {
		return startTimestep;
	}

	public void setEndTimestep(int endTimestep) {
		this.endTimestep = endTimestep;
	}

	public int getEndTimestep() {
		return endTimestep;
	}

	public Coordinates getLocation() {
		return edge.getLocationIfTraveledFraction(1);
	}

	public Coordinates getCoordinates(int timestep) {
		return edge.getLocationIfTraveledDistance(0);
	}

	public Location getLocation(int timestep) {
		return location;
	}

	public float getDistanceOnEdge(int timestep) {
		return 0;
	}

	public Edge getEdge(int timestep) {
		return edge;
	}

	public float getSpeed(int timestep) {
		return 0;
	}

	public boolean isVisibleInVisualization(int timestep) {
		return false;
	}

	public void setIsVisibleToDrivers(int timestep, boolean isVisible) {
		visibleToDrivers[timestep] = isVisible;
	}

	public boolean isVisibleToDrivers(int timestep) {
		return visibleToDrivers[timestep];
	}

}
