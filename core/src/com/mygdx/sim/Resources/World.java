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

		// Initialise Vehicle Sprites
		this.initVehicleSprites();
		
		// Initialise Road Textures
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
	
	public void initRoadTextures() {
		
		// Create HashMap
		roadTextures = new HashMap<String, Texture>();
		
		
		for(int i = 0; i <= 7; i++) {
			Texture texture;
			roadTextures.put("road_lane_" + i, (texture = new Texture(Gdx.files.internal("resources/roads/road_lane_" + i +".png"))));
			
			texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		}
	}

	public void dispose() {
		vehicleSpritesAtlas.dispose();
	}

}
