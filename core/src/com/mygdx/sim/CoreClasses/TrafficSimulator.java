package com.mygdx.sim.CoreClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.Screens.MenuScreen;

public class TrafficSimulator extends Game {
	
	// Screens
	public final MenuScreen menuScreen = new MenuScreen();
	
	// Rendering
	private SpriteBatch batch;
	
	@Override
	public void create () {
		
		//Rendering
		batch = new SpriteBatch();
		
		//Set screen
		setScreen(menuScreen);
	}

	@Override
	public void render () {
		
		// Clear Screen
		Gdx.gl.glClearColor(0.07f, 0.075f, 0.08f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render Screen
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Resources.dispose();
	}
}
