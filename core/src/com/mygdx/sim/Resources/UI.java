package com.mygdx.sim.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UI {

	// Skin
	public Skin skin;
	public TextureAtlas atlas;

	// Menu Textures
	public Texture texture_menu_background;

	// Menu Sprites
	public Sprite menu_icon_start;
	public Sprite menu_icon_editor;
	public Sprite menu_icon_settings;
	
	// Misc
	public Sprite allScroll_icon;
	public Sprite node_icon;
	public Sprite node_intersection_icon;
	
	// Audio
	public Sound sound_click;

	public UI() {

		// Create Atlas
		atlas = new TextureAtlas(Gdx.files.internal("resources/skins/ui.atlas"));

		// Initialise Components
		this.initSkin();
		this.initMenuUI();
		this.initSounds();
		this.initMisc();
	}

	private void initSkin() {

		// Create Skin
		skin = new Skin();
		skin.addRegions(atlas);

		// Read Skin
		skin.load(Gdx.files.internal("resources/skins/ui.json"));
	}

	private void initMenuUI() {

		// Menu Icons
	}
	
	private void initSounds() {
		sound_click = Gdx.audio.newSound(Gdx.files.internal("resources/sounds/click.ogg"));
	}
	
	private void initMisc() {
		allScroll_icon = atlas.createSprite("all-scroll");
		node_icon = atlas.createSprite("node");
		node_intersection_icon = atlas.createSprite("node-intersection");
	}

	public void dispose() {
		
		// Skin
		skin.dispose();
		atlas.dispose();
		
		// Sounds
		sound_click.dispose();
	}

}
