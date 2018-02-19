package com.mygdx.sim.UI.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DefaultScrollPane extends ScrollPane {
	
	public DefaultScrollPane(Actor actor, Skin skin) {
		super(actor, skin);
		this.setScrollingDisabled(true, true);
	}
}
