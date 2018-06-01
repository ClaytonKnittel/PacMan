package com.pacman.utils;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Screen {
	
	private int width, height;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int width() {
		return width;
	}
	public int height() {
		return height;
	}
	
	/**
	 * called on opening of application
	 */
	public abstract void init();
	
	/**
	 * called on opening of this screen
	 */
	public abstract void start();
	
	public abstract InputProcessor input();
	
	public abstract Color bgColor();
	
	/**
	 * called on opening a new screen from this one (closing this one)
	 */
	public abstract void stop();
	
	public abstract void draw(Batch b);
	
	public abstract void dispose();
}
