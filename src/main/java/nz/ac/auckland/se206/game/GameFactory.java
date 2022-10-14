package nz.ac.auckland.se206.game;

public class GameFactory {
	public static Game createGame(String difficulty) {
		switch(difficulty) {
		case "Easy":
			return new Easy();
		
		case "Medium":
			return new Medium();
		
		case "Hard":
			return new Hard();
			
		case "Master":
			return new Master();
			
		default:
			return null;
		}
		
	}

}
