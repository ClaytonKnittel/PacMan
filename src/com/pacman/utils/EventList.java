package com.pacman.utils;

import com.pacman.utils.ActionTimer.Action;

import structures.LList;

public class EventList {
	
	private LList<Event> actions;
	private long pauseTime;
	private boolean paused;
	
	public EventList() {
		actions = new LList<>();
		paused = false;
	}
	
	public void add(Action a, float delay) {
		ActionTimer ad = new ActionTimer();
		ad.add(a, delay);
		actions.add(ad);
	}
	
	public void add(Event t) {
		actions.add(t);
	}
	
	public void clear() {
		actions.clear();
	}
	
	public void pause() {
		pauseTime = System.currentTimeMillis();
		paused = true;
	}
	
	public void resume() {
		addTime(System.currentTimeMillis() - pauseTime);
		paused = false;
	}
	
	public void addTime(long millis) {
		for (Event a : actions) {
			if (Timer.class.isAssignableFrom(a.getClass()))
				((Timer) a).setBack(millis);
		}
	}
	
	public void check() {
		if (paused)
			return;
		for (Event a : actions)
			a.check();
		for (@SuppressWarnings("unused") Event a : actions.delIter(at -> at.empty()));
	}
	
}
