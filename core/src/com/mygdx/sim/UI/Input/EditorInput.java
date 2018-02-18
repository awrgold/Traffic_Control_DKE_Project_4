package com.mygdx.sim.UI.Input;

import com.badlogic.gdx.InputAdapter;
import com.mygdx.sim.UI.Stages.EditorStage;
import com.mygdx.sim.World.WorldController;

public class EditorInput extends InputAdapter {

	// World Controller
	private WorldController worldController;

	// UI
	private EditorStage editorStage;

	public EditorInput(EditorStage editorStage) {

		// UI
		this.editorStage = editorStage;

		// World Controller
		worldController = editorStage.getWorldController();

	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return true;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return true;
	}

}
