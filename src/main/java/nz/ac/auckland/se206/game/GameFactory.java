package nz.ac.auckland.se206.game;

import nz.ac.auckland.se206.CategorySelector.Mode;

public class GameFactory {
	public static Game createGame(Mode mode) {
		switch(mode) {
		case EASY:
			return new Easy();
		
		case MEDIUM:
			return new Medium();
		
		case HARD:
			return new Hard();
			
		case MASTER:
			return new Master();
			
		default:
			return null;
		}
		
	}

}
