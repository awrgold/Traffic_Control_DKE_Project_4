package com.mygdx.sim.UI.Components.Navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.sim.UI.Components.DefaultButton;
import com.mygdx.sim.UI.Components.DefaultScrollPane;
import com.mygdx.sim.UI.Components.DefaultTextField;
import com.mygdx.sim.World.WorldController;

public class Navbar extends Table {

	// Navbar Actors
	private DefaultButton rewindButton;
	private DefaultButton playButton;
	private DefaultButton forwardButton;
	
	private Slider speedSlider;

	private Label tickLabel;
	private DefaultTextField tickTextField;

	public Navbar(Skin skin, WorldController worldController) {
		
		Table navTable = new Table();
		navTable.align(Align.left);
		
		rewindButton = new DefaultButton("Rewind", skin, "default");
		playButton = new DefaultButton("Play", skin, "default");
		forwardButton = new DefaultButton("Forward", skin, "default");
		
		tickLabel = new Label("Tick: 0", skin);
		
		tickTextField = new DefaultTextField("", skin);
		
		navTable.add(rewindButton).padRight(10);
		navTable.add(playButton).padRight(10);
		navTable.add(forwardButton).padRight(10);
		navTable.add(tickLabel).padRight(10);
		navTable.add(tickTextField);
		
		this.add(new DefaultScrollPane(navTable, skin)).grow();
		this.pack();
		this.setVisible(true);
	}

	public void resize(int width, int height) {
		this.setSize(width, height / 10);
		this.setPosition(0, height - this.getHeight());
	}
}
