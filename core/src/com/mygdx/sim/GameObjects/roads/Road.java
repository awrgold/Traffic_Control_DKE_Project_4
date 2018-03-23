package com.mygdx.sim.GameObjects.roads;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.Resources.Resources;

public class Road {

	// Sprite Parameters
	private String spriteName;
	private float length;
	private float angle;
	private float posX;
	private float posY;

	// Sprites
	private Sprite sprite;

	
	public Road(Edge edge, String spriteName) {
		
		this.spriteName = spriteName;
		setSprite(spriteName);
		
		length = (float) edge.getLength() + sprite.getRegionHeight();
		
		angle = (float) Math.asin((edge.getTo().getY() - edge.getFrom().getY()) / edge.getLength());
		angle = (float) Math.toDegrees(angle);
		
		
		
		posX = (float) edge.getFrom().getX() - sprite.getRegionWidth() / 2;
		posY = (float) edge.getFrom().getY() - sprite.getRegionHeight() / 2;
	}

	public void setSprite(String spriteName) {
		this.spriteName = spriteName;

		sprite = Resources.world.roadSprites.get("road");
	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(sprite, posX, posY, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2,
				length, sprite.getRegionWidth(), 1f, 1f, angle, false);
	}
}
