package com.mygdx.sim.UI.Menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.UI.Stages.MainMenuStage;

public class StartMenu extends Table {

	// UI Components
	private MainMenuStage mainMenuStage;
	private Skin skin;

	public StartMenu(MainMenuStage mainMenuStage) {
		skin = Resources.ui.skin;
		this.mainMenuStage = mainMenuStage;

		// Add Components
		this.add(createStartButtonsTable()).center().expand().fill();
	}

	private Actor createStartButtonsTable() {

		// Start Button
		TextButton btn_start = new TextButton("Start", skin, "default");
		btn_start.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Start");
			}

		});

		// Editor Button
		TextButton btn_editor = new TextButton("Editor", skin, "default");
		btn_editor.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Editor");
			}
			
		});

		// Settings Button
		TextButton btn_settings = new TextButton("Settings", skin, "default");
		btn_settings.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Settings");
			}
			
		});
		
		Table tbl_buttons = new Table();
		
		tbl_buttons.defaults().center().space(60f);
		
		tbl_buttons.add(btn_start);
		tbl_buttons.add(btn_editor);
		tbl_buttons.add(btn_settings);
		
		
		return tbl_buttons;
	}

}
