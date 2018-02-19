package com.mygdx.sim.UI.Components.Sidebar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.UI.Components.DefaultButton;
import com.mygdx.sim.World.WorldController;

public class SidebarConfigMenu extends Table {

	// World Controller
	private WorldController worldController;

	// Skin
	private Skin skin;
	
	// Label
	private Label lb_mapDimensions;

	// Text Fields
	private TextField tf_mapWidth;
	private TextField tf_mapHeight;

	// Buttons
	private DefaultButton submitMapDimensions;

	public SidebarConfigMenu(WorldController worldController) {

		// World Controller
		this.worldController = worldController;

		// Skin
		skin = Resources.ui.skin;

		// Create Menu
		createMenu();
	}

	private void createMenu() {
		
		// Labels
		lb_mapDimensions = new Label("Set Map Dimensions", skin);

		// Text Fields
		tf_mapWidth = new TextField("", skin);
		tf_mapHeight = new TextField("", skin);

		// Buttons
		submitMapDimensions = new DefaultButton("Submit", skin);

		submitMapDimensions.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				worldController.createMap(Integer.valueOf(tf_mapWidth.getText()), Integer.valueOf(tf_mapHeight.getText()));
			}

		});

		//this.add(lb_mapDimensions);
		this.add(tf_mapWidth).row();
		this.add(tf_mapHeight).row();
		this.add(submitMapDimensions).row();
	}

}
