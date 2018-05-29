package com.pacman;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pacman.entities.Blinky;
import com.pacman.entities.Clyde;
import com.pacman.entities.Entity;
import com.pacman.entities.Ghost;
import com.pacman.entities.Inky;
import com.pacman.entities.PacMan;
import com.pacman.entities.Pinky;
import com.pacman.entities.Target;
import com.pacman.graphics.Board;
import com.pacman.utils.ActionTimer;

import methods.P;
import structures.CategorySet;
import structures.LList;

public class Game {
	
	private Board board;
	private LList<Entity> entities;
	private CategorySet<Entity> tiles;
	private Target target;
	public static Entity tar;
	private PacMan pacman;
	private Ghost pinky, blinky, inky, clyde;
	
	private ActionTimer events;
	
	private long lastTime;
	
	public static final int chase = 0, hide = 1, blue = 2, dead = 3;
	private int mode;
	
	public Game(int width, int height) {
		entities = new LList<Entity>();
		board = new Board(width, height);
		
		tiles = new CategorySet<>(board.size(), e -> e.getTiles());
		
		Entity.setGame(this);
		
		start();
		
		target = new Target(100, 100);
		
		lastTime = System.currentTimeMillis();
	}
	
	public Board board() {
		return board;
	}
	
	public CategorySet<Entity> tiles() {
		return tiles;
	}
	
	private void genEntities() {
		for (Entity e : board.genEntities()) {
			if (e instanceof PacMan)
				this.pacman = (PacMan) e;
			else if (e instanceof Pinky)
				this.pinky = (Ghost) e;
			else if (e instanceof Blinky)
				this.blinky = (Ghost) e;
			else if (e instanceof Inky)
				this.inky = (Ghost) e;
			else if (e instanceof Clyde)
				this.clyde = (Ghost) e;
			entities.add(e);
			tiles.add(e);
		}
	}
	
	public void start() {
		mode = chase;
		entities.clear();
		tiles.clear();
		genEntities();
		
		events = new ActionTimer();
		events.add(() -> blinky.setMode(Ghost.chase), 1);
		events.add(() -> pinky.setMode(Ghost.chase), 1);
		events.add(() -> inky.setMode(Ghost.chase), 1);
		events.add(() -> clyde.setMode(Ghost.chase), 1);
	}
	
	public void scare() {
		mode = blue;
		P.pl("FE");
		pinky.setMode(Ghost.scared);
		blinky.setMode(Ghost.scared);
		inky.setMode(Ghost.scared);
		clyde.setMode(Ghost.scared);
	}
	
	public void kill() {
		mode = dead;
		pinky.setMode(Ghost.stop);
		blinky.setMode(Ghost.stop);
		inky.setMode(Ghost.stop);
		clyde.setMode(Ghost.stop);
	}
	
	public PacMan pacman() {
		return pacman;
	}
	
	public Ghost pinky() {
		return pinky;
	}
	
	public Ghost blinky() {
		return blinky;
	}
	
	public Ghost inky() {
		return inky;
	}
	
	public Ghost clyde() {
		return clyde;
	}
	
	public void draw(Batch batch) {
		batch.draw(board.texture(), 0, 0);
		long now = System.currentTimeMillis();
		
		events.check();

		for (Entity e : entities.delIter(e -> e.deleted()))
			e.update(now - lastTime);
		
		tiles.clearDeleted(e -> e.deleted());
		tiles.actAll((a, b) -> a.interact(b));
		target.set(((Ghost) tar).target());
		
		for (Entity e : entities)
			e.draw(batch, board.tileWidth(), board.tileHeight());
		target.draw(batch, board.tileWidth(), board.tileHeight());
		lastTime = now;
	}
	
	public void dispose() {
		Entity.dispose();
	}
	
}
