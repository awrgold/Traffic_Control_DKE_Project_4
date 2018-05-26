package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.roads.Road;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;
import com.mygdx.sim.Resources.Resources;

public class WorldRenderer {

	// World Controller
	private WorldController worldController;

	// Render Objects
	private ShapeRenderer shapeRenderer;
	//private Rectangle scissor;

	// Simulation Speed
	private float simulationSpeed = 0.1f;
	private float timer = 0;

	public WorldRenderer(WorldController worldController) {

		// World Controller
		this.worldController = worldController;

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
					if (worldController.timeStep + 1 < worldController.timeStepMax) {
						worldController.timeStep++;
					}
				} else if(worldState == WorldState.REWINDING) {
					// Decrease Time Step
					if (worldController.timeStep - 1 > 0) {
						worldController.timeStep--;
					}
				}

				// Reset Timer
				timer = 0f;
			}
		}

		// Calculate Scissors
		/*
		 * ScissorStack.calculateScissors(worldController.getWorldCamera(),
		 * spriteBatch.getTransformMatrix(), worldController.getBounds(), scissor);
		 * 
		 * ScissorStack.pushScissors(scissor); { this.drawMapRoads(spriteBatch);
		 * this.drawMapNodes(spriteBatch); this.drawMapVehicles(spriteBatch, timeStep);
		 * spriteBatch.flush(); } ScissorStack.popScissors();
		 */

		this.drawMapRoads(spriteBatch);
		this.drawMapNodes(spriteBatch);
		this.drawMapVehicles(spriteBatch, worldController.timeStep);

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

	private void drawMapVehicles(SpriteBatch spriteBatch, int timeStep) {
		// Iterate through all vehicles
		for (Vehicle vehicle : worldController.getVehicles()) {
			Coordinates previousCoord = worldController.getVehicleState(worldController.timeStep - 1).get(vehicle);
			Coordinates nextCoord = worldController.getVehicleState(worldController.timeStep - 1).get(vehicle);

			float x = (float) (previousCoord.getX() - nextCoord.getX());
			float y = (float) (previousCoord.getY() - nextCoord.getY());
			float rotation = 90;
			
			rotation += (float) Math.toDegrees(Math.atan2(y, x));

			vehicle.draw(spriteBatch, (float) nextCoord.getX(), (float) previousCoord.getY(), rotation);
		}
	}
}