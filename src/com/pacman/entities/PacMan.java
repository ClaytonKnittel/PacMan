package com.pacman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.input.Controller;
import com.pacman.utils.ActionList;

import tensor.IVector2;

public class PacMan extends Live {
	
	private int phase;
	
	private IVector2 startingPos;
	
	public static final float speed = 60f;
	private static final float eatDistance = 80;
	private static final float killDistance = 100;
	
	private int mode;
	
	private int lives;
	
	public static final int eat = 0, die = 1, stop = 2;
	
	public PacMan(float x, float y) {
		super(x, y, 13, 13);
		startingPos = new IVector2(pos());
		setController(controller());
		phase = 1;
		super.setDelay(Entity.animationDelta);
		eatMode();
		lives = 3;
	}
	
	public void stop() {
		mode = stop;
	}
	
	private void eatMode() {
		this.mode = eat;
		setAnimation(eat());
	}
	
	private void kill() {
		mode = die;
		phase = 0;
		game().kill();
		super.setAnimation(die());
		lives--;
	}
	
	public int lives() {
		return lives;
	}
	
	public void addLife() {
		lives++;
	}
	
	@Override
	public float speed() {
		if (mode == die || mode == stop)
			return 0;
		return speed;
	}
	
	public void reset() {
		setPos(startingPos);
		setD(0);
		setDir(up);
		controller().setDir(up);
		eatMode();
		setVisible(true);
	}
	
	public void interact(Entity e, boolean secondCall) {
		if (Collectable.class.isAssignableFrom(e.getClass())) {
			if (dist(e) <= eatDistance)
				((Collectable) e).collect();
		}
		if (Ghost.is(e)) {
			if (dist(e) <= killDistance) {
				Ghost g = (Ghost) e;
				if (g.mode() == Ghost.chase) {
					kill();
				}
				else if (g.mode() == Ghost.scared) {
					game().pause(500);
					g.die();
				}
			}
		}
	}
	
	private ActionList eat() {
		ActionList actions = new ActionList();
		actions.loop();
		actions.add(() -> phase = 0);
		actions.add(() -> phase = 1);
		actions.add(() -> phase = 2);
		actions.add(() -> phase = 1);
		return actions;
	}
	
	private ActionList die() {
		ActionList actions = new ActionList();
		actions.add(() -> phase = 0, 6);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> phase++, 1);
		actions.add(() -> setVisible(false), 1);
		return actions;
	}
	
	@Override
	public TextureRegion texture() {
		switch (mode) {
		case eat:
			return super.pacmanTexture(dir(), phase);
		case die:
			return super.dyingTexture(phase);
		case stop:
			return super.pacmanTexture(dir(), 2);
		}
		throw new IllegalStateException("Pacman cannot be in mode " + mode);
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
				boolean a = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
				boolean d = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
				boolean s = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
				boolean w = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
				
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
