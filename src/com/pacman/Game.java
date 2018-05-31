package com.pacman;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pacman.entities.*;
import com.pacman.graphics.Board;
import com.pacman.utils.ActionTimer;
import com.pacman.utils.ConditionalEvent;
import com.pacman.utils.Event;
import com.pacman.utils.EventList;

import structures.CategorySet;
import structures.LList;

public class Game {
	
	private Board board;
	private LList<Entity> entities;
	private CategorySet<Entity> tiles;
	private Score score;
	
//	private Target target;
	public static Entity tar;
	
	private PacMan pacman;
	private Ghost pinky, blinky, inky, clyde;
	private int numDots;
	
	private ActionTimer events;
	private EventList eventList;
	
	private long lastTime;
	private long paused;
	
	private int level;
	
	public static final int chase = 0, hide = 1, blue = 2, dead = 3;
	
	public Game(int width, int height) {
		entities = new LList<Entity>();
		board = new Board(width, height - 30);
		
		tiles = new CategorySet<>(board.size(), e -> e.getTiles());
		
		level = 0;
		
		Entity.setGame(this);
		
//		target = new Target(100, 100);
		
		score = new Score(0, height - 30, width, 30);
		
		eventList = new EventList();
		
		start();
		
		lastTime = System.currentTimeMillis();
		paused = 0;
	}
	
	public Board board() {
		return board;
	}
	
	public CategorySet<Entity> tiles() {
		return tiles;
	}
	
	public int level() {
		return level;
	}
	
	public int numDots() {
		return numDots;
	}
	
	private void genEntities() {
		numDots = 0;
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
			if (e instanceof Dot || e instanceof BigDot)
				numDots++;
			if (e instanceof Fruit)
				eventList.add(((Fruit) e).fruitEvent());
			entities.add(e);
			tiles.add(e);
		}
	}
	
	public void start() {
		level++;
		entities.clear();
		tiles.clear();
		genEntities();
		chase(2);
		
		ConditionalEvent endGame = new ConditionalEvent();
		endGame.add(() -> end(), () -> endGame());
		eventList.add(endGame);
	}
	
	private boolean end() {
		return numDots <= 0;
	}
	
	public void endGame() {
		blinky.stop();
		pinky.stop();
		inky.stop();
		clyde.stop();
		pacman.stop();
		
		events.clear();
		events.add(() -> board.glow(), 1);
		events.add(() -> board.revert(), .4f);
		events.add(() -> board.glow(), .4f);
		events.add(() -> board.revert(), .4f);
		events.add(() -> board.glow(), .4f);
		events.add(() -> board.revert(), .4f);
		events.add(() -> start(), .4f);
	}
	
	public void tryAgain() {
		blinky.reset();
		pinky.reset();
		inky.reset();
		clyde.reset();
		pacman.reset();
		chase(2);
	}
	
	public void chase(float delays) {
		events = new ActionTimer();
		events.add(() -> blinky.setMode(Ghost.chase), delays);
		events.add(() -> pinky.setMode(Ghost.chase), delays);
		events.add(() -> inky.setMode(Ghost.chase), delays);
		events.add(() -> clyde.setMode(Ghost.chase), delays);
	}
	
	public void scare() {
		float time = 6;
		pinky.setScareMode(time);
		blinky.setScareMode(time);
		inky.setScareMode(time);
		clyde.setScareMode(time);
		
		Ghost.resetPoints();
		
		events = new ActionTimer();
		events.add(() -> chase(0), time);
	}
	
	public void kill() {
		pinky.stop();
		blinky.stop();
		inky.stop();
		clyde.stop();
		
		events = new ActionTimer();
		events.add(() -> tryAgain(), 3.5f);
	}
	
	public void eatDot() {
		numDots--;
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
	}
	
	public void pause(long millis) {
		paused = millis;
		eventList.pause();
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
	
	public void pauseMin(long millis) {
		paused -= millis;
		eventList.addTime(millis);
		events.setBack(millis);
		if (paused <= 0) {
			paused = 0;
			eventList.resume();
		}
	}
	
	public void draw(Batch batch) {
		long now = System.currentTimeMillis();
		long diff = now - lastTime;
		
		events.check();
		eventList.check();
		
		if (paused > 0) {
			pauseMin(diff);
		} else {
			for (Entity e : entities.delIter(e -> e.deleted()))
				e.update(diff);
			
			tiles.clearDeleted(e -> e.deleted());
			tiles.actAll((a, b) -> a.interact(b));
//			if (((Ghost) tar).target() != null)
//				target.setPos(((Ghost) tar).target());
		}
		
		batch.draw(board.texture(), 0, 0);
		for (Entity e : entities)
			e.draw(batch, board.tileWidth(), board.tileHeight());
//		target.draw(batch, board.tileWidth(), board.tileHeight());
		score.draw(batch, level);
		lastTime = now;
	}
	
	public void dispose() {
		Entity.dispose();
	}
	
}
