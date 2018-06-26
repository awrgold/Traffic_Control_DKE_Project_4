package com.mygdx.sim.GameObjects.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.LightState;
import com.mygdx.sim.GameObjects.Stoplight;

public class LightController {

	private int interval;
	private final int FINALINTERVAL = 100;
	private final int WAITINTERVAL = 25;
	private ControlScheme scheme;
	private List<Stoplight> lights;
	private List<Stoplight> pausedLights;
	private boolean startsGreen;

	public LightController(List<Stoplight> lights) {
		this.lights = lights;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public List<Stoplight> getLights() {
		return lights;
	}

	public void setStartsGreen(Boolean b) {
		this.startsGreen = b;
		if (b) {
			for (Stoplight l : lights) {
				l.setLightState(LightState.GREEN);
			}
		} else {
			for (Stoplight l : lights) {
				l.setLightState(LightState.RED);
			}
		}
	}

	public boolean isStartsGreen() {
		return startsGreen;
	}

	public List<Stoplight> getReds(){
		List<Stoplight> reds = new ArrayList<Stoplight>();
		for (Stoplight l : lights){
			if (l.getLightState().equals(LightState.RED)){
				reds.add(l);
			}
		}
		return reds;
	}

	public List<Stoplight> getGreens(){
		List<Stoplight> greens = new ArrayList<Stoplight>();
		for (Stoplight l : lights){
			if (l.getLightState().equals(LightState.GREEN)){
				greens.add(l);
			}
		}
		return greens;
	}

	public void setScheme(ControlScheme scheme) {
		this.scheme = scheme;
		if (scheme.equals(ControlScheme.BASIC)) {
			this.interval = FINALINTERVAL;
		}
	}

	public void setLights(List<Stoplight> lights) {
		this.lights = lights;
	}

	public int getTimeRemaining(int currentTimestep) {
		return interval - (currentTimestep % interval);
	}

	public void switchLights(List<Stoplight> lightsToSwitch) {
		for (Stoplight l : lightsToSwitch) {
			l.switchLight();
		}
	}

	public void update(int currentTimestep) {
		if (getTimeRemaining(currentTimestep) == interval) {
			switchLights(lights);
		}
	}

}