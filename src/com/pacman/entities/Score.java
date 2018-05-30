package com.pacman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.graphics.Drawable;

public class Score implements Drawable {
	
	private int x, y, width, height;
	private int score;
	private TextureRegion texture;
	
	BitmapFont b = new BitmapFont();
	
	public Score(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		score = 0;
		genTex();
		loadFont();
	}
	
	public void reset() {
		score = 0;
	}
	
	public void add(int points) {
		score += points;
	}
	
	private void genTex() {
		Pixmap p = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		p.setColor(new Color(0, 0, 0, 1));
		p.fill();
		texture = new TextureRegion(new Texture(p));
	}
	
	private void loadFont() {
		b = new BitmapFont(Gdx.files.internal("/Users/claytonknittel/Documents/workspace/Projects/libgdx/core/assets/pacman_font.fnt"), false);
		b.getData().setScale(.4f);
	}
	
	public void draw(Batch batch) {
		batch.draw(texture(), x, y);
		b.draw(batch, "Score", x + width / 4, y + height * 7 / 8);
		b.draw(batch, score + "", x + width / 3, y + height * 3 / 8);
	}
	
	@Override
	public TextureRegion texture() {
		return texture;
	}
	
	
}
