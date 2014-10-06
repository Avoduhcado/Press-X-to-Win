package core;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import core.utility.Text;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Screen {

	public int displayWidth = 800;
	public int displayHeight = 600;
	public Rectangle2D camera;
	
	private float fadeTotal;
	private float fadeTimer;
	private float fade = 0f;
	
	public Screen() {
		try {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.setTitle("Press X To Win! " + " FPS: " + Theater.fps + " Woohoo Ludum Dare!");
			try {
				Display.setIcon(loadIcon(System.getProperty("resources") + "/ui/Icon.png"));
			} catch (IOException e) {
				System.out.println("Failed to load icon");
			}
			Display.setResizable(false);
			Display.create();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
		
		camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
	}
	
	public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }
	
	public void update() {
		Display.update();
		Display.sync(200);
	}
	
	public void updateHeader() {
		Display.setTitle("Press X To Win! " + " FPS: " + Theater.fps + " Woohoo Ludum Dare!");
	}
	
	public void draw(Stage stage) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// Draw the backdrop
		if(stage.backdrop != null)
			stage.backdrop.draw();
		
		// Sort the z-buffer 
		for(int x = 0; x<stage.scenery.size(); x++) {
			for(int i = x; i>=0 && i>x-5; i--) {
				if(stage.scenery.get(x).getBox().getMaxY() < stage.scenery.get(i).getBox().getMaxY()) {
					stage.scenery.add(i, stage.scenery.get(x));
					stage.scenery.remove(x+1);
					x--;
				}
			}
		}
		// Draw the scenery
		for(int x = 0; x<stage.scenery.size(); x++) {
			stage.scenery.get(x).draw();
		}
		
		// Draw the win text
		if(!stage.winList.isEmpty()) {
			stage.winList.get(0).draw();
		}
		
		// Draw the HUD
		if(stage.HUD != null) {
			stage.HUD.draw();
		}
		
		// Process fading
		if(fadeTimer != 0f) {
			fade();
		}
		
		// Draw any achievement gets
		if(!stage.achievementsToShow.isEmpty()) {
			Text.drawString("Achievement Get!", 50, 15, Color.pink);
			Text.drawString(stage.achievementsToShow.peek(), 15, 40, Color.white);
		}
	}
	
	public boolean toBeClosed() {
		if(Display.isCloseRequested()) 
			return true;
		
		return false;
	}
	
	public void close() {
		Display.destroy();
	}
	
	public void setFadeTimer(float fadeTimer) {
		this.fadeTimer = fadeTimer;
		this.fadeTotal = fadeTimer;
		
		if(fadeTimer > 0f)
			fade = 0f;
		else
			fade = 1f;
	}
	
	public void fade() {
		if(fadeTotal > 0f) {
			fade += (1f / fadeTotal) * Theater.getDeltaSpeed(0.025f);
			fadeTimer -= Theater.getDeltaSpeed(0.025f);
		} else if(fadeTotal < 0f) {
			fade -= (1f / fadeTotal) * Theater.getDeltaSpeed(0.025f);
			fadeTimer += Theater.getDeltaSpeed(0.025f);
		}
		
		if(fadeTotal > 0f ? fadeTimer < 0f : fadeTimer > 0f) {
			fadeTimer = 0f;
			Event.get().finishAction();
		}
		
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
	
}
