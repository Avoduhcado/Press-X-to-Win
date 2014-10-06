package core.entity;

import core.Event;
import core.Theater;

public class Actor extends Entity {

	private float distance;
	
	public Actor(String ref, float x, float y) {
		super(ref, x, y);
	}
	
	public void update() {
		super.update();
		
		if(distance > 0f) {
			if(dx != 0)
				distance -= Math.abs(Theater.getDeltaSpeed(dx));
			else if(dy != 0)
				distance -= Math.abs(Theater.getDeltaSpeed(dy));
			
			if(distance <= 0f) {
				dx = 0;
				dy = 0;
				
				Event.get().finishAction();
			}
		}
	}
	
	public void setMovement(int direction, float distance) {
		this.distance = distance;
		switch(direction) {
		case(0):
			dy = 3f;
			break;
		case(1):
			dx = 3f;
			break;
		case(2):
			dx = -3f;
			break;
		case(3):
			dy = -3f;
			break;
		}
	}
	
}
