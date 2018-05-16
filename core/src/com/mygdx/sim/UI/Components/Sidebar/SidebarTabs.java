package com.mygdx.sim.UI.Components.Sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SidebarTabs extends Table {

	// Contents
	private ButtonGroup<Button> buttonGroup;
	private Stack actorStack;
	private Table table_tabs;

	public SidebarTabs() {

		// Set Position
		this.top().left();

		// Contents
		buttonGroup = new ButtonGroup<Button>();
		actorStack = new Stack();
		table_tabs = new Table();
	}

	public void addTab(final Button button, Actor actor) {

		// Click Action
		button.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {

				// Set Active Tab
				setActiveTab(button);
				SidebarTabs.this.fire(new ChangeListener.ChangeEvent());
			}

		});

		// Add to Group|Stack
		buttonGroup.add(button);
		actorStack.add(actor);
		table_tabs.add(button);

		// Add to table
		this.clear();
		this.add(table_tabs).left().row();
		this.add(actorStack);
	}

	public void removeTab(Actor actor) {
		for (int i = 0; i < actorStack.getChildren().size; i++) {
			if (actorStack.getChildren().get(i).equals(actor)) {

				// Remove Button
				Button button = buttonGroup.getButtons().get(i);
				buttonGroup.remove(button);
				button.remove();

				// Remove Actor
				actorStack.getChildren().get(i).remove();
			}
		}
	}

	public void setActiveTab(int buttonIndex) {
		this.setActiveTab(buttonGroup.getButtons().get(buttonIndex));
	}

	private void setActiveTab(Button tab) {
		int actorIndex = 0;

		for (int i = 0; i < buttonGroup.getButtons().size; i++) {
			if (buttonGroup.getButtons().get(i) == tab) {
				buttonGroup.getButtons().get(i).setChecked(true);
				actorIndex = i;
			}
		}

		for (int i = 0; i < actorStack.getChildren().size; i++) {
			actorStack.getChildren().get(i).setVisible(false);

			if (actorIndex == i) {
				actorStack.getChildren().get(actorIndex).setVisible(true);
			}
		}
	}
}
