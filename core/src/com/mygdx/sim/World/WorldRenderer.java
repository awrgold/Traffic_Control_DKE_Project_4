package com.mygdx.sim.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {
	
	// World Controller
	private WorldController worldController;
	
	// Render Objects
	private ShapeRenderer shapeRenderer;
	
	public WorldRenderer(WorldController worldController) {
		
		// World Controller
		this.worldController = worldController;
		
		// Render Objects
		shapeRenderer = new ShapeRenderer();
	}
	
	public void render(SpriteBatch spriteBatch) {
		this.drawGrid();
	}
	
	private void drawGrid() {
		shapeRenderer.setProjectionMatrix(worldController.getWorldCamera().combined);
		shapeRenderer.begin(ShapeType.Line);
		{
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect( worldController.getBounds().x + 0.5f,
								worldController.getBounds().y + 0.5f,
								worldController.getBounds().width - 0.5f,
								worldController.getBounds().height - 0.5f);
		}
		shapeRenderer.end();
	}
}
