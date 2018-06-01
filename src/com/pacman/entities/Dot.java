package com.pacman.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Dot extends Entity implements Collectable {
	
	public static final TextureRegion texture;
	private static final int value = 10;
	
	private static final Color dotColor = new Color(0xdfbab5ff);
	
	static {
		Pixmap p = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
		
		p.setColor(dotColor);
		p.fillRectangle(7, 7, 2, 2);
		
		texture = new TextureRegion(new Texture(p));
	}
	
	public Dot(float x, float y) {
		super(x, y, 2, 2);
	}
	
	@Override
	public void collect() {
		delete();
		game().add(value);
		game().eatDot();
	}
	
	@Override
	public void update(long delta) {
		return;
	}

	public TextureRegion texture() {
		return texture;
	}
	
}
