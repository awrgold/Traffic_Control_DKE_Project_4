package com.mygdx.sim.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.roads.Road;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;
import com.mygdx.sim.Resources.Resources;

public class WorldRenderer {

	// World Controller
	private WorldController worldController;

	// Render Objects
	private ShapeRenderer shapeRenderer;
	private Rectangle scissor;

	// Time Step
	private int timeStep = 0;

	// Vehicle History
	public ArrayList<HashMap<Vehicle, Coordinates>> vehicleHistory;

	public WorldRenderer(WorldController worldController) {

		// World Controller
		this.worldController = worldController;

		// Render Objects
		shapeRenderer = new ShapeRenderer();
		scissor = new Rectangle();

		// Get Vehicle History
		vehicleHistory = worldController.getVehicleHistory();
	}

	public void render(SpriteBatch spriteBatch) {

		// Increase Time Step
		if (timeStep + 1 < vehicleHistory.size())
			timeStep++;

		// Calculate Scissors
		ScissorStack.calculateScissors(worldController.getWorldCamera(), spriteBatch.getTransformMatrix(),
				worldController.getBounds(), scissor);

		ScissorStack.pushScissors(scissor);
		{
			this.drawMapNodes(spriteBatch);

			this.drawMapRoads(spriteBatch);
			this.drawMapVehicles(spriteBatch, timeStep);
			spriteBatch.flush();
		}
		ScissorStack.popScissors();

		// Draw Outline
		this.drawMapOutline();
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
			spriteBatch.draw(Resources.ui.allScroll_icon, (float) (node.getX()), (float) (node.getY()));
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
			Coordinates previousCoord = vehicleHistory.get(timeStep - 1).get(vehicle);
			Coordinates nextCoord = vehicleHistory.get(timeStep).get(vehicle);

			float x = (float) (previousCoord.getX() - nextCoord.getX());
			float y = (float) (previousCoord.getY() - nextCoord.getY());
			float rotation = 0;

			if (x > 0)
				rotation += 90;
			else if (x < 0)
				rotation -= 90;
			else if (y > 0)
				rotation -= 180;
			else if (y < 0)
				rotation = 0;

			vehicle.draw(spriteBatch, (float) nextCoord.getX(), (float) previousCoord.getY(), rotation);
		}
	}
}
