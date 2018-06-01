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
	private int score, level, lives;
	private TextureRegion texture;
	private TextureRegion lifeTexture;

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
	
	public int score() {
		return score;
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
		
		lifeTexture = Entity.getTexture(1);
	}
	
	private void loadFont() {
		b = new BitmapFont(Gdx.files.internal("./pacman_font.fnt"), false);
		b.getData().setScale(.4f);
	}
	
	public void setLevelAndLives(int level, int lives) {
		this.level = level;
		this.lives = lives;
	}
	
	public void draw(Batch batch) {
		batch.draw(texture, x, y);
		b.draw(batch, "Score", x + width / 4, y + height * 7 / 8);
		b.draw(batch, score + "", x + width / 3, y + height * 3 / 8);
		b.draw(batch, "Level", x + width * 2 / 3, y + height * 7 / 8);
		b.draw(batch, level + "", x + width * 3 / 4, y + height * 3 / 8);
		
		if (lives <= 3)
			drawAllLives(batch);
		else
			drawMultiplier(batch);
	}
	
	private void drawAllLives(Batch batch) {
		int wid = lifeTexture.getRegionWidth();
		int x = this.x + width * 19 / 20 - wid * 3;
		int y = this.y + (height - lifeTexture.getRegionHeight() - 3) / 2;
		for (int i = 0; i < lives; i++)
			batch.draw(lifeTexture, x + i * wid, y);
	}
	
	private void drawMultiplier(Batch batch) {
		int x = this.x + width * 19 / 20 - lifeTexture.getRegionWidth() * 3;
		int y = this.y + (height - lifeTexture.getRegionHeight() - 3) / 2;
		batch.draw(lifeTexture, x, y);
		x += lifeTexture.getRegionWidth();
		b.draw(batch, "*" + lives, x, y + b.getCapHeight() + b.getAscent() / 2);
	}
	
	
}
