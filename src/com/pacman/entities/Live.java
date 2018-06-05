package com.pacman.entities;

import java.util.LinkedList;

import com.pacman.input.Controller;
import com.pacman.utils.ActionList;

import tensor.IVector2;

public abstract class Live extends Entity {
	
	private Controller controller;
	private ActionList animation;
	
	public Live(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	protected void setController(Controller controller) {
		this.controller = controller;
	}
	
	protected void setAnimation(ActionList a) {
		animation = a;
	}
	
	public IVector2 nextTile() {
		if (d() == 0)
			return pos();
		return board().newPos(pos(), dir());
	}
	
	public IVector2 prevTile() {
		return board().newPos(pos(), Entity.opposite(dir()));
	}
	
	public abstract float speed();
	
	@Override
	public LinkedList<Integer> getTiles() {
		LinkedList<Integer> ret = new LinkedList<>();
		ret.add(board().tile(pos()));
		IVector2 n = nextTile();
		if (!n.equals(pos()))
			ret.add(board().tile(n));
		return ret;
	}
	
	@Override
	public final void update(long delta) {
		if (record(delta)) {
			if (animation != null)
				animation.next();
		}
		move(speed() * delta / 1000f);
	}
	
	/**
	 * Sets the direction if pacman can move that direction
	 * @param d
	 */
	public boolean setDirIf(int d) {
		if (canMove(pos(), d)) {
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
			setPos();
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
		setPos();
		setDir(Entity.opposite(dir()));
		controller.setDir(dir());
		flipD();
	}
	
	private boolean canMove(IVector2 pos, int dir) {
		if (Ghost.is(this))
			return !board().isGWall(pos, dir);
		return !board().isWall(pos, dir);
	}
	
	protected int turn() {
		if (canMove(pos(), up) && dir() != down)
			return up;
		if (canMove(pos(), down) && dir() != up)
			return down;
		if (canMove(pos(), left) && dir() != right)
			return left;
		if (canMove(pos(), right) && dir() != left)
			return right;
		return -1;
	}
	
}
