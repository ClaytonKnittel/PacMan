package com.pacman.entities;

import tensor.IVector2;
import tensor.Vector2;

public class Inky extends Ghost {
	
	private IVector2 startingPos;
	private Ghost blinky;
	private static final IVector2 cornerTarget = new IVector2(26, 29);
	
	public Inky(float x, float y) {
		super(x, y, 13, 13, Entity.inky);
		startingPos = new IVector2(pos());
	}
	
	public void setBlinky(Ghost blinky) {
		this.blinky = blinky;
	}
	
	protected IVector2 chaseTarget() {
		IVector2 blinkyTar = blinky.target();
		if (blinkyTar == null)
			return cornerTarget();
		Vector2 v = blinky.screenPos();
		Vector2 d = board().screenPos(blinkyTar);
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
	
	protected IVector2 boxTarget() {
		return startingPos;
	}
	
	protected IVector2 cornerTarget() {
		return cornerTarget;
	}
}
