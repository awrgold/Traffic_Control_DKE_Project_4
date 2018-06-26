package com.mygdx.sim.World.Components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.sim.GameObjects.LightState;
import com.mygdx.sim.GameObjects.Stoplight;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.Resources.Resources;

public class TrafficLight {
	
	private Stoplight stopLight;
	private float posX;
	private float posY;

	public TrafficLight(Stoplight stopLight) {
		this.stopLight = stopLight;
		Node node = stopLight.getParent();
		posX = node.getX();
		posY = node.getY();
	}
	
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw((stopLight.getLightState() == LightState.GREEN) ? Resources.ui.trafficLight_red :  Resources.ui.trafficLight_green , posX, posY);
	}
}
