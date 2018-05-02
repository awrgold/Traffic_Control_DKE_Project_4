package com.mygdx.sim.UI.Components.Sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.sim.Resources.Resources;

//TODO make image buttons and add images for tabs
public class SidebarTabButton extends TextButton {

	public SidebarTabButton(String text, Skin skin) {
		super(text, skin);
		
		this.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Resources.ui.sound_click.play();
			}

		});
	}
}
