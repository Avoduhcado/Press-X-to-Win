package core.entity;

import java.awt.geom.Rectangle2D;

import core.Theater;

public class Entity {

	protected Sprite sprite;
	private String name;
	protected float x;
	protected float y;
	protected float dx;
	protected float dy;
	private Rectangle2D box;
	
	public Entity(String ref, float x, float y) {
		this.sprite = Sprite.loadSprite(ref);
		this.name = ref;
		this.x = x;
		this.y = y;
		this.box = new Rectangle2D.Double(x, y, sprite.getWidth(), sprite.getHeight());
	}
	
	public void update() {
		sprite.animate();
		if(dx != 0 || dy != 0) {
			x += Theater.getDeltaSpeed(dx);
			y += Theater.getDeltaSpeed(dy);
			updateBox();
		}
	}
	
	public void draw() {
		sprite.draw(x, y);
	}
	
	public void updateBox() {
		box.setFrame(x, y, box.getWidth(), box.getHeight());
	}
	
	public String getName() {
		return name;
	}
	
	public void setStill(boolean still) {
		this.sprite.setStill(still);
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
}
