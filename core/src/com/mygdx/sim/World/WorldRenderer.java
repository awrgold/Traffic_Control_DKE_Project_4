package com.mygdx.sim.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.World.Map.Map;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

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
			this.drawMapObjects(spriteBatch);
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

	private void drawMapObjects(SpriteBatch spriteBatch) {

		// Iterate through all nodes
		for(Node[] nodes : worldController.getNodes()) {
			for(Node node : nodes) {
				spriteBatch.draw(Resources.ui.allScroll_icon,
		                (float)(node.getX() * Map.TILE_SIZE),
		                (float)(node.getY() * Map.TILE_SIZE));
			}
		}
	}
}
