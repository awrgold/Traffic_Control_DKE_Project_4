package com.mygdx.sim.Resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class World {

	// Vehicle Sprites
	public TextureAtlas vehicleSpritesAtlas;
	public HashMap<String, Sprite> vehicleSprites;

	// Road Sprites
	public TextureAtlas roadSpritesAtlas;
	public HashMap<String, Sprite> roadSprites;

	public World() {

		// Initialize Vehicle Sprites
		this.initVehicleSprites();
		
		// Initialize Road Sprites
		this.initRoadSprites();
	}

	private void initVehicleSprites() {

		// Load Atlas
		vehicleSpritesAtlas = new TextureAtlas(Gdx.files.internal("resources/vehicles/vehicles.atlas"));

		// Create HashMap
		vehicleSprites = new HashMap<String, Sprite>();

		for (AtlasRegion atlasRegion : vehicleSpritesAtlas.getRegions()) {

			// Set Filter to Nearest Neighbor
			atlasRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

			// Map Name to Sprite in HashMap
			vehicleSprites.put(atlasRegion.name, vehicleSpritesAtlas.createSprite(atlasRegion.name));
		}
	}

	private void initRoadSprites() {

		// Load Atlas
		roadSpritesAtlas = new TextureAtlas(Gdx.files.internal("resources/roads/roads.atlas"));
		
		// Create HashMap
		roadSprites = new HashMap<String, Sprite>();
		
		for(AtlasRegion atlasRegion : roadSpritesAtlas.getRegions()) {
			
			// Set Filter to Nearest Neighbor
			atlasRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			
			// Map Name to Sprite in HashMap
			roadSprites.put(atlasRegion.name, roadSpritesAtlas.createSprite(atlasRegion.name));
		}
	}

	public void dispose() {
		vehicleSpritesAtlas.dispose();
		roadSpritesAtlas.dispose();
	}

}
