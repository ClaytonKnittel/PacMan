package com.pacman.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BigDot extends Entity implements Collectable {
	
private static final TextureRegion texture;
	
	private static final Color dotColor = new Color(0xdfbab5ff);
	private static final int value = 50;
	
	static {
		Pixmap p = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
		
		p.setColor(dotColor);
		p.fillRectangle(6, 6, 4, 4);
		
		texture = new TextureRegion(new Texture(p));
	}
	
	public BigDot(float x, float y) {
		super(x, y, 4, 4);
	}
	
	@Override
	public void update(long delta) {
		return;
	}
	
	@Override
	public void collect() {
		delete();
		game().scare();
		game().add(value);
		game().eatDot();
	}

	public TextureRegion texture() {
		return texture;
	}
	
}
