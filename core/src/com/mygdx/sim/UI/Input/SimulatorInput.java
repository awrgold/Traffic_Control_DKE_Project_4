package com.mygdx.sim.UI.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.sim.UI.Stages.SimulatorStage;
import com.mygdx.sim.World.WorldCamera;
import com.mygdx.sim.World.WorldController;

public class SimulatorInput extends InputAdapter {

	// World Controller
	private WorldController worldController;

	// UI
	private SimulatorStage simulatorStage;

	// Scrolling
	private boolean scrollEnabled;

	// Mouse Variables
	private Vector2 mousePos_screen;
	private Vector2 scrollCenter;

	public SimulatorInput(SimulatorStage simulatorStage) {

		// UI
		this.simulatorStage = simulatorStage;

		// World Controller
		worldController = simulatorStage.getWorldController();

		// Mouse Variables
		mousePos_screen = new Vector2();
		scrollCenter = new Vector2();
		
		// Bounds
		Rectangle bounds = worldController.getBounds();
		
		// Center Camera
		worldController.getWorldCamera().move(bounds.width / 4, bounds.height / 4);

		this.update();
	}

	public void update() {
		updateMouse();
		updateScrolling();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (button == Buttons.LEFT) {
			startScroll(mousePos_screen);
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (button == Buttons.LEFT) {
			stopScroll();
		}
		return true;
	}

	@Override
	public boolean scrolled(int scroll) {
		this.mouseMoved(Gdx.input.getX(), Gdx.input.getY());

		if (scroll == -1) {
			worldController.getWorldCamera().zoomOut();
		} else {
			worldController.getWorldCamera().zoomIn();
		}

		return true;
	}

	private void startScroll(Vector2 scrollCenter) {
		scrollEnabled = true;
		this.scrollCenter.x = scrollCenter.x;
		this.scrollCenter.y = scrollCenter.y;
		simulatorStage.setScrollCenterPos(this.scrollCenter);
	}

	private void stopScroll() {
		scrollEnabled = false;
		simulatorStage.setScrollCenterPos(null);
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
		if (scrollEnabled == true) {

			// Camera
			WorldCamera worldCamera = worldController.getWorldCamera();

			float speed = worldCamera.zoom * 3f;
			float x = (mousePos_screen.x - scrollCenter.x) * speed * Gdx.graphics.getDeltaTime();
			float y = (mousePos_screen.y - scrollCenter.y) * speed * Gdx.graphics.getDeltaTime();

			worldCamera.move(x, y);

			Vector3 worldCameraPos = worldCamera.position;
			float buffer = worldCamera.zoom * 100f;
			float width = worldCamera.zoom * worldCamera.viewportWidth;
			float height = worldCamera.zoom * worldCamera.viewportHeight;
			float boundsWidth = worldController.getBounds().width;
			float boundsHeight = worldController.getBounds().height;

			if (worldCameraPos.x < -width / 2f + buffer)
				worldCameraPos.x = -width / 2f + buffer;
			if (worldCameraPos.y < -height / 2f + buffer)
				worldCameraPos.y = -height / 2f + buffer;
			if (worldCameraPos.x > width / 2f - buffer + boundsWidth)
				worldCameraPos.x = width / 2f - buffer + boundsWidth;
			if (worldCameraPos.y > height / 2f - buffer + boundsHeight)
				worldCameraPos.y = height / 2f - buffer + boundsHeight;
		}
	}

}
