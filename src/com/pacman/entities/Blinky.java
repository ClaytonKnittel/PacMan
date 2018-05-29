package com.pacman.entities;

public class Blinky extends Ghost {
	
	private int startingPos;
	
	public Blinky(float x, float y) {
		super(x, y, 13, 13, Entity.blinky);
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
