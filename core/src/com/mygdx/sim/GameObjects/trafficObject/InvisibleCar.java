package com.mygdx.sim.GameObjects.trafficObject;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Location;
import com.mygdx.sim.GameObjects.data.Node;

public class InvisibleCar implements TrafficObject {

    public Node node;
    public int startTimestep;
    public int endTimestep;
    
    boolean[] visibleToDrivers = new boolean[0];

    public InvisibleCar(Node node){
        this.node = node;
    }
    
    public void ensureCapacity(int capacity) {
		visibleToDrivers = new boolean[capacity];
	}

    @Override
    public TrafficObjectState getState(int timestep) {
        return null;
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

    public Node getNode(){
        return node;
    }

    public void setNode(Node n){
        this.node = n;
    }

    public Coordinates getLocation(){
        return node.getLocation();
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
	
	public void setIsVisibleToDrivers(int timestep, boolean isVisible) {
		visibleToDrivers[timestep] = isVisible;
	}

	@Override
	public boolean isVisibleToDrivers(int timestep) {
		return visibleToDrivers[timestep];
	}


}
