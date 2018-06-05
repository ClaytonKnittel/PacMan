package com.pacman.entities;

import tensor.IVector2;

public class Clyde extends Ghost {
	
	private IVector2 startingPos;
	private static final IVector2 cornerTarget = new IVector2(1, 29);
	
	public Clyde(float x, float y) {
		super(x, y, 13, 13, Entity.clyde);
		startingPos = new IVector2(pos());
	}
	
	protected IVector2 chaseTarget() {
		IVector2 p = pacman().nextTile();
		if (p.minus(pos()).mag2() < 64)
			return cornerTarget();
		return p;
	}
	
	protected IVector2 boxTarget() {
		return startingPos;
	}
	
	protected IVector2 cornerTarget() {
		return cornerTarget;
	}
}
