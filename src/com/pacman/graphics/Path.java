package com.pacman.graphics;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tensor.Vector2;

public class Path implements Drawable {
	
	private TextureRegion texture;
	private LinkedList<Vector2> points;
	
	public Path(Color c) {
		points = new LinkedList<>();
		genTexture(c);
	}
	
	private void genTexture(Color c) {
		Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		p.setColor(c);
		p.drawPixel(0, 0);
		texture = new TextureRegion(new Texture(p));
	}
	
	public void clear() {
		points.clear();
	}
	
	public void add(Vector2 point) {
		points.add(point);
	}

	@Override
	public void draw(Batch b) {
		drawDistLess(b, -1);
	}
	
	public void drawDistLess(Batch b, float maxDist) {
		if (points.size() == 0)
			return;
		Vector2 last = points.getFirst();
		for (Vector2 p : points) {
			if (p == last)
				continue;
			float dx = p.x() - last.x();
			float dy = p.y() - last.y();
			float x = last.x();
			float y = last.y();
			if (dx < 0) {
				x = p.x();
				dx *= -1;
			}
			if (dy < 0) {
				y = p.y();
				dy *= -1;
			}
			if (Math.max(dx, dy) <= maxDist || maxDist == -1) {
				if (dx == 0)
					dx++;
				if (dy == 0)
					dy++;
				b.draw(texture, x, y, dx, dy);
			}
			last = p;
		}
	}

}
