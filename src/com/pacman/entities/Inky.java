package com.pacman.entities;

import tensor.Vector2;

public class Inky extends Ghost {
	
	private int startingPos;
	private Ghost blinky;
	
	public Inky(float x, float y) {
		super(x, y, 13, 13, Entity.inky);
		startingPos = board().boardPos(x, y);
	}
	
	public void setBlinky(Ghost blinky) {
		this.blinky = blinky;
	}
	
	protected int chaseTarget() {
		Vector2 v = new Vector2(blinky.x(), blinky.y());
		int target = blinky.target();
		Vector2 d = new Vector2(board().screenX(target), board().screenY(target));
		v = d.minus(v);
		d = d.plus(v);
		float width = board().width() - board().tileWidth() / 2;
		float height = board().height() - board().tileHeight() / 2;
		if (d.x() > width) {
			float m = v.y() / v.x();
			float diff = d.x() - width;
			d.subtract(new Vector2(diff, diff * m));
		}
		if (d.x() < .5f) {
			float m = v.y() / v.x();
			float diff = .5f - d.x();
			d.add(new Vector2(diff, diff * m));
		}
		if (d.y() > height) {
			float m = v.x() / v.y();
			float diff = d.y() - height;
			d.subtract(new Vector2(diff * m, diff));
		}
		if (d.y() < .5f) {
			float m = v.x() / v.y();
			float diff = .5f - d.y();
			d.add(new Vector2(diff * m, diff));
		}
		return board().nearest(d.x(), d.y());
	}
	
	protected int boxTarget() {
		return startingPos;
	}
	
	protected int cornerTarget() {
		return 0;
	}
}
