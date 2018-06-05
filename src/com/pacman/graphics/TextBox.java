package com.pacman.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextBox implements Drawable {
	
	private static GlyphLayout g;
	private static final BitmapFont b;
	
	private String text;
	private int x, y;
	private float width, height;
	private Color c;
	
	private boolean visible;
	private boolean center;
	
	static {
		b = new BitmapFont(Gdx.files.internal("./pacman_font.fnt"), false);
		b.getData().setScale(.4f);
	}
	
	public TextBox(String text, int x, int y, Color c) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.c = c;
		visible = false;
		center = false;
		
		g = new GlyphLayout(b, text);
		width = g.width;
		height = g.height;
	}
	
	public void setCentered(boolean b) {
		center = b;
	}
	
	public void setVisible(boolean b) {
		visible = b;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Batch b) {
		if (visible) {
			TextBox.b.setColor(c);
			if (center)
				TextBox.b.draw(b, text, x - width / 2, y + (float) Math.ceil(height) / 2);
			else
				TextBox.b.draw(b, text, x, y + (float) Math.ceil(height) / 2);
		}
	}
	
}
