package com.mygdx.sim.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.sim.UI.Stages.EditorStage;

public class EditorScreen implements Screen {
	// UI
	private EditorStage editorStage;

	@Override
	public void show() {

		// UI
		editorStage = new EditorStage();

		// User Input
		Gdx.input.setInputProcessor(editorStage);
	}

	@Override
	public void render(float delta) {

		// Editor Stage
		editorStage.act();
		editorStage.draw();

	}

	@Override
	public void resize(int width, int height) {
		editorStage.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		this.dispose();
	}

	@Override
	public void dispose() {
		editorStage.dispose();
	}
}
