package core;

public enum Achievements {

	PRESSX (false, "Pressed X!"),
	STARTGAME (false, "Started the game!"),
	SKIPSPLASH (false, "Skipped a splash screen!"),
	BEGIN (false, "Began your adventure!"),
	CHARACTER (false, "Chose a character!"),
	READING (false, "You know how to read!"),
	CALIBRATED (false, "Adjusted your sights!"),
	SHOT (false, "You shot your gun!"),
	JUNGLE (false, "Welcome to the jungle!"),
	GOTHIT (false, "You got hit!"),
	RECOVERED (false, "You recovered your HP!"),
	BEATJUNGLE (false, "You saved Africa from Terrorism!"),
	ARCTIC (false, "I'm getting too cold for this..."),
	TRIPLE (false, "Triple kill!"),
	BEATARCTIC (false, "You foiled Terrorism!"),
	NEWYORK (false, "Concrete jungle where dreams are made of!"),
	SAVEAMERICA (false, "You killed so many aliens!"),
	BEATNEWYORK (false, "You restored FREEDOM to New York!"),
	SPACE (false, "It's the place."),
	SHIT (false, "SHIT"),
	KILLEDTERRORISM (false, "You defeated Terrorism!"),
	BEATTHEGAME (false, "You beat the game!"),
	QUITGAME (false, "Quit the game!");
	
	private boolean got;
	private String getText;
	
	Achievements(boolean got, String getText) {
		this.got = got;
		this.getText = getText;
	}
	
	public void get() {
		this.got = true;
	}
	
	public boolean getGot() {
		return got;
	}
	
	public String getGetText() {
		return getText;
	}
	
	public void showGet(Stage stage) {
		stage.addAchievementToShow(getGetText());
	}
	
}
