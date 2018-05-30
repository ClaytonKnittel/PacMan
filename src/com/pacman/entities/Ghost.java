package com.pacman.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.input.Controller;
import com.pacman.utils.ActionList;
import com.pacman.utils.ActionTimer;

import tensor.IVector2;

public abstract class Ghost extends Live {
	
	private int phase;
	private int mode;
	private int name;
	private boolean dead;
	
	public static final int chase = 0, scared = 1, eyes = 2, box = 3, stop = 4, point = 5;
	
	private static final float speed = 65f;
	
	private static int points;

	public Ghost(float x, float y, float width, float height, int name) {
		super(x, y, width, height);
		super.setDir(Entity.down);
		super.setController(controller());
		super.setAnimation(animation());
		phase = 0;
		mode = box;
		this.name = name;
		dead = false;
	}
	
	@Override
	public float speed() {
		if (mode == box)
			return speed / 2;
		if (mode == stop)
			return 0;
		if (mode == scared)
			return speed / 2;
		if (mode == eyes)
			return speed * 2;
		return speed;
	}
	
	public int mode() {
		return mode;
	}
	
	public void setMode(int mode) {
		if (dead)
			return;
		this.mode = mode;
	}
	
	public void setScareMode(float seconds) {
		this.mode = scared;
		turnAround();
		createScareAnimation(seconds);
	}
	
	public static void resetPoints() {
		points = 0;
	}
	
	public void die() {
		mode = point;
		points++;
		dead = true;
		game().add(valueOf(points));
	}
	
	private static int valueOf(int points) {
		switch(points) {
		case 1:
			return 200;
		case 2:
			return 400;
		case 3:
			return 800;
		case 4:
			return 1600;
		}
		throw new IllegalArgumentException(points + " is not valid point value of Ghost");
	}
	
	protected Live pacman() {
		return game().pacman();
	}
	
	public static boolean is(Object o) {
		return Ghost.class.isAssignableFrom(o.getClass());
	}
	
	private ActionList animation() {
		ActionList animation = new ActionList();
		animation.loop();
		animation.add(() -> phase++);
		animation.add(() -> phase--);
		return animation;
	}
	
	private void createScareAnimation(float seconds) {
		ActionTimer a = new ActionTimer();
		a.add(() -> phase += 2, seconds - 1.75f);
		a.add(() -> phase -= 2, .25f);
		a.add(() -> phase += 2, .25f);
		a.add(() -> phase -= 2, .25f);
		a.add(() -> phase += 2, .25f);
		a.add(() -> phase -= 2, .25f);
		a.add(() -> phase += 2, .25f);
		a.add(() -> phase -= 2, .25f);
		game().eventList().add(a);
	}
	
	public void reset() {
		setPos(boxTarget());
		mode = box;
		dead = false;
		points = 0;
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
				if (dead)
					testReturned(target());
				return dir = turn();
			}

			@Override
			public int onDecision() {
				int dir;
				IVector2 target = target();
				if (target == null)
					return board().randomDir(pos());
				if (dead && testReturned(target))
					return onDecision();
				try {
					dir = Entity.opposite(graph().shortestPath(pos(), target).getFirst().dir());
				} catch (IllegalStateException e) {
					dir = (int) (Math.random() * 4);
				}
				return this.dir = dir;
			}
		};
	}
	
	private boolean testReturned(IVector2 target) {
		if (target.equals(pos())) {
			returnedBox();
			return true;
		}
		return false;
	}
	
	private void returnedBox() {
		mode = chase;
		dead = false;
	}
	
	public IVector2 target() {
		switch(mode) {
		case chase:
		case stop:
			return chaseTarget();
		case scared:
		case point:
			return null;
		case eyes:
		case box:
			return boxTarget();
		}
		throw new IllegalStateException("Ghost cannot be in state " + mode);
	}
	
	protected abstract IVector2 chaseTarget();
	
	protected abstract IVector2 boxTarget();
	
	protected abstract IVector2 cornerTarget();
	
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
		case point:
			if (!game().paused())
				mode = eyes;
			return pointsTexture(points);
		}
		return null;
	}
	
}
