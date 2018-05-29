package com.pacman.entities;

public class Pinky extends Ghost {
	
	private int startingPos;
	
	public Pinky(float x, float y) {
		super(x, y, 13, 13, Entity.pinky);
		startingPos = board().boardPos(x, y);
	}
	
	protected int chaseTarget() {
		return board().pathFind(2, pacman().nextTile(), pacman().dir());
	}
	
	protected int boxTarget() {
		return startingPos;
	}
	
	protected int cornerTarget() {
		return 0;
	}
	
}
