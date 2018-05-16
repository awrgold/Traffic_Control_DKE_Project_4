package com.mygdx.sim.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.CoreClasses.TrafficSimulator;
import com.mygdx.sim.UI.Input.SimulatorInput;
import com.mygdx.sim.UI.Stages.SimulatorStage;
import com.mygdx.sim.World.WorldController;
import com.mygdx.sim.World.WorldRenderer;

public class SimulatorScreen implements Screen {

	// World
	private WorldController worldController;
	private WorldRenderer worldRenderer;

	// Input
	private SimulatorInput simulatorInput;
	private InputMultiplexer inputMultiplexer;

	// UI
	private SimulatorStage simulatorStage;

	@Override
	public void show() {

		// World Controller
		worldController = new WorldController();

		// World Renderer
		worldRenderer = new WorldRenderer(worldController);

		// UI
		simulatorStage = new SimulatorStage(worldController);

		// Input
		simulatorInput = new SimulatorInput(simulatorStage);
		inputMultiplexer = new InputMultiplexer();

		inputMultiplexer.addProcessor(simulatorStage);
		inputMultiplexer.addProcessor(simulatorInput);

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

		// Simulator Stage
		simulatorStage.act();
		simulatorStage.draw();

		// Input
		simulatorInput.update();
	}

	@Override
	public void resize(int width, int height) {
		simulatorStage.resize(width, height);
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
		try {
			simulatorStage.dispose();
		} catch (Exception ex) {

		}
	}

}
