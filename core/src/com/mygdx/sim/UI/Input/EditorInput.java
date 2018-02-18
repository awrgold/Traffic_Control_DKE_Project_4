package com.mygdx.sim.UI.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.sim.UI.Stages.EditorStage;
import com.mygdx.sim.World.WorldCamera;
import com.mygdx.sim.World.WorldController;

public class EditorInput extends InputAdapter {

	// World Controller
	private WorldController worldController;

	// UI
	private EditorStage editorStage;
	
	// Scrolling
	private boolean scrollEnabled;
	
	// Mouse Variables
	private static Vector2 mousePos_screen;
	private static Vector2 scrollCenter;

	public EditorInput(EditorStage editorStage) {

		// UI
		this.editorStage = editorStage;

		// World Controller
		worldController = editorStage.getWorldController();
		
		// Mouse Variables
		mousePos_screen = new Vector2();
		scrollCenter = new Vector2();
		
		this.update();
	}
	
	public void update() {
		updateMouse();
		updateScrolling();
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(button == Buttons.MIDDLE) {
			startScroll(x, y);
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(button == Buttons.MIDDLE) {
			stopScroll();
		}
		return true;
	}
	
	@Override
	public boolean scrolled(int scroll) {
		this.mouseMoved(Gdx.input.getX(), Gdx.input.getY());
		
		if(scroll == -1) {
			worldController.getWorldCamera().zoom -= 0.1f;
		} else {
			worldController.getWorldCamera().zoom += 0.1f;
		}
		
		return true;
	}
	
	private void startScroll(int x, int y) {
		scrollEnabled = true;
		scrollCenter.x = x;
		scrollCenter.y = y;
	}
	
	private void stopScroll() {
		scrollEnabled = false;
	}
	
	private void updateMouse() {
		
		// Mouse Coordinates
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		
		// Screen Mouse Coordinates
		mousePos_screen.x = x;
		mousePos_screen.y = Gdx.graphics.getHeight() - y;
	}
	
	private void updateScrolling() {
		if(scrollEnabled == true) {
			
			// Camera
			WorldCamera worldCamera = worldController.getWorldCamera();
			
			float speed = worldCamera.zoom * 3f;
			float x = (mousePos_screen.x - scrollCenter.x) * speed * Gdx.graphics.getDeltaTime();
			float y = (mousePos_screen.y - scrollCenter.y) * speed * Gdx.graphics.getDeltaTime();
			
			worldCamera.move(x, y);
		}
	}

}
