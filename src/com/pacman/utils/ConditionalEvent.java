package com.pacman.utils;

import com.pacman.utils.ActionTimer.Action;

import structures.LList;

public class ConditionalEvent implements Event {
	
	private LList<CA> cas;
	
	public ConditionalEvent() {
		cas = new LList<>();
	}
	
	public void add(Condition c, Action a) {
		cas.add(new CA(c, a));
	}
	
	public void check() {
		for (@SuppressWarnings("unused") CA c : cas.delIter(a -> a.check()));
	}
	
	public boolean empty() {
		return cas.isEmpty();
	}
	
	private static class CA {
		private Condition c;
		private Action a;
		
		CA(Condition c, Action a) {
			this.c = c;
			this.a = a;
		}
		
		boolean check() {
			if (c.act()) {
				a.act();
				return true;
			}
			return false;
		}
	}
	
	public static interface Condition {
		boolean act();
	}
	
}
