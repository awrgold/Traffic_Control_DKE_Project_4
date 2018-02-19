package com.mygdx.sim.UI.Components.Sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.sim.UI.Components.DefaultButton;
import com.mygdx.sim.UI.Components.DefaultScrollPane;

public class SidebarPanel extends Table {
	
	// Dimensions
	public static final float WIDTH = 362f;
	
	public SidebarPanel(Skin skin) {
		
		this.add(new DefaultScrollPane(new DefaultButton("Banana", skin), skin));
		this.pack();
		this.setVisible(true);
	}
	
	public void resize(int width, int height) {
		this.setSize(WIDTH, height);
	}
}
