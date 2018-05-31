package com.pacman.entities;

import tensor.IVector2;

public class Blinky extends Ghost {
	
	private IVector2 startingPos;
	
	public Blinky(float x, float y) {
		super(x, y, 13, 13, Entity.blinky);
		startingPos = new IVector2(pos());
	}
	
	@Override
	public float speed() {
		float speed = super.speed();
		if (super.mode() != chase)
			return speed;
		int dots = game().numDots();
		int t1 = t1();
		int t2 = t1 / 2;
		if (dots <= t2)
			return speed + .1f * PacMan.speed;
		if (dots <= t1)
			return speed + .05f * PacMan.speed;
		return speed;
	}
	
	private int t1() {
		int level = game().level();
		if (level == 1)
			return 20;
		if (level == 2)
			return 30;
		if (level <= 5)
			return 40;
		if (level <= 8)
			return 50;
		if (level <= 11)
			return 60;
		if (level <= 14)
			return 80;
		if (level <= 18)
			return 100;
		return 120;
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
