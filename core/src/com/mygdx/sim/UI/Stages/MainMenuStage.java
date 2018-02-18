package com.mygdx.sim.UI.Stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.sim.UI.Menu.StartMenu;

public class MainMenuStage extends Stage {
	
	// UI Components
	private Table table_main;
	private Stack stack_menus;
	
	private Actor mnu_startMenu;
	private Actor mnu_editorMenu;
	private Actor mnu_settingsMenu;
	
	public MainMenuStage() {
		
		// Viewport | Camera
		super(new ScreenViewport());
		
		// Menus
		mnu_startMenu = new StartMenu(this);
		
		// Menu Stack
		stack_menus = new Stack();
		stack_menus.add(mnu_startMenu);
		
		// Table
		table_main = new Table();
		table_main.setFillParent(true);
		
		table_main.add(stack_menus);
		
		this.addActor(table_main);
		
		// Initial Menu
		mnu_startMenu.setVisible(true);
	}
	
	@Override
	public void draw() {
		super.draw();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height, true);
	}

}
