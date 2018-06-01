package com.pacman.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.pacman.utils.ActionTimer.Action;

public class KeyAction implements InputProcessor {
	
	private Action a;
	
	public KeyAction(Action a) {
		this.a = a;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ENTER)
			a.act();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
}
