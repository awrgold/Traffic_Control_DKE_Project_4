package com.mygdx.sim.World.Components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.Resources.Resources;

public class Road {

	// Sprite Parameters
	private float length;
	private float angle;
	private float posX;
	private float posY;

	// Texture
	private Texture texture = Resources.world.roadTextures.get("road_lane_1");
	private List<RoadTextureRegion> roadTextureRegions = new ArrayList<RoadTextureRegion>();

	public Road(Edge edge) {

		if (edge.getShapeCoordinates() != null) {
			createMultipleTextureRegions(edge);
		} else {
			createSingleTextureRegion(edge);
		}
	}

	private void createMultipleTextureRegions(Edge edge) {
		List<Coordinates> shapeCoordinates = edge.getShapeCoordinates();

		for (int i = 0; i < shapeCoordinates.size(); i++) {
			if (i < shapeCoordinates.size() - 1) {
				Coordinates mannhatten = shapeCoordinates.get(i).subtractAbs(shapeCoordinates.get(i + 1));
				length = (float) Math.sqrt(Math.pow(mannhatten.getX(), 2) + Math.pow(mannhatten.getY(), 2)) + texture.getWidth();

				int x = (int) (shapeCoordinates.get(i + 1).getX() - shapeCoordinates.get(i).getX());
				int y = (int) (shapeCoordinates.get(i + 1).getY() - shapeCoordinates.get(i).getY());

				angle = (float) Math.toDegrees(Math.atan2(y, x));

				posX = (float) shapeCoordinates.get(i).getX() - texture.getWidth() / 2;
				posY = (float) shapeCoordinates.get(i).getY() - texture.getHeight() / 2;
				TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth() * (int) (length / texture.getWidth()), texture.getHeight());
				roadTextureRegions.add(new RoadTextureRegion(length, angle, posX, posY, textureRegion));
			}
		}
	}

	private void createSingleTextureRegion(Edge edge) {
		length = (float) edge.getLength() + texture.getWidth();

		int x = (int) (edge.getTo().getX() - edge.getFrom().getX());
		int y = (int) (edge.getTo().getY() - edge.getFrom().getY());

		angle = (float) Math.toDegrees(Math.atan2(y, x));

		posX = (float) edge.getFrom().getX() - texture.getWidth() / 2;
		posY = (float) edge.getFrom().getY() - texture.getHeight() / 2;
		TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth() * (int) (length / texture.getWidth()), texture.getHeight());
		roadTextureRegions.add(new RoadTextureRegion(length, angle, posX, posY, textureRegion));
	}

	public void draw(SpriteBatch spriteBatch) {
		for (RoadTextureRegion roadTextureRegion : roadTextureRegions) {
			spriteBatch.draw(roadTextureRegion.textureRegion, roadTextureRegion.posX, roadTextureRegion.posY, texture.getWidth() / 2, texture.getHeight() / 2, roadTextureRegion.length, texture.getHeight(), 1f, 1f, roadTextureRegion.angle);
		}
	}
}