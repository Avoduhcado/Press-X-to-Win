package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import core.entity.Sprite;
import core.keyboard.Keybinds;

public class SplashScreen {

	private Queue<Sprite> splashImages = new LinkedList<Sprite>();
	private float timer;
	private float fade = 1f;
	
	public SplashScreen() {
		loadSplashes();
	}
	
	public void loadSplashes() {
		try {
			int totalScreens = (int)(Math.random() * 20) + 1;
			List<Integer> usedInts = new LinkedList<Integer>();
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/splash/splash text"));
			
			String line = null;
			for(int x = 0; x<totalScreens; x++) {
				int tempNumber = (int)(Math.random() * 20) + 1;
				if(!usedInts.contains(tempNumber)) {
					usedInts.add(tempNumber);
					while(tempNumber > 0) {
						line = reader.readLine();
						tempNumber--;
					}
					splashImages.add(new Sprite("/splash/" + line));
					reader.close();
					reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/splash/splash text"));
				} else {
					tempNumber = (int)(Math.random() * 20) + 1;
					x--;
				}
					
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if(Keybinds.X.clicked()) {
			if(!Achievements.SKIPSPLASH.getGot()) {
				Achievements.SKIPSPLASH.get();
				Achievements.SKIPSPLASH.showGet(Theater.get().getStage());
			}
			splashImages.poll();
			if(!splashImages.isEmpty()) {
				timer = 5f;
				fade = 1f;
			}
		}
		
		if(timer > 0f) {
			timer -= Theater.getDeltaSpeed(0.025f);
			
			if(timer >= 4f) {
				fade -= Theater.getDeltaSpeed(0.025f);
				if(fade < 0f)
					fade = 0f;
			} else if(timer <= 1f) {
				fade += Theater.getDeltaSpeed(0.025f);
			}
		} else {
			splashImages.poll();
			if(!splashImages.isEmpty()) {
				timer = 5f;
				fade = 1f;
			}
		}
	}
	
	public void draw() {
		splashImages.peek().draw(0, 0);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor4f(0f, 0f, 0f, fade);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), 0);
			GL11.glVertex2d(Theater.get().getScreen().camera.getWidth(), Theater.get().getScreen().camera.getHeight());
			GL11.glVertex2d(0, Theater.get().getScreen().camera.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public boolean isSplashing() {
		if(splashImages.isEmpty())
			return false;
		
		return true;
	}
	
}
