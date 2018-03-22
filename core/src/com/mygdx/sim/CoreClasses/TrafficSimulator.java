package com.mygdx.sim.CoreClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.TrafficManager;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.Screens.EditorScreen;
import com.mygdx.sim.Screens.MenuScreen;
import com.mygdx.sim.Screens.SimulatorScreen;

public class TrafficSimulator extends Game {
	
	// Singleton
	private static TrafficSimulator trafficSimulator;
	
	// Screens
	public final MenuScreen menuScreen = new MenuScreen();
	public final EditorScreen editorScreen = new EditorScreen();
	public final SimulatorScreen simulatorScreen = new SimulatorScreen();

	// Rendering
	private SpriteBatch batch;
	
	public static TrafficSimulator get() {
		if(trafficSimulator == null) {
			trafficSimulator = new TrafficSimulator();
		}
		return trafficSimulator;
	}
	
	@Override
	public void create () {
		
		//Rendering
		batch = new SpriteBatch();
		
		//Set screen
		this.setScreen(menuScreen);

	}

	@Override
	public void render () {
		
		// Clear Screen
		Gdx.gl.glClearColor(0.21f, 0.22f, 0.24f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render Screen
		super.render();
	}
	
	public SpriteBatch getBatch() {
		return this.batch;
	}
	
	@Override
	public void dispose () {
		
		//Menus
		menuScreen.dispose();
		editorScreen.dispose();
		simulatorScreen.dispose();
		
		batch.dispose();
		Resources.dispose();
	}
}
