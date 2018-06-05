package com.pacman.utils;

import java.util.LinkedList;

public class ActionTimer extends Timer implements Event {
	
	private LinkedList<ActionDelay> actions;
	private long loop;
	private Action end;
	private long pauseTime;
	private boolean paused;
	
	public ActionTimer() {
		actions = new LinkedList<>();
		loop = 0;
		
		pauseTime = 0;
		paused = false;
	}
	
	public void loop() {
		loop = -1l;
	}
	
	public void loop(int times) {
		loop = (long) times * actions.size() - 1;
	}
	
	public void loop(int times, Action endAction) {
		loop(times);
		end = endAction;
	}
	
	public void clear() {
		actions.clear();
		super.set();
		if (loop > 0)
			loop = 0;
	}
	
	public void add(Action a, float secondsAfter) {
		actions.add(new ActionDelay(a, (long) (secondsAfter * 1000)));
	}
	
	public boolean empty() {
		return actions.isEmpty();
	}
	
	public void pause() {
		paused = true;
		pauseTime = System.currentTimeMillis();
	}
	
	public void resume() {
		resume(System.currentTimeMillis() - pauseTime);
	}
	
	public void resume(long delta) {
		paused = false;
		if (actions.size() > 0)
			setBack(delta);
		pauseTime = 0;
	}
	
	public void check() {
		if (paused)
			return;
		if (actions.size() == 0)
			return;
		ActionDelay a = actions.getFirst();
		if (super.time() >= a.delay) {
			ActionDelay ad = actions.removeFirst();
			if (a.delay >= 0) {
				super.setBack(a.delay);
				if (loop != 0) {
					if (loop > 0)
						loop--;
					actions.add(ad);
				} else if (end != null) {
					Action e = end;
					end = null;
					e.act();
				}
			}
			a.a.act();
			check();
		}
	}
	
	private static class ActionDelay {
		Action a;
		long delay;
		ActionDelay(Action a, long delay) {
			this.a = a;
			this.delay = delay;
		}
	}
	
	public static interface Action {
		void act();
	}
	
}
