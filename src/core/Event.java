package core;

import core.entity.Actor;
import core.entity.Animation;
import core.sounds.Ensemble;
import core.sounds.SoundEffect;

public class Event {

	private boolean acting;
	
	private static Event event = new Event();
	
	public static Event get() {
		return event;
	}
	
	public void parseEvent(String event) {
		acting = true;
		
		String[] temp = event.split(";");
		if(temp[0].contains("Achievement")) {
			getAchievement(temp[1]);
		} else if(temp[0].contains("Fade Screen")) {
			fadeScreen(temp[1]);
		} else if(temp[0].contains("Show HUD")) {
			showHUD(Boolean.parseBoolean(temp[1]));
		} else if(temp[0].contains("Play Sound")) {
			playSound(temp[1]);
		} else if(temp[0].contains("Low HP")) {
			lowHP();
		} else if(temp[0].contains("Full HP")) {
			fullHP();
		} else if(temp[0].contains("Move")) {
			move(temp[1], Integer.parseInt(temp[2]), Float.parseFloat(temp[3]));
		} else if(temp[0].contains("Play Animation")) {
			playAnimation(temp[1], Float.parseFloat(temp[2]), Float.parseFloat(temp[3]));
		} else if(temp[0].contains("Add Actor")) {
			addActor(temp[1], Float.parseFloat(temp[2]), Float.parseFloat(temp[3]));
		} else if(temp[0].contains("Kill Actor")) {
			killActor(temp[1]);
		} else if(temp[0].contains("Remove Player")) {
			removePlayer();
		} else if(temp[0].contains("Hang")) {
			
		} else {
			System.out.println("Unknown event: " + temp[0]);
			acting = false;
		}
	}
	
	public boolean inAction() {
		return acting;
	}
	
	public void finishAction() {
		acting = false;
	}
	
	public void getAchievement(String achievement) {
		if(!Achievements.valueOf(achievement).getGot()) {
			Achievements.valueOf(achievement).get();
			Achievements.valueOf(achievement).showGet(Theater.get().getStage());
		}
		acting = false;
	}
	
	public void fadeScreen(String time) {
		Theater.get().getScreen().setFadeTimer(Float.parseFloat(time));
		Theater.get().getStage().winList.get(0).setAutoComplete(true);
	}
	
	public void showHUD(boolean show) {
		Theater.get().getStage().toggleHUD(show);
		
		acting = false;
	}
	
	public void playSound(String sound) {
		Ensemble.get().playSoundEffect(new SoundEffect(sound, 1f, false));
		
		acting = false;
	}
	
	public void lowHP() {
		Theater.get().getStage().swapHUD(true);
		Ensemble.get().playSoundEffect(new SoundEffect("LIFEALERT.ogg", 1f, true));
		acting = false;
	}
	
	public void fullHP() {
		Theater.get().getStage().swapHUD(false);
		Ensemble.get().removeSoundEffect("LIFEALERT");
		acting = false;
	}
	
	public void move(String entity, int direction, float distance) {
		Theater.get().getStage().getActor(entity).setMovement(direction, distance);
	}
	
	public void playAnimation(String animation, float x, float y) {
		Theater.get().getStage().addAnimation(new Animation(animation, x, y));
	}
	
	public void addActor(String name, float x, float y) {
		Theater.get().getStage().addActor(new Actor(name, x, y));
		acting = false;
	}
	
	public void removeActor(String name) {
		Theater.get().getStage().removeActor(name);
		acting = false;
	}
	
	public void killActor(String name) {
		removeActor(name);
		playSound("YEEAARRGH.ogg");
		playSound("Ratatat.ogg");
		acting = true;
		playAnimation("Gunshot", 380f, 425f);
	}
	
	public void removePlayer() {
		Theater.get().getStage().removePlayer();
		showHUD(false);
	}
	
}
