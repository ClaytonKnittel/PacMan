package com.pacman.entities;

import tensor.IVector2;

public class Clyde extends Ghost {
	
	private IVector2 startingPos;
	
	public Clyde(float x, float y) {
		super(x, y, 13, 13, Entity.clyde);
		startingPos = new IVector2(pos());
	}
	
	protected IVector2 chaseTarget() {
		return pacman().nextTile();
	}
	
	protected IVector2 boxTarget() {
		return startingPos;
	}
	
	protected IVector2 cornerTarget() {
		return IVector2.ZERO;
	}
}
