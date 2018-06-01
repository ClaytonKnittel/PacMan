package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
	
	SpriteBatch batch;
	
	public static int width, height;
	
	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(448, 526);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		Gdx.graphics.setResizable(false);
		
		batch = new SpriteBatch();
		
		Startup s = new Startup(width, height);
		Director.add(s);
		Game game = new Game(width, height);
		Director.add(game);
		
		Director.init();
		
		Director.setCurrent(s);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		Color c = Director.current().bgColor();
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		Director.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		Director.dispose();
	}
}
