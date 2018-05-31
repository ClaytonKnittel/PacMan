package com.pacman.utils;

import java.util.LinkedList;

public class ActionTimer extends Timer implements Event {
	
	private LinkedList<ActionDelay> actions;
	private boolean loop;
	
	public ActionTimer() {
		actions = new LinkedList<>();
		loop = false;
	}
	
	public void loop() {
		loop = true;
	}
	
	public void clear() {
		actions.clear();
		super.set();
	}
	
	public void add(Action a, float secondsAfter) {
		actions.add(new ActionDelay(a, (long) (secondsAfter * 1000)));
	}
	
	public boolean empty() {
		return actions.isEmpty();
	}
	
	public void check() {
		if (actions.size() == 0)
			return;
		ActionDelay a = actions.getFirst();
		if (super.time() >= a.delay) {
			a.a.act();
			super.setBack(a.delay);
			ActionDelay ad = actions.removeFirst();
			if (loop)
				actions.add(ad);
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
