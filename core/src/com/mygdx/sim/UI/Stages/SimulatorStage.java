package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.World.WorldController;

public class SimulatorStage extends Stage {

	// World Controller
	private WorldController worldController;

	// All Scroll
	private Vector2 scrollCenter;

	public SimulatorStage(WorldController worldController) {
		this.worldController = worldController;
	}

	@Override
	public void draw() {
		this.getBatch().begin();
		{
			this.drawScrollCenter();
		}
		this.getBatch().end();
		
		super.draw();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height, true);
	}

	private void drawScrollCenter() {
		if (scrollCenter != null) {
			int x = (int) (scrollCenter.x - Resources.ui.allScroll_icon.getWidth() / 2);
			int y = (int) (scrollCenter.y - Resources.ui.allScroll_icon.getHeight() / 2);

			Resources.ui.allScroll_icon.setPosition(x, y);
			Resources.ui.allScroll_icon.draw(this.getBatch());
		}
	}
	
	public void setScrollCenterPos(Vector2 scrollCenter) {
		this.scrollCenter = scrollCenter;
	}

	public WorldController getWorldController() {
		return this.worldController;
	}
}
