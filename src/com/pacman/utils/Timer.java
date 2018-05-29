package com.pacman.utils;

public class Timer {
	
	private long time;
	
	public Timer() {
		set();
	}
	
	public long time() {
		return System.currentTimeMillis() - time;
	}
	
	public void set() {
		this.time = System.currentTimeMillis();
	}
	
	public void setBack(long time) {
		this.time += time;
	}
	
}
