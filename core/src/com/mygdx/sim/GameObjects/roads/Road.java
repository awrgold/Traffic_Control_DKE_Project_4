package com.mygdx.sim.GameObjects.roads;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.Resources.Resources;

public class Road {

	// Coordinates
	private Coordinates startPoint;
	private Coordinates endPoint;

	// Sprite Parameters
	private String spriteName;
	private float width;
	private float angle;

	// Sprites
	private Sprite sprite;

	public Road(Coordinates startPoint, Coordinates endPoint, String spriteName) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;

		this.spriteName = spriteName;

		width = (float) Math.sqrt(
				Math.pow(endPoint.getX() - startPoint.getX(), 2) + Math.pow(endPoint.getY() - startPoint.getY(), 2));
		angle = (float) Math.asin((endPoint.getY() - startPoint.getY()) / width);
		angle = (float) Math.toDegrees(angle);

		setSprite(spriteName);
	}

	public void setSprite(String spriteName) {
		this.spriteName = spriteName;

		sprite = Resources.world.roadSprites.get("road");
	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(sprite, (float) startPoint.getX(), (float) startPoint.getY(), 0, 0, width,
				sprite.getRegionWidth(), 1f, 1f, angle, false);
	}
}
