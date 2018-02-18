package com.mygdx.sim.UI.Menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.sim.Resources.Resources;
import com.mygdx.sim.UI.Stages.MainMenuStage;

public class EditorMenu extends Table {

	// UI Components
	private MainMenuStage mainMenuStage;
	private Skin skin;

	public EditorMenu(MainMenuStage mainMenuStage) {
		skin = Resources.ui.skin;
		this.mainMenuStage = mainMenuStage;

		// Add Components
	}
}
