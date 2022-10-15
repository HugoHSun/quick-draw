package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.CategorySelector.Difficulty;

public class Master extends Game {
  public Master() {
    winningRank = 1;
    remainingTime = 15;

    List<Difficulty> difficulty = new ArrayList<Difficulty>();
    difficulty.add(Difficulty.H);
    categoryToDraw = CategorySelector.getRandomCategory(difficulty);
    currentPredictions = null;
    confidence = 0.5;
    visiblePrediction = 3;
  }
}
