package com.pacman;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pacman.entities.*;
import com.pacman.graphics.Board;
import com.pacman.utils.ActionTimer;
import com.pacman.utils.EventList;

import methods.P;
import structures.CategorySet;
import structures.LList;

public class Game {
	
	private Board board;
	private LList<Entity> entities;
	private CategorySet<Entity> tiles;
	private Score score;
	
	private Target target;
	public static Entity tar;
	
	private PacMan pacman;
	private Ghost pinky, blinky, inky, clyde;
	
	private ActionTimer events;
	private EventList eventList;
	
	private long lastTime;
	private long paused;
	
	public static final int chase = 0, hide = 1, blue = 2, dead = 3;
	
	public Game(int width, int height) {
		entities = new LList<Entity>();
		board = new Board(width, height - 30);
		
		tiles = new CategorySet<>(board.size(), e -> e.getTiles());
		
		Entity.setGame(this);
		
		start();
		
		target = new Target(100, 100);
		
		score = new Score(0, height - 30, width, 30);
		
		eventList = new EventList();
		lastTime = System.currentTimeMillis();
		paused = 0;
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
		entities.clear();
		tiles.clear();
		genEntities();
		chase(1);
	}
	
	public void tryAgain() {
		blinky.reset();
		pinky.reset();
		inky.reset();
		clyde.reset();
		pacman.reset();
		chase(1);
	}
	
	public void chase(int delays) {
		events = new ActionTimer();
		events.add(() -> blinky.setMode(Ghost.chase), delays + 1);
		events.add(() -> pinky.setMode(Ghost.chase), delays);
		events.add(() -> inky.setMode(Ghost.chase), delays);
		events.add(() -> clyde.setMode(Ghost.chase), delays);
	}
	
	public void scare() {
		pinky.setScareMode(6);
		blinky.setScareMode(6);
		inky.setScareMode(6);
		clyde.setScareMode(6);
		
		Ghost.resetPoints();
		
		events = new ActionTimer();
		events.add(() -> chase(0), 6);
	}
	
	public void kill() {
		pinky.setMode(Ghost.stop);
		blinky.setMode(Ghost.stop);
		inky.setMode(Ghost.stop);
		clyde.setMode(Ghost.stop);
		
		events = new ActionTimer();
		events.add(() -> tryAgain(), 3.5f);
	}
	
	public EventList eventList() {
		return eventList;
	}
	
	public void pause(long millis) {
		paused = millis;
	}
	
	public void add(int points) {
		score.add(points);
	}
	
	public boolean paused() {
		return paused > 0;
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
		long diff = now - lastTime;
		
		events.check();
		eventList.check();
		
		if (paused > 0) {
			paused -= diff;
			if (paused <= 0)
				paused = 0;
		} else {
			for (Entity e : entities.delIter(e -> e.deleted()))
				e.update(diff);
			
			tiles.clearDeleted(e -> e.deleted());
			tiles.actAll((a, b) -> a.interact(b));
//			target.set(375);
		}
		
		for (Entity e : entities)
			e.draw(batch, board.tileWidth(), board.tileHeight());
//		target.draw(batch, board.tileWidth(), board.tileHeight());
		score.draw(batch);
		lastTime = now;
	}
	
	public void dispose() {
		Entity.dispose();
	}
	
}
