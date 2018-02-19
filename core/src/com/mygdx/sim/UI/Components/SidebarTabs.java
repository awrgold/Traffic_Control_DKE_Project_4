package com.mygdx.sim.UI.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SidebarTabs extends Table {
	
	// Contents
	private ButtonGroup<Button> buttonGroup;
	private Stack actorStack;
	private Table table_tabs;
	
	public SidebarTabs() {
		buttonGroup = new ButtonGroup<Button>();
		actorStack = new Stack();
		table_tabs = new Table();
	}
	
	public void addTab(Button button, Actor actor) {
		
		// Add to Group|Stack
		buttonGroup.add(button);
		actorStack.add(actor);
		table_tabs.add(button);
		
		
		// Add to table
		this.clear();
		this.add(table_tabs).left().row();;
		this.add(actorStack);
	}
}
