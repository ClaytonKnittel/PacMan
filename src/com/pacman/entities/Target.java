package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Target extends Entity {

	public Target(float x, float y) {
		super(x, y, 13, 13);
	}
	
	public void set(int tile) {
		super.setTile(tile);
	}
	
	@Override
	public TextureRegion texture() {
		return super.getTexture(44);
	}
	
	public int target() {
		return board().boardPos(x(), y());
	}

	@Override
	public void update(long delta) {
		return;
	}

}
