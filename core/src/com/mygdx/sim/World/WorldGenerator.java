package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.Stoplight;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.World.Components.Road;
import com.mygdx.sim.World.Components.TrafficLight;

public class WorldGenerator {

	// World Controller
	private WorldController worldController;

	// Roads
	List<Road> roads;
	
	// Traffic Lights
	List<TrafficLight> trafficLights;

	public WorldGenerator(WorldController worldController) {
		this.worldController = worldController;

		roads = new ArrayList<Road>();
		
		trafficLights = new ArrayList<TrafficLight>();

		generateRoads(worldController.getEdges());
	}

	public void generateRoads(List<Edge> edges) {
		for (Edge edge : edges) {
			roads.add(new Road(edge));
			
			Stoplight stopLight = null;
			if((stopLight = edge.getStopLight()) != null) {
				trafficLights.add(new TrafficLight(stopLight));
			}
		}
	}

	public List<Road> getRoads() {
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights() {
		return trafficLights;
	}
}
