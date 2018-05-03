package com.mygdx.sim.GameObjects.roads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.Resources.Resources;

public class Road {

	// Sprite Parameters
	private String textureName;
	private float length;
	private float angle;
	private float posX;
	private float posY;

	// Texture
	private Texture texture;
	private TextureRegion textureRegion;

	public Road(Edge edge, String textureName) {

		this.textureName = textureName;
		setSprite(textureName);

		length = (float) edge.getLength() + texture.getWidth();

		int x = (int) (edge.getTo().getX() - edge.getFrom().getX());
		int y = (int) (edge.getTo().getY() - edge.getFrom().getY());

		angle = (float) Math.toDegrees(Math.atan2(y, x));

		posX = (float) edge.getFrom().getX() - texture.getWidth() / 2;
		posY = (float) edge.getFrom().getY() - texture.getHeight() / 2;

		texture = new Texture(Gdx.files.internal("resources/roads/road_1.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth() * (int) (length / texture.getWidth()),
				texture.getHeight());
	}

	public void setSprite(String textureName) {
		this.textureName = textureName;

		texture = Resources.world.roadTextures.get(textureName);
	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(textureRegion, posX, posY, texture.getWidth() / 2, texture.getHeight() / 2, length,
				texture.getHeight(), 1f, 1f, angle);
	}
}
