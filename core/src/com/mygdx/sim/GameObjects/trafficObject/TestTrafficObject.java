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
		return new TrafficObjectState(null, location, 0, false, (timestep < 85));
	}

	@Override
	public Coordinates getCoordinates(int timestep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(int timestep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getDistanceOnEdge(int timestep) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Edge getEdge(int timestep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getSpeed(int timestep) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isVisibleInVisualization(int timestep) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisibleToDrivers(int timestep) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
