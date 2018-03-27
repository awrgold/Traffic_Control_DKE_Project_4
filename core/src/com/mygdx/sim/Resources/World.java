package com.mygdx.sim.Resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class World {

	// Vehicle Sprites
	public TextureAtlas vehicleSpritesAtlas;
	public HashMap<String, Sprite> vehicleSprites;

	// Road Textures
	public HashMap<String, Texture> roadTextures;

	public World() {

		// Initialize Vehicle Sprites
		this.initVehicleSprites();

		// Initialize Road Textures
		this.initRoadTextures();
	}

	private void initVehicleSprites() {

		// Load Atlas
		vehicleSpritesAtlas = new TextureAtlas(Gdx.files.internal("resources/vehicles/vehicles.atlas"));

		// Create HashMap
		vehicleSprites = new HashMap<String, Sprite>();

		for (AtlasRegion atlasRegion : vehicleSpritesAtlas.getRegions()) {

			// Map Name to Sprite in HashMap
			vehicleSprites.put(atlasRegion.name, vehicleSpritesAtlas.createSprite(atlasRegion.name));
		}
	}

	private void initRoadTextures() {

		// Create HashMap
		roadTextures = new HashMap<String, Texture>();

		// Create Texture
		Texture texture = new Texture(Gdx.files.internal("resources/roads/road_1.png"));

		// Set Filter to Nearest Neighbor
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		// Make texture wrap around texture region
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		roadTextures.put("road", texture);
	}

	public void dispose() {
		vehicleSpritesAtlas.dispose();
	}

}
