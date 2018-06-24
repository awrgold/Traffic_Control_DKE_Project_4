package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;

public class TestTrafficObject implements TrafficObject {
	
	private Location location;
	
	public TestTrafficObject(Location loc) {
		this.location = loc;
	}

	@Override
	public TrafficObjectState getState(int timestep) {
		return new TrafficObjectState(getCoordinates(timestep), getLocation(timestep), getSpeed(timestep), isVisibleInVisualization(timestep), isVisibleToDrivers(timestep));
	}

	@Override
	public Coordinates getCoordinates(int timestep) {
		return location.getEdge().getLocationIfTraveledDistance(location.getDistanceOnEdge());
	}

	@Override
	public Location getLocation(int timestep) {
		return location;
	}

	@Override
	public float getDistanceOnEdge(int timestep) {
		return location.getDistanceOnEdge();
	}

	@Override
	public Edge getEdge(int timestep) {
		return location.getEdge();
	}

	@Override
	public float getSpeed(int timestep) {
		return 0;
	}

	@Override
	public boolean isVisibleInVisualization(int timestep) {
		return true;
	}

	@Override
	public boolean isVisibleToDrivers(int timestep) {
		return true;
	}
	

}
