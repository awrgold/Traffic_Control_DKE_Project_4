package com.mygdx.sim.Resources;

public class Resources {
	
	// World
	public static final World world = new World();

	// UI
	public static final UI ui = new UI();
	
	public static void dispose() {
		ui.dispose();
		world.dispose();
	}
}
