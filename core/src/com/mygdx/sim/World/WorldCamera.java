package com.mygdx.sim.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldCamera extends OrthographicCamera {
	
	private float maxZoom = 50;
	private float minZoom = 0.5f;
	private float scrollSpeed = 1f;

	public WorldCamera() {
		this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		zoom = maxZoom;
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

	public void zoomIn() {
		if (this.zoom < this.maxZoom)
			this.zoom += this.scrollSpeed;
	}

	public void zoomOut() {
		if (this.zoom > this.minZoom)
			this.zoom -= this.scrollSpeed;
	}

}