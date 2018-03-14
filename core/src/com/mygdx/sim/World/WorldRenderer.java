package com.mygdx.sim.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;
import com.mygdx.sim.Resources.Resources;

public class WorldRenderer {

	// World Controller
	private WorldController worldController;

	// Render Objects
	private ShapeRenderer shapeRenderer;
	private Rectangle scissor;

	public WorldRenderer(WorldController worldController) {

		// World Controller
		this.worldController = worldController;

		// Render Objects
		shapeRenderer = new ShapeRenderer();
		scissor = new Rectangle();
	}

	public void render(SpriteBatch spriteBatch) {

		// Calculate Scissors
		ScissorStack.calculateScissors(worldController.getWorldCamera(), spriteBatch.getTransformMatrix(),
				worldController.getBounds(), scissor);

		ScissorStack.pushScissors(scissor);
		{
			this.drawMapNodes(spriteBatch);
			this.drawMapVehicles(spriteBatch);
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
			spriteBatch.draw(Resources.ui.allScroll_icon, (float) (node.getX() / 100 * Map.TILE_SIZE),
					(float) (node.getY() / 100 * Map.TILE_SIZE));
		}
	}

	private void drawMapVehicles(SpriteBatch spriteBatch) {
		//TODO: Get Vehicle location based on timestamp
		// Iterate through all vehicles
		for (Vehicle vehicle : worldController.getVehicles()) {
			spriteBatch.draw(Resources.ui.allScroll_icon, (float) (vehicle.getLocationCoordinates(10).getX() / 100 * Map.TILE_SIZE),
					(float) (vehicle.getLocationCoordinates(10).getY() / 100 * Map.TILE_SIZE));
		}
	}
}
