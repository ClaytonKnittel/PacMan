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
import tensor.IVector2;
import tensor.Vector2;

public abstract class Entity implements Drawable {
	
	private float width, height;
	
	private IVector2 pos;
	private int dir;
	private float d;
	
	private static final Texture spritesheet;
	private static final TextureRegion[] sprites;
	private static final TextureRegion[] fruitNumSprites;
	
	private TextureRegion lastTexture;
	
	protected static final long animationDelta = 80;
	
	private static Game game;
	private static Board board;
	private static Dijkstra<IVector2, Board.WeightDir> graph;
	private static CategorySet<Entity> tiles;
	
	private long time;
	private long delay;
	
	private boolean deleted;
	
	private boolean visible;
	
	static {
		spritesheet = new Texture(Gdx.files.internal("/Users/claytonknittel/Documents/workspace/Projects/libgdx/core/assets/pacman_sprite.png"), Format.RGBA8888, false);
		sprites = new TextureRegion[136];
		for (int i = 0; i < sprites.length; i++)
			sprites[i] = new TextureRegion(spritesheet, 3 + (i % 14) * 16, i / 14 * 16, 16, 16);
		fruitNumSprites = new TextureRegion[8];
		fruitNumSprites[0] = new TextureRegion(spritesheet, 5, 148, 13, 7);
		fruitNumSprites[1] = new TextureRegion(spritesheet, 19, 148, 15, 7);
		fruitNumSprites[2] = new TextureRegion(spritesheet, 35, 148, 15, 7);
		fruitNumSprites[3] = new TextureRegion(spritesheet, 51, 148, 15, 7);
		fruitNumSprites[4] = new TextureRegion(spritesheet, 67, 148, 18, 7);
		fruitNumSprites[5] = new TextureRegion(spritesheet, 86, 148, 20, 7);
		fruitNumSprites[6] = new TextureRegion(spritesheet, 107, 148, 20, 7);
		fruitNumSprites[7] = new TextureRegion(spritesheet, 128, 148, 20, 7);
	}
	
	public static void setGame(Game game) {
		Entity.game = game;
		Entity.board = game.board();
		Entity.graph = game.board().genGraph();
		Entity.tiles = game.tiles();
	}
	
	public Entity(float x, float y, float width, float height) {
		this.pos = board.boardPos(x, y);
		Vector2 b = board().screenPos(pos);
		if (Math.abs(x - b.x()) > Math.abs(y - b.y())) {
			dir = x - b.x() > 0 ? right : left;
			d = Math.abs(x - b.x());
		} else {
			dir = y - b.y() > 0 ? up : down;
			d = Math.abs(y - b.y());
		}
		this.time = 0;
		this.delay = animationDelta;
		this.deleted = false;
		this.lastTexture = null;
		this.visible = true;
	}
	
	public Vector2 screenPos() {
		return board.screenPos(pos).plus(dPos());
	}
	
	public IVector2 pos() {
		return pos;
	}
	
	public int x() {
		return pos.x();
	}
	
	public int y() {
		return pos.y();
	}
	
	public float dist(Entity e) {
		return screenPos().minus(e.screenPos()).mag2();
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
	
	public Vector2 dPos() {
		return Board.screenDirVec(dir).toVector2().times(d);
	}
	
	public int dir() {
		return dir;
	}
	
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public boolean forward(float d) {
		if (Ghost.is(this)) {
			if (board.isGWall(pos, dir)) {
				this.d = 0;
				return false;
			}
		}
		else if (board.isWall(pos, dir)) {
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
			return board.numGTurns(pos) > 2;
		return board.numTurns(pos) > 2;
	}
	
	public void setPos(IVector2 pos) {
		setPos(pos.x(), pos.y());
	}
	
	public void setPos(int x, int y) {
		pos = new IVector2(x, y);
	}
	
	protected void setPos() {
		pos = board.newPos(pos, dir);
	}
	
	protected void moveD() {
		if (dir > 1)
			d -= board.tileHeight();
		else
			d -= board.tileWidth();
	}
	
	protected void flipD() {
		if (vertDir(dir))
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
	
	public LinkedList<Integer> getTiles() {
		LinkedList<Integer> ret = new LinkedList<>();
		ret.add(board.tile(pos));
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
	
	protected Dijkstra<IVector2, Board.WeightDir> graph() {
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
		if (t == null)
			t = lastTexture;
		else
			lastTexture = t;
		
		Vector2 pos = screenPos().minus(t.getRegionWidth() / 2, t.getRegionHeight() / 2);
		batch.draw(t, pos.x(), pos.y());
		
		if (board().offScreen(this.pos, dir)) {
			if (vertDir(dir))
				pos = board().reflectY(pos);
			else
				pos = board().reflectX(pos);
			batch.draw(lastTexture, pos.x(), pos.y());
		}
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
	
	public static boolean vertDir(int dir) {
		return dir > 1;
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
	
	protected TextureRegion fruitPointTexture(int which) {
		return fruitNumSprites[which];
	}
	
	protected TextureRegion getTexture(int i) {
		return sprites[i];
	}
	
	public static void dispose() {
		spritesheet.dispose();
	}
	
}
