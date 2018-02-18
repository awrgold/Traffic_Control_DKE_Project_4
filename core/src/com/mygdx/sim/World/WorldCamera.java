package com.mygdx.sim.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldCamera extends OrthographicCamera {

	public WorldCamera() {
		this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void update() {
		super.update();
	}

	public void resize(int width, int height) {
		super.viewportWidth = width;
		super.viewportHeight = width * ((float) height / (float) width);

		this.update();
	}
	
	public void move(float x, float y) {
		this.position.x += x;
		this.position.y += y;
	}

}
