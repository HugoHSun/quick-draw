package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.util.CategorySelector;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class Easy extends Game {
  public Easy() {
    winningRank = 3;
    remainingTime = 60;

    List<Difficulty> difficulty = new ArrayList<Difficulty>();
    difficulty.add(Difficulty.E);
    categoryToDraw = CategorySelector.getRandomCategory(difficulty);
    currentPredictions = null;
    confidence = 0.01;
    visiblePrediction = 10;
  }
}
