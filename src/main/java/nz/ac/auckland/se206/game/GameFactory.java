package nz.ac.auckland.se206.game;

import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class GameFactory {
  public static Game createGame(Difficulty difficulty) {
    switch (difficulty) {
      case E:
        return new Easy();

      case M:
        return new Medium();

      case H:
        return new Hard();

      case X:
        return new Master();

      default:
        return null;
    }
  }
}
