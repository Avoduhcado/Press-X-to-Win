package core.keyboard;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import core.Achievements;
import core.Theater;


public enum Keybinds {
	
	X (Keyboard.KEY_X);
	
	private Press key;
	
	Keybinds(int k) {
		this.key = new Press(k);
	}
	
	public static void update() {
		for(Keybinds keybinds : Keybinds.values()) {
			keybinds.key.setHeld(keybinds.key.isPressed());
			keybinds.key.update();
		}
	}
	
	/** Key has been pressed. */
	public boolean press() {
		return key.isPressed();
	}
	
	/** Key has been and is currently pressed. */
	public boolean held() {
		return key.isHeld();
	}
	
	/** Key was pressed and is no longer pressed. */
	public boolean clicked() {
		if(key.isPressed() && !key.isHeld()) {
			if(!Achievements.PRESSX.getGot()) {
				Achievements.PRESSX.get();
				Achievements.PRESSX.showGet(Theater.get().getStage());
			}
			return true;
		} else
			return false;
	}
	
	/** Key was released. */
	public boolean released() {
		return key.isReleased();
	}

	public static void clear() {
		Keyboard.destroy();
		try {
			Keyboard.create();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		for(Keybinds e : Keybinds.values()) {
			e.key.setPressed(false);
		}
	}
}
