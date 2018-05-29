package com.pacman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.input.Controller;

import methods.P;

public class PacMan extends Live {
	
	private int phase;
	
	private static final float speed = 60f;
	private static final float eatDistance = 35;
	private static final float killDistance = 100;
	
	private int mode;
	
	public static int eat = 0, die = 1;
	
	public PacMan(float x, float y) {
		super(x, y, 13, 13);
		setController(controller());
		this.phase = 1;
		super.setDelay(Entity.animationDelta);
	}
	
	@Override
	public float speed() {
		if (mode == die)
			return 0;
		return speed;
	}
	
	public void interact(Entity e, boolean secondCall) {
		if (Collectable.class.isAssignableFrom(e.getClass())) {
			if (dist(e) <= eatDistance)
				((Collectable) e).collect();
		}
		if (Ghost.is(e)) {
			if (dist(e) <= killDistance) {
				if (((Ghost) e).mode() == Ghost.chase) {
					mode = die;
					phase = 0;
					game().kill();
				}
				else
					return;
			}
		}
	}
	
	@Override
	public void updateState() {
		if (mode == eat)
			phase = (phase + 1) % 4;
		else
			phase = (phase + 1) % 11;
	}

	@Override
	public TextureRegion texture() {
		if (mode == eat)
			return super.pacmanTexture(dir(), phase);
		else
			return super.dyingTexture(phase);
	}
	
	public static Controller controller() {
		return new Controller() {
			private boolean a = false, d = false, s = false, w = false;
			private int dir = up;
			
			public int dir() {
				return dir;
			}
			
			public void setDir(int dir) {
				this.dir = dir;
			}
			
			public int update() {
				boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
				boolean d = Gdx.input.isKeyPressed(Input.Keys.D);
				boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
				boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
				
				boolean ca = a == true && this.a == false;
				boolean cd = d == true && this.d == false;
				boolean cs = s == true && this.s == false;
				boolean cw = w == true && this.w == false;
				
				if (ca)
					return dir = left;
				if (cd)
					return dir = right;
				if (cs)
					return dir = down;
				if (cw)
					return dir = up;
				return dir;
			}
			
			public int onTurn() {
				return dir;
			}
			
			public int onDecision() {
				return dir;
			}
		};
	}

}
