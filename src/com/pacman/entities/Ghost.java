package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.input.Controller;

import methods.P;

public abstract class Ghost extends Live {
	
	private int phase;
	private int mode;
	private int name;
	
	public static final int chase = 0, scared = 1, eyes = 2, box = 3, stop = 4;
	
	private static final float speed = 65f;

	public Ghost(float x, float y, float width, float height, int name) {
		super(x, y, width, height);
		super.setDir(Entity.down);
		super.setController(controller());
		phase = 0;
		mode = box;
		this.name = name;
	}
	
	@Override
	public float speed() {
		if (mode == box)
			return speed / 2;
		if (mode == stop)
			return 0;
		return speed;
	}
	
	public int mode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
		if (mode == scared)
			turnAround();
	}
	
	protected Live pacman() {
		return game().pacman();
	}
	
	public static boolean is(Object o) {
		return Ghost.class.isAssignableFrom(o.getClass());
	}
	
	@Override
	public void updateState() {
		phase = phase == 0 ? 1 : 0;
	}
	
	private Controller controller() {
		return new Controller() {
			private int dir = up;
			
			@Override
			public int dir() {
				return dir;
			}
			
			@Override
			public void setDir(int dir) {
				this.dir = dir;
			}

			@Override
			public int update() {
				return dir;
			}
			
			@Override
			public int onTurn() {
				if (mode == box)
					return onDecision();
				return dir = turn();
			}

			@Override
			public int onDecision() {
				int dir;
				int target = target();
				if (target == -1)
					return board().randomDir(tile());
				try {
					dir = Entity.opposite(graph().shortestPath(tile(), target).getFirst().dir());
				} catch (IllegalStateException e) {
					dir = (int) (Math.random() * 4);
				}
				return this.dir = dir;
			}
		};
	}
	
	public int target() {
		switch(mode) {
		case chase:
		case stop:
			return chaseTarget();
		case scared:
			return -1;
		case eyes:
		case box:
			return boxTarget();
		}
		throw new IllegalStateException("Ghost cannot be in state " + mode);
	}
	
	protected abstract int chaseTarget();
	
	protected abstract int boxTarget();
	
	protected abstract int cornerTarget();
	
	@Override
	public TextureRegion texture() {
		switch(mode) {
		case chase:
		case box:
			return idleTexture(name, dir(), phase);
		case scared:
			return scaredTexture(phase);
		case eyes:
			return eyeTexture(dir());
		}
		return null;
	}
	
}
