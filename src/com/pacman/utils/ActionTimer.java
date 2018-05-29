package com.pacman.utils;

import java.util.LinkedList;

public class ActionTimer extends Timer {
	
	private LinkedList<ActionDelay> actions;
	
	public ActionTimer() {
		actions = new LinkedList<>();
	}
	
	public void add(Action a, float secondsAfter) {
		actions.add(new ActionDelay(a, (long) (secondsAfter * 1000)));
	}
	
	public void check() {
		if (actions.size() == 0)
			return;
		ActionDelay a = actions.getFirst();
		if (super.time() >= a.delay) {
			a.a.act();
			super.setBack(a.delay);
			actions.removeFirst();
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
