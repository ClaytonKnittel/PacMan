package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
	
	SpriteBatch batch;
	Game game;
	
	public static int width, height;
	
	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(448, 496);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch();
		
		game = new Game(width, height);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.3f, .95f, .96f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		game.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		game.dispose();
	}
}
