package com.mygdx.sim.UI.Components.Navbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.mygdx.sim.UI.Components.DefaultButton;
import com.mygdx.sim.UI.Components.DefaultScrollPane;
import com.mygdx.sim.UI.Components.DefaultTextField;
import com.mygdx.sim.World.WorldController;
import com.mygdx.sim.World.WorldState;

public class Navbar extends Table {
	
	private WorldController worldController;

	// Navbar Actors
	private DefaultButton rewindButton;
	private DefaultButton playButton;
	private DefaultButton forwardButton;
	
	private Slider speedSlider;

	private Label tickLabel;
	private DefaultTextField tickTextField;

	public Navbar(Skin skin, final WorldController worldController) {
		
		// World Controller
		this.worldController = worldController;
		
		Table navTable = new Table();
		navTable.align(Align.left);
		
		// Rewind Button
		rewindButton = new DefaultButton("Rewind", skin, "default");
		rewindButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				worldController.setWorldState(WorldState.REWINDING);
			}
		});
		
		// Play Button
		playButton = new DefaultButton("Play", skin, "default");
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(worldController.getWorldState() != WorldState.PAUSED) {
					worldController.setWorldState(WorldState.PAUSED);
				} else {
					worldController.setWorldState(worldController.getPreviousWorldState());
				}
			}
		});
		
		// Forward Button
		forwardButton = new DefaultButton("Forward", skin, "default");
		forwardButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				worldController.setWorldState(WorldState.RUNNING);
			}
		});

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
	
	public void update() {
		tickLabel.setText("Tick: " + worldController.timestep);
		playButton.setText(worldController.getWorldState() == WorldState.PAUSED ? "Play" : "Pause");
	}
}
