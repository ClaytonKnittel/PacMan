package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.utils.ActionTimer;

public class Fruit extends Entity implements Collectable {
	
	public static final int cherry = 0, strawberry = 1, orange = 2, apple = 3, melon = 4, galaxianBoss = 5, bell = 6, key = 7, none = 8;
	
	private int which;
	private boolean point;
	
	public Fruit(float x, float y) {
		super(x, y, 13, 13);
		which = none;
		point = false;
		setVisible(false);
	}
	
	public void collect() {
		if (which != none && !point) {
			game().add(value());
			point = true;
			game().addEvent(eatEvent());
		}
	}
	
	public int random() {
		double val = Math.random();
		if (val < 0.532049657968)
			return cherry;
		if (val < 0.709399543957)
			return strawberry;
		if (val < 0.815809475551)
			return orange;
		if (val < 0.891816569546)
			return apple;
		if (val < 0.945021535343)
			return melon;
		if (val < 0.971624018242)
			return galaxianBoss;
		if (val < 0.989359006841)
			return bell;
		return key;
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
		case none:
			return 0;
		}
		throw new IllegalStateException("There is no " + which + " fruit");
	}
	
	public ActionTimer fruitEvent() {
		ActionTimer a = new ActionTimer();
		a.loop();
		a.add(() -> pickRandom(), 20);
		a.add(() -> deleteFruit(), 25);
		return a;
	}
	
	private ActionTimer eatEvent() {
		ActionTimer a = new ActionTimer();
		a.add(() -> clear(), 1);
		return a;
	}
	
	private void clear() {
		point = false;
		deleteFruit();
	}
	
	private void deleteFruit() {
		if (point)
			return;
		super.setVisible(false);
		which = none;
	}
	
	private void pickRandom() {
		super.setVisible(true);
		which = random();
	}

	@Override
	public TextureRegion texture() {
		if (point)
			return super.fruitPointTexture(which);
		return super.fruitTexture(which);
	}

	@Override
	public void update(long delta) {
		return;
	}
	
}
