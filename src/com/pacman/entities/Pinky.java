package com.pacman.entities;

import tensor.IVector2;

public class Pinky extends Ghost {
	
	private IVector2 startingPos;
	private static final IVector2 cornerTarget = new IVector2(1, 1);
	
	public Pinky(float x, float y) {
		super(x, y, 13, 13, Entity.pinky);
		startingPos = new IVector2(pos());
	}
	
	protected IVector2 chaseTarget() {
		return board().pathFind(4, pacman().nextTile(), pacman().dir());
	}
	
	protected IVector2 boxTarget() {
		return startingPos;
	}
	
	protected IVector2 cornerTarget() {
		return cornerTarget;
	}
	
}
