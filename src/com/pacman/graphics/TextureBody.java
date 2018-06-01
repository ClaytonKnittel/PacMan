package com.pacman.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.entities.Entity;
import com.pacman.utils.ActionTimer;

public class TextureBody implements Drawable {
	
	private float x, y;
	private int index;
	
	private int phase, dir;
	private float scale;
	
	private boolean visible;
	
	private ActionTimer t;
	private TextureRegion texture;
	
	public final ActionTimer WIGGLE_ANIMATION, EAT_ANIMATION;
	
	public TextureBody(int index, float x, float y, float scale, boolean visible) {
		this.x = x;
		this.y = y;
		this.index = index;
		
		phase = 0;
		dir = 0;
		this.scale = scale;
		
		setVisible(visible);
		
		WIGGLE_ANIMATION = new ActionTimer();
		WIGGLE_ANIMATION.loop();
		WIGGLE_ANIMATION.add(() -> phase = 1, .06f);
		WIGGLE_ANIMATION.add(() -> phase = 0, .06f);
		
		EAT_ANIMATION = new ActionTimer();
		EAT_ANIMATION.loop();
		EAT_ANIMATION.add(() -> phase = 1, .06f);
		EAT_ANIMATION.add(() -> phase = 2, .06f);
		EAT_ANIMATION.add(() -> phase = 1, .06f);
		EAT_ANIMATION.add(() -> phase = 0, .06f);
	}
	
	public TextureBody(int index, int x, int y) {
		this(index, x, y, 1, false);
	}
	
	public void setTexture(int i) {
		this.index = i;
	}
	
	public void pause() {
		if (t != null)
			t.pause();
	}
	
	public void resume() {
		if (t != null)
			t.resume();
	}
	
	public void setTexture(TextureRegion t) {
		this.texture = t;
	}
	
	public void setAnimation(ActionTimer t) {
		this.t = t;
	}
	
	public void move(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVisible(boolean v) {
		this.visible = v;
	}
	
	public void setPhase(int p) {
		this.phase = p;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public void update() {
		if (t != null)
			t.check();
	}
	
	public void setScale(float s) {
		this.scale = s;
	}
	
	public void draw(Batch b) {
		if (!visible)
			return;
		TextureRegion t;
		if (texture == null) {
			if (index == Entity.pacman) {
				if (phase == 2)
					t = Entity.getTexture(2);
				else
					t = Entity.getTexture(14 * dir + phase);
			}
			else
				t = Entity.getTexture(index + 2 * dir + phase);
		}
		else
			t = texture;
		float w = t.getRegionWidth() * scale;
		float h = t.getRegionHeight() * scale;
		b.draw(t, x - w / 2, y - h / 2, w, h);
	}
	
}
