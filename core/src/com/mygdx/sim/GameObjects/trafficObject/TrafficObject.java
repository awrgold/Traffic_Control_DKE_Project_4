package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;

public interface TrafficObject {
	public TrafficObjectState getState(int timestep);
	
	public Coordinates getCoordinates(int timestep);
	
	public Location getLocation(int timestep);
	
	public float getDistanceOnEdge(int timestep);
	
	public Edge getEdge(int timestep);
	
	public float getSpeed(int timestep);
	
	public boolean isVisibleInVisualization(int timestep);
	
	public boolean isVisibleToDrivers(int timestep);

}
