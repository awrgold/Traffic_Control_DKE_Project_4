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
	
	// Audio
	public Sound sound_click;

	public UI() {

		// Create Atlas
		atlas = new TextureAtlas(Gdx.files.internal("resources/skins/ui.atlas"));

		// Initialize Components
		this.initSkin();
		this.initMenuUI();
		this.initSounds();
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

	public void dispose() {
		
		// Skin
		skin.dispose();
		atlas.dispose();
		
		// Sounds
		sound_click.dispose();
	}

}
