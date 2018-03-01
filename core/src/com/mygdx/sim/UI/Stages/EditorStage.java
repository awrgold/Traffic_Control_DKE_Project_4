package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.UI.Components.Sidebar.SidebarPanel;
import com.mygdx.sim.World.WorldController;

public class EditorStage extends Stage {

	// World Controller
	private WorldController worldController;

	// UI
	private SidebarPanel sidebarPanel;

	// All Scroll
	private Vector2 scrollCenter;

	public EditorStage(WorldController worldController) {

		// Viewport | Camera
		super(new ScreenViewport());

		// World Controller
		this.worldController = worldController;

		// Sidebar
		sidebarPanel = new SidebarPanel(Resources.ui.skin, worldController);

		this.addActor(sidebarPanel);
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

		sidebarPanel.resize(width, height);
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
