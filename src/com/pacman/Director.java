package com.pacman;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pacman.utils.Screen;

public class Director {
	
	private static ArrayList<Screen> screens;
	private static Screen current;
	
	static {
		screens = new ArrayList<>();
		current = null;
	}
	
	public static void add(Screen s) {
		screens.add(s);
	}
	
	public static Screen current() {
		return current;
	}
	
	public static void setCurrent(Screen s) {
		if (s == null)
			return;
		if (current != null)
			current.stop();
		current = s;
		start();
	}
	
	public static void setCurrent(int i) {
		if (i == -1)
			return;
		if (current != null)
			current.stop();
		current = screens.get(i);
		start();
	}
	
	private static void start() {
		current.start();
		Gdx.input.setInputProcessor(current.input());
	}
	
	public static void init() {
		for (Screen s : screens)
			s.init();
	}
	
	public static void draw(Batch b) {
		current.draw(b);
	}
	
	public static void dispose() {
		for (Screen screen : screens)
			screen.dispose();
	}
	
}
