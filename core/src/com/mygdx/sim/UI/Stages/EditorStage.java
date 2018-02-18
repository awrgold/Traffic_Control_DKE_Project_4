package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.sim.World.WorldController;

public class EditorStage extends Stage {

	private WorldController worldController;
	
	public EditorStage(WorldController worldController) {

		// Viewport | Camera
		super(new ScreenViewport());
		
		// World Controller
		this.worldController = worldController;
	}

	@Override
	public void draw() {
		super.draw();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height, true);
	}
	
	public WorldController getWorldController() {
		return this.worldController;
	}
	
}
