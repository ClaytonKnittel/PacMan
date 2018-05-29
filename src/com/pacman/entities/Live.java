package com.pacman.entities;

import java.util.LinkedList;

import com.pacman.input.Controller;

import methods.P;

public abstract class Live extends Entity {
	
	private Controller controller;
	
	public Live(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	protected void setController(Controller controller) {
		this.controller = controller;
	}
	
	public int nextTile() {
		if (d() == 0)
			return tile();
		return tile() + board().dTile(dir());
	}
	
	public abstract float speed();
	
	@Override
	public LinkedList<Integer> getTiles() {
		LinkedList<Integer> ret = new LinkedList<>();
		ret.add(tile());
		int n = nextTile();
		if (n != tile())
			ret.add(n);
		return ret;
	}
	
	@Override
	public final void update(long delta) {
		if (record(delta))
			updateState();
		move(speed() * delta / 1000f);
	}
	
	/**
	 * Sets the direction if pacman can move that direction
	 * @param d
	 */
	public boolean setDirIf(int d) {
		if (canMove(tile(), d)) {
			setDir(d);
			return true;
		}
		return false;
	}
	
	public boolean sharpTurn(int d) {
		return (d == up && dir() == down) || (d == down && dir() == up) || (d == left && dir() == right) || (d == right && dir() == left);
	}
	
	public void move(float d) {
		controller.update();
		if (!forward(d))
			setDir(controller.onTurn());
		if (sharpTurn(controller.dir()))
			turnAround();
		if (inNextTile()) {
			setTile();
			if (isDecisionTile())
				controller.onDecision();
			else
				controller.onTurn();
			if (!setDirIf(controller.dir()))
				setD(0);
			else
				moveD();
			posChange();
		}
	}
	
	public void turnAround() {
		setTile();
		setDir(Entity.opposite(dir()));
		controller.setDir(dir());
		flipD();
	}
	
	private boolean canMove(int tile, int dir) {
		if (Ghost.is(this))
			return !board().isGWall(tile, dir);
		return !board().isWall(tile, dir);
	}
	
	protected int turn() {
		if (canMove(tile(), up) && dir() != down)
			return up;
		if (canMove(tile(), down) && dir() != up)
			return down;
		if (canMove(tile(), left) && dir() != right)
			return left;
		if (canMove(tile(), right) && dir() != left)
			return right;
		return -1;
	}
	
	public abstract void updateState();
	
}
