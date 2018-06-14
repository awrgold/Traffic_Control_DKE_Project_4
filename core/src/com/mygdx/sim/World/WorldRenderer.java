package com.mygdx.sim.World;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.roads.Road;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObjectState;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;
import com.mygdx.sim.Resources.Resources;

public class WorldRenderer {

	// World Controller
	private WorldController worldController;

	// Render Objects
	private ShapeRenderer shapeRenderer;
	//private Rectangle scissor;

	// Simulation Speed
	private float simulationSpeed = 0.05f;
	private float timer = 0;
	
	// State HashMaps
	HashMap<TrafficObject, TrafficObjectState> previousTrafficObjectState;
	HashMap<TrafficObject, TrafficObjectState> nextTrafficObjectState;

	public WorldRenderer(WorldController worldController) {

		// World Controller
		this.worldController = worldController;
		
		// Get initial HashMaps
		previousTrafficObjectState = worldController.getTrafficObjectState(worldController.timestep - 1);
		nextTrafficObjectState = worldController.getTrafficObjectState(worldController.timestep);

		// Render Objects
		shapeRenderer = new ShapeRenderer();
		//scissor = new Rectangle();
	}

	public void render(SpriteBatch spriteBatch) {

		WorldState worldState;
		if ((worldState = worldController.getWorldState()) != WorldState.PAUSED) {
			timer += Gdx.graphics.getDeltaTime();
			if (timer >= simulationSpeed) {
				if(worldState == WorldState.RUNNING)
				{
					// Increase Time Step
					if (worldController.timestep + 1 < worldController.timestepMax) {
						worldController.timestep++;
					}
				} else if(worldState == WorldState.REWINDING) {
					// Decrease Time Step
					if (worldController.timestep - 1 > 0) {
						worldController.timestep--;
					}
				}
				
				
				previousTrafficObjectState = worldController.getTrafficObjectState(worldController.timestep - 1);
				nextTrafficObjectState = worldController.getTrafficObjectState(worldController.timestep);
				
				// Reset Timer
				timer = 0f;
			}
		}
		
		this.drawMapRoads(spriteBatch);
		this.drawMapNodes(spriteBatch);
		this.drawMapTrafficObjects(spriteBatch, worldController.timestep);

		// Calculate Scissors
		/*
		 * ScissorStack.calculateScissors(worldController.getWorldCamera(),
		 * spriteBatch.getTransformMatrix(), worldController.getBounds(), scissor);
		 * 
		 * ScissorStack.pushScissors(scissor); { this.drawMapRoads(spriteBatch);
		 * this.drawMapNodes(spriteBatch); this.drawMapVehicles(spriteBatch, timeStep);
		 * spriteBatch.flush(); } ScissorStack.popScissors();
		 */

		// Draw Outline
		// this.drawMapOutline();
	}

	private void drawMapOutline() {
		shapeRenderer.setProjectionMatrix(worldController.getWorldCamera().combined);
		shapeRenderer.begin(ShapeType.Line);
		{
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(worldController.getBounds().x + 0.5f, worldController.getBounds().y + 0.5f,
					worldController.getBounds().width - 0.5f, worldController.getBounds().height - 0.5f);
		}
		shapeRenderer.end();
	}

	private void drawMapNodes(SpriteBatch spriteBatch) {

		// Iterate through all nodes
		for (Node node : worldController.getNodes()) {
			if(node.isIntersection()) {
				spriteBatch.draw(Resources.ui.node_intersection_icon, (float) (node.getX()), (float) (node.getY()));
			} else {
				spriteBatch.draw(Resources.ui.node_icon, (float) (node.getX()), (float) (node.getY()));
			}
		}
	}

	private void drawMapRoads(SpriteBatch spriteBatch) {

		// Iterate through all roads
		for (Road road : worldController.getRoads()) {
			road.draw(spriteBatch);
		}
	}

	private void drawMapTrafficObjects(SpriteBatch spriteBatch, int timestep) {
		// Iterate through all vehicles
		for (Vehicle vehicle : worldController.getVehicles()) {
			
			Coordinates previousCoord = previousTrafficObjectState.get(vehicle).getLocation();
			Coordinates nextCoord = nextTrafficObjectState.get(vehicle).getLocation();

			float x = (float) (previousCoord.getX() - nextCoord.getX());
			float y = (float) (previousCoord.getY() - nextCoord.getY());
			float rotation = 90;
			
			rotation += (float) Math.toDegrees(Math.atan2(y, x));

			if (vehicle.isVisibleInVisualization(timestep)){
				vehicle.draw(spriteBatch, (float) nextCoord.getX(), (float) previousCoord.getY(), rotation);
			}
		}
	}
}