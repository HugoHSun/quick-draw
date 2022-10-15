package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.CategorySelector.Difficulty;

public class Medium extends Game {
  public Medium() {
    winningRank = 2;
    remainingTime = 45;

    List<Difficulty> difficulty = new ArrayList<Difficulty>();
    difficulty.add(Difficulty.E);
    difficulty.add(Difficulty.M);
    categoryToDraw = CategorySelector.getRandomCategory(difficulty);
    currentPredictions = null;
    confidence = 0.1;
    visiblePrediction = 8;
  }
}
