package com.pacman.input;

public interface Controller {
	
	int dir();
	
	void setDir(int dir);
	
	/**
	 * Called every frame
	 * @return
	 */
	int update();
	
	/**
	 * Called when the entity enters a spot with a turn
	 * @return
	 */
	int onTurn();
	
	/**
	 * Called when an entity enters a decision space (more than one turn possible)
	 * @return
	 */
	int onDecision();
}
