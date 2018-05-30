package com.pacman.utils;

import com.pacman.utils.ActionTimer.Action;

import structures.LList;

public class EventList {
	
	private LList<ActionTimer> actions;
	
	public EventList() {
		actions = new LList<>();
	}
	
	public void add(Action a, float delay) {
		ActionTimer ad = new ActionTimer();
		ad.add(a, delay);
		actions.add(ad);
	}
	
	public void add(ActionTimer t) {
		actions.add(t);
	}
	
	public void check() {
		for (ActionTimer a : actions)
			a.check();
		for (@SuppressWarnings("unused") ActionTimer a : actions.delIter(at -> at.empty()));
	}
	
}
