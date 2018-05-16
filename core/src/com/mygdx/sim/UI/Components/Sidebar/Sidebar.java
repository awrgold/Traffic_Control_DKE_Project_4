package com.mygdx.sim.UI.Components.Sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.sim.UI.Components.DefaultScrollPane;
import com.mygdx.sim.World.WorldController;

public class Sidebar extends Table {

	// Sidebar Tabs
	private SidebarTabs sidebarTabs;

	// Sidebar Menus
	private SidebarConfigMenu mnu_config;

	public Sidebar(Skin skin, WorldController worldController) {
		
		// Menus
		mnu_config = new SidebarConfigMenu(worldController);

		sidebarTabs = new SidebarTabs();
		sidebarTabs.addTab(new SidebarTabButton("A", skin), mnu_config);
		sidebarTabs.addTab(new SidebarTabButton("B", skin), new Table());
		sidebarTabs.addTab(new SidebarTabButton("C", skin), new Table());

		this.add(new DefaultScrollPane(sidebarTabs, skin)).grow();
		this.pack();
		this.setVisible(true);
	}

	public void resize(int width, int height) {
		this.setSize(width / 3, height);
	}
}
