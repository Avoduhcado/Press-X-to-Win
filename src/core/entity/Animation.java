package core.entity;

import core.Event;
import core.Theater;

public class Animation extends Entity {

	private boolean animating = true;
	
	public Animation(String ref, float x, float y) {
		super(ref, x, y);
	}
	
	public void update() {
		if(sprite.getFrame() == sprite.getMaxFrame() - 1)
			animating = false;
		sprite.animate();
		if(sprite.getFrame() == 0 && !animating) {
			Theater.get().getStage().removeAnimation(this);
			Event.get().finishAction();
		} else
			animating = true;
	}
	
	public boolean isAnimating() {
		return animating;
	}

}
