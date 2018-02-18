package com.mygdx.sim.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.CoreClasses.TrafficSimulator;
import com.mygdx.sim.UI.Input.EditorInput;
import com.mygdx.sim.UI.Stages.EditorStage;
import com.mygdx.sim.World.WorldController;
import com.mygdx.sim.World.WorldRenderer;

public class EditorScreen implements Screen {

	// World
	private WorldController worldController;
	private WorldRenderer worldRenderer;

	// Input
	private EditorInput editorInput;
	private InputMultiplexer inputMultiplexer;

	// UI
	private EditorStage editorStage;

	@Override
	public void show() {

		// World Controller
		worldController = new WorldController();

		// World Renderer
		worldRenderer = new WorldRenderer(worldController);

		// UI
		editorStage = new EditorStage(worldController);

		// Input
		editorInput = new EditorInput(editorStage);
		inputMultiplexer = new InputMultiplexer();
		
		inputMultiplexer.addProcessor(editorStage);
		inputMultiplexer.addProcessor(editorInput);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {

		// World Controller Update
		worldController.update();

		// Sprite Batch
		SpriteBatch batch = TrafficSimulator.get().getBatch();

		// Render World
		batch.begin();
		{
			batch.setProjectionMatrix(worldController.getWorldCamera().combined);
			worldRenderer.render(batch);
		}
		batch.end();

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

		// UI
		editorStage.dispose();

		// Input
		inputMultiplexer.clear();
	}
}
