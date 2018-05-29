package com.pacman.entities;

public class Clyde extends Ghost {
	
	private int startingPos;
	
	public Clyde(float x, float y) {
		super(x, y, 13, 13, Entity.clyde);
		startingPos = board().boardPos(x, y);
	}
	
	protected int chaseTarget() {
		return pacman().nextTile();
	}
	
	protected int boxTarget() {
		return startingPos;
	}
	
	protected int cornerTarget() {
		return 0;
	}
}
