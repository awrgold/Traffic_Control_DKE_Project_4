package com.mygdx.sim.GameObjects.roads;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RoadTextureRegion {
	public float length;
	public float angle;
	public float posX;
	public float posY;
	public TextureRegion textureRegion;
	
	public RoadTextureRegion(float length, float angle, float posX, float posY, TextureRegion textureRegion) {
		this.length = length;
		this.angle = angle;
		this.posX = posX;
		this.posY = posY;
		this.textureRegion = textureRegion;
	}
}
