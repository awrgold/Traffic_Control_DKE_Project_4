package com.mygdx.sim.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.sim.UI.Stages.MainMenuStage;

public class MenuScreen implements Screen {

	// UI
	private MainMenuStage mainMenuStage;

	@Override
	public void show() {

		// UI
		mainMenuStage = new MainMenuStage();

		// User Input
		Gdx.input.setInputProcessor(mainMenuStage);
	}

	@Override
	public void render(float delta) {

		// Main Menu Stage
		mainMenuStage.act();
		mainMenuStage.draw();

	}

	@Override
	public void resize(int width, int height) {
		mainMenuStage.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		mainMenuStage.dispose();
	}

}
