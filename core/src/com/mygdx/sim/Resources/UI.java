package com.mygdx.sim.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UI {

	// Skin
	public Skin skin;
	//public TextureAtlas atlas;

	// Menu Textures
	public Texture texture_menu_background;

	// Menu Sprites
	public Sprite menu_icon_start;
	public Sprite menu_icon_editor;
	public Sprite menu_icon_settings;

	public UI() {

		// Create Atlas
		// atlas = new TextureAtlas(Gdx.files.internal("assets/images/ui/ui.atlas"));

		// Initialize Components
		this.initSkin();
		this.initMenuUI();
	}

	private void initSkin() {

		// Create Skin
		skin = new Skin();
		// skin.addRegions(atlas);

		// Read Skin
		skin.load(Gdx.files.internal("resources/skins/ui.json"));
	}

	private void initMenuUI() {

		// Menu Icons
		// menu_icon_start = atlas.createSprite("");
		// menu_icon_editor = atlas.createSprite("");
		// menu_icon_settings = atlas.createSprite("");
	}

	public void dispose() {
		
		// Skin
		skin.dispose();
		//atlas.dispose();
	}

}
