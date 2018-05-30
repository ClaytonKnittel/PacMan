package com.pacman.entities;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Game;
import com.pacman.graphics.Board;
import com.pacman.graphics.Drawable;

import algorithms.Dijkstra;
import structures.CategorySet;

public abstract class Entity implements Drawable {
	
	private float width, height;
	
	private int tile, dir;
	private float d;
	
	private static final Texture spritesheet;
	private static final TextureRegion[] sprites;
	
	private TextureRegion lastTexture;
	
	protected static final long animationDelta = 80;
	
	private static Game game;
	private static Board board;
	private static Dijkstra<Integer, Board.WeightDir> graph;
	private static CategorySet<Entity> tiles;
	
	private long time;
	private long delay;
	
	private boolean deleted;
	
	private boolean visible;
	
	static {
		spritesheet = new Texture(Gdx.files.internal("/Users/claytonknittel/Documents/workspace/Projects/libgdx/core/assets/pacman_sprite_clear.png"), Format.RGBA8888, false);
		sprites = new TextureRegion[140];
		for (int i = 0; i < sprites.length; i++)
			sprites[i] = new TextureRegion(spritesheet, 3 + (i % 14) * 16, i / 14 * 16, 16, 16);
	}
	
	public static void setGame(Game game) {
		Entity.game = game;
		Entity.board = game.board();
		Entity.graph = game.board().genGraph();
		Entity.tiles = game.tiles();
	}
	
	public Entity(float x, float y, float width, float height) {
		tile = board.boardPos(x, y);
		float bx = board.screenX(tile);
		float by = board.screenY(tile);
		if (Math.abs(x - bx) > Math.abs(y - by)) {
			dir = x - bx > 0 ? 1 : 3;
			d = Math.abs(x - bx);
		} else {
			dir = y - by > 0 ? 0 : 2;
			d = Math.abs(y - by);
		}
		this.time = 0;
		this.delay = animationDelta;
		this.deleted = false;
		this.lastTexture = null;
		this.visible = true;
	}
	
	public float x() {
		return board.screenX(tile) + dx();
	}
	
	public float y() {
		return board.screenY(tile) + dy();
	}
	
	public float dist(Entity e) {
		return square(e.x() - x()) + square(e.y() - y());
	}
	
	private static float square(float f) {
		return f * f;
	}
	
	public float d() {
		return d;
	}
	
	public static int opposite(int dir) {
		if (dir == left)
			return right;
		if (dir == right)
			return left;
		if (dir == up)
			return down;
		if (dir == down)
			return up;
		return -1;
	}
	
	public float dx() {
		return Board.dirX(dir) * d;
	}
	
	public float dy() {
		return Board.dirY(dir) * d;
	}
	
	public int dir() {
		return dir;
	}
	
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public boolean forward(float d) {
		if (Ghost.is(this)) {
			if (board.isGWall(tile, dir)) {
				this.d = 0;
				return false;
			}
		}
		else if (board.isWall(tile, dir)) {
			this.d = 0;
			return false;
		}
		this.d += d;
		return true;
	}
	
	public void setD(float d) {
		this.d = d;
	}
	
	protected boolean inNextTile() {
		if (dir > 1)
			return d >= board.tileHeight();
		return d >= board.tileWidth();
	}
	
	protected boolean isDecisionTile() {
		if (Ghost.is(this))
			return board.numGTurns(tile) > 2;
		return board.numTurns(tile) > 2;
	}
	
	protected void setTile(int tile) {
		this.tile = tile;
	}
	
	protected void setTile() {
		tile += Board.dirX(dir) - board.boardWidth() * Board.dirY(dir);
	}
	
	protected void moveD() {
		if (dir > 1)
			d -= board.tileHeight();
		else
			d -= board.tileWidth();
	}
	
	protected void flipD() {
		if (dir > 1)
			d = board.tileHeight() - d;
		else
			d = board.tileWidth() - d;
	}
	
	public float width() {
		return width;
	}
	
	public float height() {
		return height;
	}
	
	public int tile() {
		return tile;
	}
	
	public LinkedList<Integer> getTiles() {
		LinkedList<Integer> ret = new LinkedList<>();
		ret.add(tile());
		return ret;
	}
	
	public boolean deleted() {
		return deleted;
	}
	
	public void delete() {
		deleted = true;
	}
	
	public final void interact(Entity e) {
		interact(e, false);
	}
	
	public void interact(Entity e, boolean secondCall) {
		if (!secondCall)
			e.interact(this, true);
	}
	
	protected Game game() {
		return game;
	}
	
	protected Board board() {
		return board;
	}
	
	protected Dijkstra<Integer, Board.WeightDir> graph() {
		return graph;
	}
	
	protected void setDir(int d) {
		this.dir = d;
	}
	
	protected void setDelay(long delay) {
		this.delay = delay;
	}
	
	protected boolean record(long delta) {
		time += delta;
		if (time >= delay) {
			do time -= delay;
			while (time >= delay);
			return true;
		}
		return false;
	}
	
	public abstract void update(long delta);
	
	public void posChange() {
		tiles.update(this);
	}
	
	public void draw(Batch batch, float tileWidth, float tileHeight) {
		if (!visible)
			return;
		TextureRegion t = texture();
		if (t != null)
			batch.draw(lastTexture = t, x() - tileWidth / 2, y() - tileHeight / 2, tileWidth, tileHeight);
		else
			batch.draw(lastTexture, x() - tileWidth / 2, y() - tileHeight / 2, tileWidth, tileHeight);
	}
	
	public static final int pacman = 0, red = 56, blue = 84, yellow = 98;
	public static final int pinky = 70, blinky = 56, inky = 84, clyde = 98;
	
	public static final int right = 0, left = 1, up = 2, down = 3;
	
	public static String dirString(int dir) {
		switch(dir) {
		case right:
			return "right";
		case left:
			return "left";
		case up:
			return "up";
		case down:
			return "down";
		}
		return "[not a direction]";
	}
	
	public static int sequentialDir(int dir) {
		switch(dir) {
		case right:
			return 1;
		case left:
			return 3;
		case up:
			return 0;
		case down:
			return 2;
		}
		return -1;
	}
	
	public static int rotateDirRight(int dir) {
		if (dir == up)
			return right;
		if (dir == right)
			return down;
		if (dir == down)
			return left;
		return up;
	}
	
	private static boolean dir(int position) {
		return position < 4;
	}
	
	protected TextureRegion pacmanTexture(int position, int phase) {
		if (phase == 2)
			return getTexture(2);
		return getTexture(14 * position + phase % 2);
	}
	
	protected TextureRegion dyingTexture(int phase) {
		return getTexture(3 + phase);
	}
	
	protected TextureRegion idleTexture(int entity, int position, int phase) {
		if (dir(position)) {
			return getTexture(entity + 2 * position + phase);
		}
		return getTexture(entity + position + phase);
	}
	
	protected TextureRegion scaredTexture(int phase) {
		return getTexture(64 + phase);
	}
	
	protected TextureRegion eyeTexture(int direction) {
		return getTexture(78 + direction);
	}
	
	protected TextureRegion pointsTexture(int points) {
		return getTexture(111 + points);
	}
	
	protected TextureRegion fruitTexture(int which) {
		return getTexture(44 + which);
	}
	
	protected TextureRegion getTexture(int i) {
		return sprites[i];
	}
	
	public static void dispose() {
		spritesheet.dispose();
	}
	
}
