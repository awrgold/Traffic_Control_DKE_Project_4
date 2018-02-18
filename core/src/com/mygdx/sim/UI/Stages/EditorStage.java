package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EditorStage extends Stage {

	public EditorStage() {

		// Viewport | Camera
		super(new ScreenViewport());
	}

	@Override
	public void draw() {
		super.draw();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height, true);
	}
}
