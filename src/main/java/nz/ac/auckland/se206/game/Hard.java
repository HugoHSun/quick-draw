package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.CategorySelector.Difficulty;

public class Hard extends Game {
  public Hard() {
    winningRank = 1;
    remainingTime = 30;

    List<Difficulty> difficulty = new ArrayList<Difficulty>();
    difficulty.add(Difficulty.E);
    difficulty.add(Difficulty.M);
    difficulty.add(Difficulty.H);
    categoryToDraw = CategorySelector.getRandomCategory(difficulty);
    currentPredictions = null;
    confidence = 0.25;
    visiblePrediction = 5;
  }
}
