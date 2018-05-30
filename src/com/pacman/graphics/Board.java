package com.pacman.graphics;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Game;
import com.pacman.entities.*;

import algorithms.Dijkstra;
import algorithms.Dijkstra.Addable;
import structures.LList;
import structures.Reversible;
import tensor.IVector2;
import tensor.Vector2;

public class Board implements Drawable {
	
	private TextureRegion texture;
	
	private static final int DEFAULT_HEIGHT = 31, DEFAULT_WIDTH = 28;
	
	// w = wall, e = empty, o = dot, O = big dot, c = candy, b = ghost box, d = door (to ghost box), n = nothing, p = pacman, t = teleport
	private static final short w = 0, e = 1, o = 2, c = 3, b = 4, O = 5, d = 6, n = 7, p = 8, t1 = 9;
	
	private static final short[] DEFAULT_BOARD = new short[] {
			w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w,
			w, o, o, o, o, o, o, o, o, o, o, o, o, w, w, o, o, o, o, o, o, o, o, o, o, o, o, w,
			w, o, w, w, w, w, o, w, w, w, w, w, o, w, w, o, w, w, w, w, w, o, w, w, w, w, o, w,
			w, O, w, w, w, w, o, w, w, w, w, w, o, w, w, o, w, w, w, w, w, o, w, w, w, w, O, w,
			w, o, w, w, w, w, o, w, w, w, w, w, o, w, w, o, w, w, w, w, w, o, w, w, w, w, o, w,
			w, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, w,
			w, o, w, w, w, w, o, w, w, o, w, w, w, w, w, w, w, w, o, w, w, o, w, w, w, w, o, w,
			w, o, w, w, w, w, o, w, w, o, w, w, w, w, w, w, w, w, o, w, w, o, w, w, w, w, o, w,
			w, o, o, o, o, o, o, w, w, o, o, o, o, w, w, o, o, o, o, w, w, o, o, o, o, o, o, w,
			w, w, w, w, w, w, o, w, w, w, w, w, e, w, w, e, w, w, w, w, w, o, w, w, w, w, w, w,
			n, n, n, n, n, w, o, w, w, w, w, w, e, w, w, e, w, w, w, w, w, o, w, n, n, n, n, n,
			n, n, n, n, n, w, o, w, w, e, e, e, e, e, e, e, e, e, e, w, w, o, w, n, n, n, n, n,
			n, n, n, n, n, w, o, w, w, e, w, w, d, d, d, d, w, w, e, w, w, o, w, n, n, n, n, n,
			w, w, w, w, w, w, o, w, w, e, w, b, b, b, b, b, b, w, e, w, w, o, w, w, w, w, w, w,
			t1, e, e, e, e, e, o, e, e, e, w, b, b, b, b, b, b, w, e, e, e, o, e, e, e, e, e, t1,
			w, w, w, w, w, w, o, w, w, e, w, b, b, b, b, b, b, w, e, w, w, o, w, w, w, w, w, w,
			n, n, n, n, n, w, o, w, w, e, w, w, w, w, w, w, w, w, e, w, w, o, w, n, n, n, n, n,
			n, n, n, n, n, w, o, w, w, e, e, e, e, c, c, e, e, e, e, w, w, o, w, n, n, n, n, n,
			n, n, n, n, n, w, o, w, w, e, w, w, w, w, w, w, w, w, e, w, w, o, w, n, n, n, n, n,
			w, w, w, w, w, w, o, w, w, e, w, w, w, w, w, w, w, w, e, w, w, o, w, w, w, w, w, w,
			w, o, o, o, o, o, o, o, o, o, o, o, o, w, w, o, o, o, o, o, o, o, o, o, o, o, o, w,
			w, o, w, w, w, w, o, w, w, w, w, w, o, w, w, o, w, w, w, w, w, o, w, w, w, w, o, w,
			w, o, w, w, w, w, o, w, w, w, w, w, o, w, w, o, w, w, w, w, w, o, w, w, w, w, o, w,
			w, O, o, o, w, w, o, o, o, o, o, o, o, p, p, o, o, o, o, o, o, o, w, w, o, o, O, w,
			w, w, w, o, w, w, o, w, w, o, w, w, w, w, w, w, w, w, o, w, w, o, w, w, o, w, w, w,
			w, w, w, o, w, w, o, w, w, o, w, w, w, w, w, w, w, w, o, w, w, o, w, w, o, w, w, w,
			w, o, o, o, o, o, o, w, w, o, o, o, o, w, w, o, o, o, o, w, w, o, o, o, o, o, o, w,
			w, o, w, w, w, w, w, w, w, w, w, w, o, w, w, o, w, w, w, w, w, w, w, w, w, w, o, w,
			w, o, w, w, w, w, w, w, w, w, w, w, o, w, w, o, w, w, w, w, w, w, w, w, w, w, o, w,
			w, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, w,
			w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w, w
	};
	
	private int width, height, texWidth, texHeight;
	private float tileWidth, tileHeight;
	
	private short[] board;
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.texWidth = DEFAULT_WIDTH;
		this.texHeight = DEFAULT_HEIGHT;
		
		this.tileWidth = (float) width / texWidth;
		this.tileHeight = (float) height / texHeight;
		
		this.board = DEFAULT_BOARD;
		genBoard(DEFAULT_DRAW_STRATEGY);
	}
	
	private void genBoard(DrawStrategy draw) {
		Pixmap p = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		
		p.setColor(Color.BLACK);
		p.fill();
		
		for (int i = 0; i < board.length; i++)
			draw.draw(p, x(i), height - y(i), tileWidth, tileHeight, board[i], getSurrounding(this.boardPos(i)));
		
		texture = new TextureRegion(new Texture(p));
	}
	
	public Dijkstra<IVector2, WeightDir> genGraph() {
		LinkedList<IVector2> spots = new LinkedList<>();
		for (int i = 0; i < board.length; i++) {
			if (isGhostPermeable(board[i]))
				spots.add(boardPos(i));
		}
		Dijkstra<IVector2, WeightDir> g = new Dijkstra<>(spots);
		LList<IVector2> teleports = new LList<>();
		for (IVector2 i : spots) {
			if (i.x() < texWidth - 1) {
				if (isGhostPermeable(i.plus(1, 0)))
					g.addEdge(i, i.plus(1, 0), new WeightDir(1, Entity.right));
			}
			if (i.y() < texHeight - 1) {
				if (isGhostPermeable(i.plus(0, 1)))
					g.addEdge(i, i.plus(0, 1), new WeightDir(1, Entity.down));
			}
			if (get(i) > 8)
				teleports.add(i);
		}
		teleports.actPairs((a, b) -> {
			if (get(a) == get(b)) {
				int dir;
				if (a.x() == b.x()) {
					if (a.y() > b.y())
						dir = Entity.down;
					else
						dir = Entity.up;
				} else if (a.y() == b.y()) {
					if (a.x() > b.y())
						dir = Entity.right;
					else
						dir = Entity.left;
				} else
					throw new IllegalArgumentException("Cannot create teleportation from tile " + a + " to tile " + b);
				g.addEdge(a, b, new WeightDir(1, dir));
			}
		});
		
		return g;
	}
	
	public class WeightDir extends Number implements Reversible<WeightDir>, Addable<WeightDir> {
		private static final long serialVersionUID = -4609693872829433907L;
		
		float weight;
		int dir;
		
		WeightDir(float weight, int dir) {
			this.weight = weight;
			this.dir = dir;
		}
		
		public int dir() {
			return dir;
		}
		@Override
		public int intValue() {
			return 0;
		}
		@Override
		public long longValue() {
			return 0;
		}
		@Override
		public float floatValue() {
			return weight;
		}
		@Override
		public double doubleValue() {
			return (double) weight;
		}

		@Override
		public WeightDir reverse() {
			return new WeightDir(weight, Entity.opposite(dir));
		}
		
		@Override
		public WeightDir add(WeightDir o) {
			return new WeightDir(weight + o.weight, dir);
		}
		
		public String toString() {
			return weight + " " + Entity.dirString(dir);
		}
	}
	
	public int xCoord(int tile) {
		return tile % texWidth;
	}
	
	public int yCoord(int tile) {
		return tile / texWidth;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public int boardWidth() {
		return texWidth;
	}
	
	public int boardHeight() {
		return texHeight;
	}
	
	private short get(int x, int y) {
		if (x >= 0 && x < texWidth && y >= 0 && y < texHeight)
			return board[tile(x, y)];
		return n;
	}
	
	public short get(IVector2 pos) {
		return get(pos.x(), pos.y());
	}
	
	public int size() {
		return board.length;
	}
	
	public float tileWidth() {
		return tileWidth;
	}
	
	public float tileHeight() {
		return tileHeight;
	}
	
	private float x(int i) {
		return i % texWidth * tileWidth;
	}
	
	private float y(int i) {
		return height - i / texWidth * tileHeight;
	}
	
	public Vector2 screenPos(IVector2 p) {
		return screenPos(p.x(), p.y());
	}
	
	private Vector2 screenPos(int x, int y) {
		return new Vector2(tileWidth * (x + .5f), height - tileHeight * (y + .5f));
	}
	
	private float screenX(int tile) {
		return tileWidth * (tile % texWidth + .5f);
	}
	
	private float screenY(int tile) {
		return height - tileHeight * (tile / texWidth + .5f);
	}
	
	public static IVector2 screenDirVec(int dir) {
		if (dir == Entity.right)
			return new IVector2(1, 0);
		if (dir == Entity.left)
			return new IVector2(-1, 0);
		if (dir == Entity.up)
			return new IVector2(0, 1);
		if (dir == Entity.down)
			return new IVector2(0, -1);
		return IVector2.ZERO;
	}
	
	private static IVector2 dirVec(int dir) {
		if (dir == Entity.right)
			return new IVector2(1, 0);
		if (dir == Entity.left)
			return new IVector2(-1, 0);
		if (dir == Entity.up)
			return new IVector2(0, -1);
		if (dir == Entity.down)
			return new IVector2(0, 1);
		return IVector2.ZERO;
	}
	
	public IVector2 newPos(IVector2 pos, int dir) {
		if (isTeleporter(pos)) {
			if (dir == Entity.right && pos.x() >= texWidth - 1)
				return pos.minus(texWidth - 1, 0);
			if (dir == Entity.left && pos.x() <= 0)
				return pos.plus(texWidth - 1, 0);
			if (dir == Entity.up && pos.y() <= 0)
				return pos.plus(0, texHeight - 1);
			if (dir == Entity.down && pos.y() >= texHeight - 1)
				return pos.minus(0, texHeight - 1);
		}
		return pos.plus(dirVec(dir));
	}
	
	public boolean offScreen(IVector2 pos, int dir) {
		switch(dir) {
		case Entity.left:
			return pos.x() <= 0;
		case Entity.right:
			return pos.x() >= texWidth - 1;
		case Entity.up:
			return pos.y() <= 0;
		case Entity.down:
			return pos.y() >= texHeight - 1;
		}
		return false;
	}
	
	public Vector2 reflectX(Vector2 vec) {
		if (vec.x() < width / 2)
			return vec.plus(width, 0);
		return vec.minus(width, 0);
	}
	
	public Vector2 reflectY(Vector2 vec) {
		if (vec.y() < height / 2)
			return vec.plus(0, height);
		return vec.minus(0, height);
	}
	
	public boolean isWall(IVector2 pos, int dir) {
		IVector2 newPos = newPos(pos, dir);
		if (!oob(newPos))
			return !isPermeable(newPos);
		return true;
	}
	
	public int numTurns(IVector2 pos) {
		short[] s = getSurrounding(pos);
		return (isPermeable(s[0]) ? 1 : 0) + (isPermeable(s[1]) ? 1 : 0) + (isPermeable(s[2]) ? 1 : 0) + (isPermeable(s[3]) ? 1 : 0);
	}
	
	public boolean isGWall(IVector2 pos, int dir) {
		IVector2 newPos = newPos(pos, dir);
		if (!oob(newPos))
			return !isGhostPermeable(newPos);
		return true;
	}
	
	public boolean isTeleporter(IVector2 pos) {
		return isTeleporter(tile(pos));
	}
	
	private boolean isTeleporter(int tile) {
		return board[tile] > 8;
	}
	
	public int numGTurns(IVector2 pos) {
		short[] s = getSurrounding(pos);
		return (isGhostPermeable(s[0]) ? 1 : 0) + (isGhostPermeable(s[1]) ? 1 : 0) + (isGhostPermeable(s[2]) ? 1 : 0) + (isGhostPermeable(s[3]) ? 1 : 0);
	}
	
	private boolean oob(IVector2 pos) {
		if (pos.x() < 0)
			return true;
		if (pos.y() < 0)
			return true;
		if (pos.x() >= texWidth)
			return true;
		if (pos.y() >= texHeight)
			return true;
		return false;
	}
	
	public int tile(IVector2 pos) {
		return tile(pos.x(), pos.y());
	}
	
	private int tile(int x, int y) {
		return x + texWidth * y;
	}
	
	private int boardX(float x) {
		return (int) (x / tileWidth);
	}
	
	private int boardY(float y) {
		return (int) ((height - y) / tileHeight);
	}
	
	public IVector2 boardPos(float x, float y) {
		return new IVector2(boardX(x), boardY(y));
	}
	
	public IVector2 boardPos(int tile) {
		return new IVector2(tile % texWidth, tile / texWidth);
	}
	
	public boolean isPermeable(IVector2 pos) {
		return isPermeable(board[tile(pos)]);
	}
	
	private boolean isPermeable(short s) {
		return s != w && s != d && s != n && s != b;
	}
	
	public boolean isGhostPermeable(IVector2 pos) {
		return isGhostPermeable(board[tile(pos)]);
	}
	
	private boolean isGhostPermeable(short s) {
		return s != w && s != n;
	}
	
	public IVector2 pathFind(int tilesAhead, IVector2 pos, int dir) {
		short[] tiles = getSurrounding(pos);
		if (!isGhostPermeable(tiles[0]) && !isGhostPermeable(tiles[1]) && !isGhostPermeable(tiles[2]) && !isGhostPermeable(tiles[3]))
			return pos;
		IVector2 n;
		while (tilesAhead > 0) {
			n = newPos(pos, dir);
			if (isGhostPermeable(n)) {
				pos = n;
				tilesAhead--;
			}
			else
				dir = Entity.rotateDirRight(dir);
		}
		return pos;
	}
	
	public IVector2 nearest(float x, float y) {
		IVector2 v = boardPos(x, y);
		int tx = v.x();
		int ty = v.y();
		
		if (isPermeable(v))
			return v;
		
		int dx, dy;
		for (int radius = 1; true; radius++) {
			for (int d = 0; d < radius; d++) {
				dx = tx + d;
				dy = ty + d - radius;
				if (isPermeable(get(dx, dy)))
					return new IVector2(dx, dy);
				dx = tx + d - radius;
				dy = ty - d;
				if (isPermeable(get(dx, dy)))
					return new IVector2(dx, dy);
				dx = tx - d;
				dy = ty - d + radius;
				if (isPermeable(get(dx, dy)))
					return new IVector2(dx, dy);
				dx = tx - d + radius;
				dy = ty + d;
				if (isPermeable(get(dx, dy)))
					return new IVector2(dx, dy);
			}
		}
	}
	
	public int randomDir(IVector2 pos) {
		int dir;
		do {
			dir = (int) (Math.random() * 4);
		} while (!isGhostPermeable(newPos(pos, dir)));
		return dir;
	}
	
	/**
	 * comes:<br>
	 * 4 0 5<br>
	 * 3   1<br>
	 * 7 2 6
	 * 
	 * @param board
	 * @param index
	 * @param width
	 * @param height
	 * @return surrounding tiles
	 */
	private short[] getSurrounding(IVector2 pos) {
		int x = pos.x();
		int y = pos.y();
		
		short[] ret = new short[8];
		if (y > 0) {
			if (x > 0)
				ret[4] = board[x - 1 + texWidth * (y - 1)];
			else
				ret[4] = n;
			ret[0] = board[x + texWidth * (y - 1)];
		}
		else {
			ret[0] = n;
			ret[4] = n;
		}
		if (x < texWidth - 1) {
			if (y > 0)
				ret[5] = board[x + 1 + texWidth * (y - 1)];
			else
				ret[5] = n;
			ret[1] = board[x + 1 + texWidth * y];
		}
		else {
			ret[1] = n;
			ret[5] = n;
		}
		if (y < texHeight - 1) {
			if (x < texWidth - 1)
				ret[6] = board[x + 1 + texWidth * (y + 1)];
			else
				ret[6] = n;
			ret[2] = board[x + texWidth * (y + 1)];
		}
		else {
			ret[2] = n;
			ret[6] = n;
		}
		if (x > 0) {
			if (y < texHeight - 1)
				ret[7] = board[x - 1 + texWidth * (y + 1)];
			else
				ret[7] = n;
			ret[3] = board[x - 1 + texWidth * y];
		}
		else {
			ret[3] = n;
			ret[7] = n;
		}
		return ret;
	}
	
	@Override
	public TextureRegion texture() {
		return texture;
	}
	
	public Iterable<Entity> genEntities() {
		LinkedList<Entity> entities = new LinkedList<Entity>();
		boolean[] used = new boolean[board.length];
		int which = 0;
		Ghost gh = null;
		
		for (int i = 0; i < board.length; i++) {
			switch (board[i]) {
			case o:
				entities.add(new Dot(screenX(i), screenY(i)));
				break;
			case O:
				entities.add(new BigDot(screenX(i), screenY(i)));
				break;
			case c:
				break;
			case p:
				if (board[i - 1] == p || board[i - texWidth] == p)
					continue;
				float x, y;
				if (board[i + 1] == p) {
					x = (screenX(i) + screenX(i + 1)) / 2;
					if (board[i + texWidth] == p)
						y = (screenY(i) + screenY(i + texWidth)) / 2;
					else
						y = screenY(i);
				} else if (board[i + texWidth] == p) {
					x = screenX(i);
					y = (screenY(i) + screenY(i + texWidth)) / 2;
				} else {
					x = screenX(i);
					y = screenY(i);
				}
				entities.add(new PacMan(x, y));
				break;
			case b:
				if (board[i + 1] == b && board[i + texWidth] == b && board[i + texWidth + 1] == b) {
					if (!used[i] && !used[i + 1] && !used[i + texWidth] && !used[i + texWidth + 1]) {
						Ghost g = null;
						switch (which) {
						case 0:
							g = new Pinky(screenX(i), screenY(i));
							break;
						case 1:
							g = new Blinky(screenX(i), screenY(i) - tileHeight);
							break;
						case 2:
							g = new Inky(screenX(i), screenY(i) - 2 * tileHeight);
							((Inky) g).setBlinky(gh);
							Game.tar = g;
							break;
						case 3:
							g = new Clyde(screenX(i), screenY(i) - tileHeight);
						}
						g.setD((float) Math.random() * tileHeight);
						entities.add(g);
						
						used[i] = true;
						used[i + texWidth] = true;
						if (which != 1 && which != 2) {
							used[i + 1] = true;
							used[i + texWidth + 1] = true;
						}
						which++;
						gh = g;
					}
				}
			}
		}
		return entities;
	}
	
	public static interface DrawStrategy {
		// surrounding goes from left to right, top to bottom, not including currentSquare
		void draw(Pixmap p, float xOff, float yOff, float width, float height, short currentSquare, short[] surrounding);
	}
	
	private static DrawStrategy DEFAULT_DRAW_STRATEGY = new DrawStrategy() {
		@Override
		public void draw(Pixmap p, float xOff, float yOff, float width, float height, short currentSquare, short[] surrounding) {
			switch (currentSquare) {
			case w:
				drawWall(p, wallColor, xOff, yOff, width, height, surrounding);
				break;
			case d:
				drawDoor(p, doorColor, xOff, yOff, width, height, surrounding);
			}
		}
	};
	
	private static void fillSquare(Pixmap p, Color color, float xOff, float yOff, float width, float height) {
		p.setColor(color);
		p.fillRectangle((int) xOff, (int) yOff, (int) width, (int) height);
	}
	
	private static final Color wallColor = new Color(0x1030cdff);
	private static final Color doorColor = new Color(0xf1b9d9ff);
	
	private static final int thickness = 2;
	
	// the following are as percents of the width / height
	private static final float cushion = .35f;
	private static final float largeRad = .5f;
	
	private static void drawWall(Pixmap p, Color wallColor, float xOff, float yOff, float width, float height, short[] surrounding) {
		fillSquare(p, Color.BLACK, xOff, yOff, width, height);
		
		xOff += cushion * width;
		yOff += cushion * height;
		float w = width * (1 - cushion * 2);
		float h = height * (1 - cushion * 2);
		
		float circEX = -width * largeRad; // extra x
		float circEY = -height * largeRad;
		float difx = -w / 2 - circEX;
		float dify = -h / 2 - circEY;
		
		boolean circletr = !mergeable(surrounding[0]) && !mergeable(surrounding[1]);
		boolean circletl = !mergeable(surrounding[0]) && !mergeable(surrounding[3]);
		boolean circlebr = !mergeable(surrounding[2]) && !mergeable(surrounding[1]);
		boolean circlebl = !mergeable(surrounding[2]) && !mergeable(surrounding[3]);
		
		p.setColor(wallColor);
		if (!mergeable(surrounding[0])) {
			if (circletr && !circletl && !circlebr)
				drawArc(p, false, xOff + w + circEX, yOff - circEY, largeRad * width, -largeRad * height);
			else
				drawLine(p, xOff + w / 2 + (circletl && !circletr && !circlebl ? difx : 0), yOff, xOff + width - cushion * width, yOff);
			if (circletl && !circletr && !circlebl)
				drawArc(p, false, xOff - circEX, yOff - circEY, -largeRad * width, -largeRad * height);
			else
				drawLine(p, xOff - cushion * width, yOff, xOff + w / 2 - (circletr && !circletl && !circlebr ? difx : 0), yOff);
		}
		if (!mergeable(surrounding[2])) {
			if (circlebr && !circletr && !circlebl)
				drawArc(p, false, xOff + w + circEX, yOff + h + circEY, largeRad * width, largeRad * height);
			else
				drawLine(p, xOff + w / 2 + (circlebl && !circlebr && ! circletl ? difx : 0), yOff + h, xOff + width - cushion * width, yOff + h);
			if (circlebl && !circlebr && !circletl) {
				drawArc(p, false, xOff - circEX, yOff + h + circEY, -largeRad * width, largeRad * height);
			}
			else
				drawLine(p, xOff - cushion * width, yOff + h, xOff + w / 2 - (circlebr && !circletr && !circlebl ? difx : 0), yOff + h);
		}
		if (!mergeable(surrounding[1])) {
			if (mergeable(surrounding[0]))
				drawLine(p, xOff + w, yOff - cushion * height, xOff + w, yOff + h / 2 - (circlebr && !circletr && !circlebl ? dify : 0));
			if (mergeable(surrounding[2]))
				drawLine(p, xOff + w, yOff + h / 2 + (circletr && !circletl && !circlebr ? dify : 0), xOff + w, yOff + height - cushion * height);
		}
		if (!mergeable(surrounding[3])) {
			if (mergeable(surrounding[0]))
				drawLine(p, xOff, yOff - cushion * height, xOff, yOff + h / 2 - (circlebl && !circletl && !circlebr ? dify : 0));
			if (mergeable(surrounding[2]))
				drawLine(p, xOff, yOff + h / 2 + (circletl && !circletr && !circlebl ? dify : 0), xOff, yOff + height - cushion * height);
		}
		if (mergeable(surrounding[0]) && mergeable(surrounding[1]) && !mergeable(surrounding[5]))
			drawArc(p, false, xOff + w + width * cushion, yOff - height * cushion, -width * cushion, height * cushion);
		if (mergeable(surrounding[1]) && mergeable(surrounding[2]) && !mergeable(surrounding[6]))
			drawArc(p, false, xOff + w + width * cushion, yOff + h + height * cushion, -width * cushion, -height * cushion);
		if (mergeable(surrounding[2]) && mergeable(surrounding[3]) && !mergeable(surrounding[7]))
			drawArc(p, false, xOff - width * cushion - .5f, yOff + h + height * cushion, width * cushion + .5f, -height * cushion);
		if (mergeable(surrounding[3]) && mergeable(surrounding[0]) && !mergeable(surrounding[4]))
			drawArc(p, false, xOff - width * cushion, yOff - height * cushion, width * cushion, height * cushion);
	}
	
	private static final float doorWidth = .26f;
	
	private static void drawDoor(Pixmap p, Color color, float xOff, float yOff, float width, float height, short[] surrounding) {
		fillSquare(p, Color.BLACK, xOff, yOff, width, height);

		float w = doorWidth * width;
		float h = doorWidth * height;
		float x = (width - w) / 2 + xOff;
		float y = (height - h) / 2 + yOff;
		
		float ww = (width + w) / 2;
		float hh = (height + h) / 2;
		if ((int) (x + w) > (int) x + (int) w)
			w += 1;
		if ((int) (x + ww) > (int) x + (int) ww)
			ww += 1;
		if ((int) (y + h) > (int) y + (int) h)
			h += 1;
		if ((int) (y + hh) > (int) y + (int) hh)
			hh += 1;
		
		
		p.setColor(doorColor);
		if (mergeable(surrounding[0])) {
			p.fillRectangle((int) x, (int) yOff, (int) w, (int) hh);
		}
		if (mergeable(surrounding[1])) {
			p.fillRectangle((int) x, (int) y, (int) ww, (int) h);
		}
		if (mergeable(surrounding[2])) {
			p.fillRectangle((int) x, (int) y, (int) w, (int) hh);
		}
		if (mergeable(surrounding[3])) {
			p.fillRectangle((int) xOff, (int) y, (int) ww, (int) h);
		}
	}
	
	private static boolean mergeable(short adj) {
		return adj == w || adj == d;
	}
	
	private static void drawArc(Pixmap p, boolean fill, float x, float y, float dx, float dy) {
		float move = (thickness - 1) / 2f;
		
		float thetai = dy < 0 ? (float) Math.PI : 0;
		if (dx > 0 && dy < 0 || dx < 0 && dy > 0)
			thetai += (float) Math.PI / 2;
		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		
		float dtheta = (float) Math.PI / (4 * (Math.max(dx, dy) + move));
		
		int stop;
		if (fill) {
			stop = (int) (2 * Math.max(dx, dy) + thickness - 1);
			dx = .5f;
			dy = .5f;
		}
		else {
			dx -= move;
			dy -= move;
			stop = thickness * 2 - 1;
		}
		for (int i = 0; i < stop; i++) {
			for (float theta = thetai; theta <= thetai + Math.PI / 2; theta += dtheta) {
				p.drawPixel((int) (x + dx * Math.cos(theta)), (int) (y + dy * Math.sin(theta))); 
			}
			dx += .5f;
			dy += .5f;
		}
	}
	
	private static void drawLine(Pixmap p, float x1, float y1, float x2, float y2) {
		float dx = y1 - y2;
		float dy = x2 - x1;
		float mag = (float) Math.sqrt(dx * dx + dy * dy);
		dx /= mag;
		dy /= mag;
		
		float move = (thickness - 1) / 2f;
		x1 -= dx * move;
		x2 -= dx * move;
		y1 -= dy * move;
		y2 -= dy * move;
		for (int i = 0; i < thickness; i++)
			p.drawLine((int) (x1 + i * dx), (int) (y1 + i * dy), (int) (x2 + i * dx), (int) (y2 + i * dy));
	}
	
	
	
}
