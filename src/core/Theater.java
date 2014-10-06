package core;

import core.keyboard.Keybinds;
import core.sounds.Ensemble;
import core.utility.Text;

public class Theater {

	private Stage stage;
	private Screen screen;
	private SplashScreen splashScreen;
	
	private boolean playing;
	
	private float delta;
	private float deltaMax = 25f;
	private long currentTime;
	private long lastLoopTime;
	public static int fps = 0;
	
	private static Theater theater;
	
	public static void init() {
		theater = new Theater();
	}
	
	public static Theater get() {
		return theater;
	}
	
	public Theater() {
		screen = new Screen();
		stage = new Stage();
		if(Ensemble.get().getBackground() != null)
			Ensemble.get().getBackground().pause();
		splashScreen = new SplashScreen();
		if(!Achievements.STARTGAME.getGot()) {
			Achievements.STARTGAME.get();
			Achievements.STARTGAME.showGet(stage);
		}
	}
	
	public void update() {
		getFps();
		
		if(splashScreen == null)
			Ensemble.get().update();
		
		if(splashScreen == null)
			screen.draw(stage);
		else {
			splashScreen.draw();
			splashScreen.update();
		}
		screen.update();
		
		if(splashScreen == null)
			stage.act();
		
		Input.checkInput();
		
		if(screen.toBeClosed()) {
			if(!Achievements.QUITGAME.getGot() && splashScreen == null) {
				Achievements.QUITGAME.get();
				Achievements.QUITGAME.showGet(stage);
				float x = 3f;
				while(x > 0f && !Keybinds.X.clicked()) {
					screen.draw(stage);
					screen.update();
					stage.act();
					Input.checkInput();
					x -= getDeltaSpeed(0.025f);
				}
			}
			close();
		}
	}
	
	public void play() {
		while(splashScreen.isSplashing()) {
			update();
		}
		splashScreen = null;
		if(Ensemble.get().getBackground() != null)
			Ensemble.get().getBackground().resume();
		
		playing = true;
		Text.setFont();
		
		while(playing) {
			update();
		}
	}
	
	public void close() {
		screen.close();
		System.exit(0);
	}
	
	public void getFps() {
		delta = getTime() - currentTime;
		currentTime = getTime();
		lastLoopTime += delta;
		fps++;
		if(lastLoopTime >= 1000) {
			screen.updateHeader();
			fps = 0;
			lastLoopTime = 0;
		}
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	public static float getDeltaSpeed(float speed) {
		return (Theater.get().delta * speed) / Theater.get().deltaMax;
	}
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/natives");
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		
		Theater.init();
		theater.play();
	}

}
