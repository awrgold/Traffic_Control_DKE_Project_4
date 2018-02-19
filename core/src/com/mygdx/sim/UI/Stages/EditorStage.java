package com.mygdx.sim.UI.Stages;

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

	public EditorStage(WorldController worldController) {

		// Viewport | Camera
		super(new ScreenViewport());

		// World Controller
		this.worldController = worldController;
		
		// Sidebar
		sidebarPanel = new SidebarPanel(Resources.ui.skin);
		
		this.addActor(sidebarPanel);
	}

	@Override
	public void draw() {
		super.draw();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height, true);
		
		sidebarPanel.resize(width, height);
	}

	public WorldController getWorldController() {
		return this.worldController;
	}

}
