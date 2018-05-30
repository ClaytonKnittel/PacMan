package com.pacman.utils;

import java.util.LinkedList;

public class ActionList {
	
	private LinkedList<ActionDelay> actions;
	private boolean loop;
	
	public ActionList() {
		actions = new LinkedList<>();
		loop = false;
	}
	
	public void loop() {
		loop = true;
	}
	
	public void add(Action a) {
		actions.add(new ActionDelay(a, 1));
	}
	
	public void add(Action a, int numTimes) {
		actions.add(new ActionDelay(a, numTimes));
	}
	
	public boolean next() {
		if (actions.size() == 0)
			return false;
		ActionDelay a = actions.getFirst();
		if (--a.times > 0)
			return true;
		a.a.act();
		ActionDelay ad = actions.removeFirst();
		if (loop) {
			actions.add(ad);
			ad.reset();
		}
		return true;
	}
	
	private static class ActionDelay {
		Action a;
		final int total;
		int times;
		ActionDelay(Action a, int times) {
			this.a = a;
			this.total = times;
			this.times = times;
		}
		void reset() {
			this.times = total;
		}
	}
	
	public static interface Action {
		void act();
	}
	
}
