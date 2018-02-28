package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.sim.World.WorldController;

public class SimulatorStage extends Stage {

	// World Controller
	private WorldController worldController;

	public SimulatorStage(WorldController worldController) {
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
