package com.pacman;

import java.util.LinkedList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pacman.entities.BigDot;
import com.pacman.entities.Dot;
import com.pacman.entities.Entity;
import com.pacman.entities.PacMan;
import com.pacman.graphics.Drawable;
import com.pacman.graphics.TextBox;
import com.pacman.graphics.TextureBody;
import com.pacman.input.KeyAction;
import com.pacman.utils.ActionTimer;
import com.pacman.utils.Screen;

public class Startup extends Screen {
	
	private TextureBody pacman, blinky, pinky, inky, clyde, eat;
	private TextureBody b, p, i, c;
	private TextBox bn, bnn, pn, pnn, in, inn, cn, cnn;
	private TextBox character, name;
	
	private TextureBody big, small;
	private TextBox bc, sc;
	
	private LinkedList<Drawable> entities;
	
	private ActionTimer animation;
	
	public Startup(int width, int height) {
		super(width, height);
	}

	@Override
	public void init() {
		entities = new LinkedList<>();
		
		character = new TextBox("Character", 115, 450, Color.WHITE);
		name = new TextBox("Nickname", 260, 450, Color.WHITE);
		character.setVisible(true);
		name.setVisible(true);
		
		b = new TextureBody(Entity.blinky, 80, 400, 2, false);
		b.setAnimation(b.WIGGLE_ANIMATION);
		bn = new TextBox("-Shadow", 130, 400, Entity.blinkyColor);
		bnn = new TextBox("\"Blinky\"", 260, 400, Entity.blinkyColor);
		
		p = new TextureBody(Entity.pinky, 80, 350, 2, false);
		p.setAnimation(p.WIGGLE_ANIMATION);
		pn = new TextBox("-Speedy", 130, 350, Entity.pinkyColor);
		pnn = new TextBox("\"Pinky\"", 260, 350, Entity.pinkyColor);
		
		i = new TextureBody(Entity.inky, 80, 300, 2, false);
		i.setAnimation(i.WIGGLE_ANIMATION);
		in = new TextBox("-Bashful", 130, 300, Entity.inkyColor);
		inn = new TextBox("\"Inky\"", 260, 300, Entity.inkyColor);
		
		c = new TextureBody(Entity.clyde, 80, 250, 2, false);
		c.setAnimation(c.WIGGLE_ANIMATION);
		cn = new TextBox("-Pokey", 130, 250, Entity.clydeColor);
		cnn = new TextBox("\"Clyde\"", 260, 250, Entity.clydeColor);
		
		big = new TextureBody(-1, 175, 80, 1, false);
		big.setTexture(BigDot.texture);
		small = new TextureBody(-1, 175, 100, 1, false);
		small.setTexture(Dot.texture);
		bc = new TextBox("50 pts", 205, 80, Color.WHITE);
		sc = new TextBox("10 pts", 205, 100, Color.WHITE);
		
		eat = new TextureBody(-1, 80, 160, 1, false);
		eat.setTexture(BigDot.texture);
		
		pacman = new TextureBody(Entity.pacman, 460, 160, 2, false);
		pacman.setAnimation(pacman.EAT_ANIMATION);
		pacman.setDir(Entity.left);
		
		blinky = new TextureBody(Entity.blinky, 525, 160, 2, false);
		blinky.setAnimation(blinky.WIGGLE_ANIMATION);
		blinky.setDir(Entity.left);
		
		pinky = new TextureBody(Entity.pinky, 565, 160, 2, false);
		pinky.setAnimation(pinky.WIGGLE_ANIMATION);
		pinky.setDir(Entity.left);
		
		inky = new TextureBody(Entity.inky, 605, 160, 2, false);
		inky.setAnimation(inky.WIGGLE_ANIMATION);
		inky.setDir(Entity.left);
		
		clyde = new TextureBody(Entity.clyde, 645, 160, 2, false);
		clyde.setAnimation(clyde.WIGGLE_ANIMATION);
		clyde.setDir(Entity.left);
		
		entities.add(character);
		entities.add(name);
		entities.add(b);
		entities.add(bn);
		entities.add(bnn);
		entities.add(p);
		entities.add(pn);
		entities.add(pnn);
		entities.add(i);
		entities.add(in);
		entities.add(inn);
		entities.add(c);
		entities.add(cn);
		entities.add(cnn);
		entities.add(big);
		entities.add(small);
		entities.add(bc);
		entities.add(sc);
		entities.add(eat);
		entities.add(pacman);
		entities.add(blinky);
		entities.add(pinky);
		entities.add(inky);
		entities.add(clyde);
	}

	@Override
	public void start() {
		animation = new ActionTimer();
		animation.add(() -> b.setVisible(true), .9f);
		animation.add(() -> bn.setVisible(true), .9f);
		animation.add(() -> bnn.setVisible(true), .5f);
		animation.add(() -> p.setVisible(true), .5f);
		animation.add(() -> pn.setVisible(true), .9f);
		animation.add(() -> pnn.setVisible(true), .5f);
		animation.add(() -> i.setVisible(true), .5f);
		animation.add(() -> in.setVisible(true), .9f);
		animation.add(() -> inn.setVisible(true), .5f);
		animation.add(() -> c.setVisible(true), .5f);
		animation.add(() -> cn.setVisible(true), .9f);
		animation.add(() -> cnn.setVisible(true), .5f);
		
		animation.add(() -> small.setVisible(true), .5f);
		animation.add(() -> sc.setVisible(true), .5f);
		animation.add(() -> big.setVisible(true), .5f);
		animation.add(() -> bc.setVisible(true), .5f);
		
		animation.add(() -> eat.setVisible(true), .8f);
		animation.add(() -> pacman.setVisible(true), .5f);
		animation.add(() -> blinky.setVisible(true), 0);
		animation.add(() -> pinky.setVisible(true), 0);
		animation.add(() -> inky.setVisible(true), 0);
		animation.add(() -> clyde.setVisible(true), 0);
		
		animation.add(() -> animation.loop(154, () -> reverse()), -1);
		animation.add(() -> stepLeft(), .01f);
	}
	
	private void reverse() {
		eat.setVisible(false);
		pacman.setDir(Entity.right);
		blinky.setDir(Entity.right);
		pinky.setDir(Entity.right);
		inky.setDir(Entity.right);
		clyde.setDir(Entity.right);
		blinky.setTexture(Entity.blue);
		pinky.setTexture(Entity.blue);
		inky.setTexture(Entity.blue);
		clyde.setTexture(Entity.blue);

		animation.add(() -> stepRight(), .01f);
		animation.loop(10, () -> kill(blinky, 0));
	}
	
	private void kill(TextureBody ghost, int which) {
		ghost.setTexture(112 + which);
		ghost.setDir(0);
		ghost.setPhase(0);
		pause();
		animation.add(() -> resume(), .5f);
		animation.add(() -> ghost.setVisible(false), 0);
		
		if (which == 3)
			animation.add(() -> animation.loop(100, () -> Director.setCurrent(1)), -1);
		else {
			switch(which) {
			case 0:
				animation.add(() -> animation.loop(36, () -> kill(pinky, 1)), -1);
				break;
			case 1:
				animation.add(() -> animation.loop(36, () -> kill(inky, 2)), -1);
				break;
			case 2:
				animation.add(() -> animation.loop(36, () -> kill(clyde, 3)), -1);
			}
		}
		animation.add(() -> stepRight(), .01f);
	}
	
	private void pause() {
		pacman.pause();
		blinky.pause();
		pinky.pause();
		inky.pause();
		clyde.pause();
	}
	
	private void resume() {
		pacman.resume();
		blinky.resume();
		pinky.resume();
		inky.resume();
		clyde.resume();
	}
	
	private void stepRight() {
		pacman.move(PacMan.speed * .04f, 0);
		blinky.move(PacMan.speed * .022f, 0);
		pinky.move(PacMan.speed * .022f, 0);
		inky.move(PacMan.speed * .022f, 0);
		clyde.move(PacMan.speed * .022f, 0);
	}
	
	private void stepLeft() {
		pacman.move(-PacMan.speed * .04f, 0);
		blinky.move(-PacMan.speed * .044f, 0);
		pinky.move(-PacMan.speed * .044f, 0);
		inky.move(-PacMan.speed * .044f, 0);
		clyde.move(-PacMan.speed * .044f, 0);
	}

	@Override
	public void stop() {
		
	}
	
	@Override
	public Color bgColor() {
		return Color.BLACK;
	}

	@Override
	public void draw(Batch b) {
		animation.check();
		for (Drawable d : entities) {
			if (d instanceof TextureBody)
				((TextureBody) d).update();
			d.draw(b);
		}
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public InputProcessor input() {
		return new KeyAction(() -> Director.setCurrent(1));
	}
	
	
}
