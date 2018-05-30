package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fruit extends Entity implements Collectable {
	
	public static final int cherry = 0, strawberry = 1, orange = 2, apple = 3, melon = 4, galaxianBoss = 5, bell = 6, key = 7;
	
	private int which;
	
	public Fruit(float x, float y, int which) {
		super(x, y, 13, 13);
		this.which = which;
	}
	
	public void collect() {
		delete();
		game().add(value());
	}
	
	public int value() {
		switch(which) {
		case cherry:
			return 100;
		case strawberry:
			return 300;
		case orange:
			return 500;
		case apple:
			return 700;
		case melon:
			return 1000;
		case galaxianBoss:
			return 2000;
		case bell:
			return 3000;
		case key:
			return 5000;
		}
		throw new IllegalStateException("There is no " + which + " fruit");
	}

	@Override
	public TextureRegion texture() {
		return super.fruitTexture(which);
	}

	@Override
	public void update(long delta) {
		return;
	}
	
}
