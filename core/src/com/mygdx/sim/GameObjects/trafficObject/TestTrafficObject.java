package com.mygdx.sim.GameObjects.trafficObject;

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
	

}
