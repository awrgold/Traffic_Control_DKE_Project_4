package com.mygdx.sim.UI.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.sim.Resources.Resources;

public class DefaultButton extends TextButton {

	public DefaultButton(String text, Skin skin) {
		super(text, skin);

		this.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Resources.ui.sound_click.play();
			}

		});
	}

	public DefaultButton(String text, Skin skin, String style) {
		super(text, skin, style);

		this.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Resources.ui.sound_click.play();
			}

		});
	}

}
