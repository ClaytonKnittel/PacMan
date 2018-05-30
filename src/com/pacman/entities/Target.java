package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tensor.IVector2;

public class Target extends Entity {

	public Target(float x, float y) {
		super(x, y, 13, 13);
	}
	
	public void set(int x, int y) {
		super.setPos(x, y);
	}
	
	@Override
	public TextureRegion texture() {
		return super.getTexture(44);
	}
	
	public IVector2 target() {
		return pos();
	}

	@Override
	public void update(long delta) {
		return;
	}

}
