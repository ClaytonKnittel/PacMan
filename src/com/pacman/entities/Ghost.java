package com.pacman.entities;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.graphics.Board;
import com.pacman.graphics.Path;
import com.pacman.input.Controller;
import com.pacman.utils.ActionList;
import com.pacman.utils.ActionTimer;

import tensor.IVector2;
import tensor.Vector2;

public abstract class Ghost extends Live {
	
	private int phase;
	private int mode;
	private int name;
	private boolean dead;
	
	public static final int chase = 0, scared = 1, eyes = 2, box = 3, stop = 4, point = 5, hide = 6;
	
	private static int points;
	
	private Path path;
	
	private TextureRegion lastTexture;

	public Ghost(float x, float y, float width, float height, int name) {
		super(x, y, width, height);
		super.setDir(Entity.down);
		super.setController(controller());
		super.setAnimation(animation());
		phase = 0;
		mode = box;
		this.name = name;
		dead = false;
		
		Color c;
		switch(name) {
		case Entity.blinky:
			c = Entity.blinkyColor;
			break;
		case Entity.pinky:
			c = Entity.pinkyColor;
			break;
		case Entity.inky:
			c = Entity.inkyColor;
			break;
		case Entity.clyde:
			c = Entity.clydeColor;
			break;
		default:
			c = Color.WHITE;
		}
		path = new Path(c);
	}
	
	@Override
	public float speed() {
		if (mode == box)
			return speed_() / 2;
		if (mode == stop)
			return 0;
		if (mode == scared)
			return speed_() / 2;
		if (mode == eyes)
			return speed_() * 2;
		return speed_();
	}
	
	private float speed_() {
		if (game().level() == 1)
			return .75f * PacMan.speed;
		if (game().level() < 5)
			return .85f * PacMan.speed;
		return .95f * PacMan.speed;
	}
	
	public void stop() {
		mode = stop;
	}
	
	public int mode() {
		return mode;
	}
	
	public void setMode(int mode) {
		if (dead || this.mode == stop)
			return;
		this.mode = mode;
	}
	
	public void setModeIfNot(int mode, int not) {
		if (mode != not)
			setMode(mode);
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
		game().addEvent(a);
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
					LinkedList<Board.WeightDir> path = graph().shortestPath(pos(), target, prevTile());
					dir = Entity.opposite(path.getFirst().dir());
					createPath(path);
				} catch (IllegalStateException e) {
					dir = (int) (Math.random() * 4);
				}
				return this.dir = dir;
			}
		};
	}
	
	private void createPath(LinkedList<Board.WeightDir> l) {
		path.clear();
		IVector2 pos = new IVector2(pos());
		path.add(board().screenPos(pos));
		for (Board.WeightDir d : l) {
			pos = board().newPos(pos, Entity.opposite(d.dir()));
			path.add(board().screenPos(pos));
		}
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
			if (!board().isPermeable(pos()))
				return chaseTarget();
		case point:
			return null;
		case hide:
			return cornerTarget();
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
	public void draw(Batch batch) {
		if (!visible())
			return;
		TextureRegion t = texture();
		if (t == null)
			t = lastTexture;
		else
			lastTexture = t;
		
		Vector2 pos = screenPos().minus(t.getRegionWidth() / 2, t.getRegionHeight() / 2);
		batch.draw(t, pos.x(), pos.y());
		
		if (board().offScreen(pos(), dir())) {
			if (vertDir(dir()))
				pos = board().reflectY(pos);
			else
				pos = board().reflectX(pos);
			batch.draw(t, pos.x(), pos.y());
		}
		path.drawDistLess(batch, 30);
	}
	
	@Override
	public TextureRegion texture() {
		switch(mode) {
		case chase:
		case box:
		case hide:
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
