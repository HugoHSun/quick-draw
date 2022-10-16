package nz.ac.auckland.se206.game;

import ai.djl.modality.Classifications.Classification;
import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.util.CategorySelector;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class Game {

  // In seconds
  protected Integer remainingTime;

  protected Integer winningRank;

  protected Category categoryToDraw;

  protected List<Classification> currentPredictions;

  protected Double confidence;

  protected Integer visiblePrediction;

  private int prevRank;

  /**
   * Constructor for the game. It gets input list of difficulties, consisting of
   * accuracy, word difficulty, time, confidence and visibility. Assign variable with specific rules from client
   * 
   * @param difficulty
   */
  public Game(List<Difficulty> difficulty) {
    prevRank = 344;
    if (difficulty.get(0).equals(Difficulty.E)) {
      winningRank = 3;
    } else if (difficulty.get(0).equals(Difficulty.M)) {
      winningRank = 2;
    } else if (difficulty.get(0).equals(Difficulty.H)) {
      winningRank = 1;
    } else if (difficulty.get(0).equals(Difficulty.X)) {
      winningRank = 1;
    }

    if (difficulty.get(1).equals(Difficulty.E)) {
      List<Difficulty> dif = new ArrayList<Difficulty>();
      dif.add(Difficulty.E);
      categoryToDraw = CategorySelector.getRandomCategory(dif);
    } else if (difficulty.get(1).equals(Difficulty.M)) {
      List<Difficulty> dif = new ArrayList<Difficulty>();
      dif.add(Difficulty.E);
      dif.add(Difficulty.M);
      categoryToDraw = CategorySelector.getRandomCategory(dif);
    } else if (difficulty.get(1).equals(Difficulty.H)) {
      List<Difficulty> dif = new ArrayList<Difficulty>();
      dif.add(Difficulty.E);
      dif.add(Difficulty.M);
      dif.add(Difficulty.H);
      categoryToDraw = CategorySelector.getRandomCategory(dif);
    } else if (difficulty.get(1).equals(Difficulty.X)) {
      List<Difficulty> dif = new ArrayList<Difficulty>();
      dif.add(Difficulty.H);
      categoryToDraw = CategorySelector.getRandomCategory(dif);
    }

    if (difficulty.get(2).equals(Difficulty.E)) {
      remainingTime = 60;
    } else if (difficulty.get(2).equals(Difficulty.M)) {
      remainingTime = 45;
    } else if (difficulty.get(2).equals(Difficulty.H)) {
      remainingTime = 30;
    } else if (difficulty.get(2).equals(Difficulty.X)) {
      remainingTime = 15;
    }

    if (difficulty.get(3).equals(Difficulty.E)) {
      confidence = 0.01;
    } else if (difficulty.get(3).equals(Difficulty.M)) {
      confidence = 0.1;
    } else if (difficulty.get(3).equals(Difficulty.H)) {
      confidence = 0.25;
    } else if (difficulty.get(3).equals(Difficulty.X)) {
      confidence = 0.5;
    }

    if (difficulty.get(4).equals(Difficulty.E)) {
      visiblePrediction = 10;
    } else if (difficulty.get(4).equals(Difficulty.M)) {
      visiblePrediction = 8;
    } else if (difficulty.get(4).equals(Difficulty.H)) {
      visiblePrediction = 5;
    } else if (difficulty.get(4).equals(Difficulty.X)) {
      visiblePrediction = 3;
    }

    currentPredictions = null;
  }

  /** This method decreases the remaining time by 1 second */
  public void decreaseTime() {
    remainingTime--;
  }

  /**
   * This is a helper method which reminds user of the remaining time by text to speech
   *
   * @param reminderTime the time to remind the user
   */
  public void remindTimeLeft(int reminderTime) {
    if (remainingTime == (reminderTime + 1)) {
      Thread timeReminder =
          new Thread(
              () -> new TextToSpeech().speak(Integer.toString(reminderTime) + " seconds left"));
      timeReminder.start();
    }
  }

  /**
   * This method returns the remaining time of the game
   *
   * @return the remaining time of the game
   */
  public Integer getRemainingTime() {
    return remainingTime;
  }

  /**
   * This method returns the category to be drawn of the game
   *
   * @return the category to be drawn of the game
   */
  public String getCategoryToDraw() {
    return categoryToDraw.getCategoryToDraw();
  }

  public Difficulty getCategoryDifficulty() {
    return categoryToDraw.getDifficulty();
  }

  /**
   * This is a helper method that builds a string
   *
   * @return a string containing the top x predictions
   */
  public String getTopPredictionsDisplay() {
    // Build the string to display the top ten predictions
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < winningRank; i++) {
      // Build the predictions string to be displayed
      Classification currentClass = currentPredictions.get(i);
      sb.append(currentClass.getClassName().replaceAll("_", " "))
          .append(" : ")
          .append(String.format("%d%%", Math.round(100 * currentClass.getProbability())))
          .append(System.lineSeparator());
    }

    return sb.toString();
  }

  /**
   * This is a helper method that builds a string of the current top 10 predictions, which can be
   * displayed in a label.
   *
   * @return a string containing the top 10 predictions
   */
  public String getRemainingPredictionsDisplay() {
    // Build the string to display the top ten predictions
    final StringBuilder sb = new StringBuilder();

    for (int i = winningRank; i < visiblePrediction; i++) {
      // Build the predictions string to be displayed
      Classification currentClass = currentPredictions.get(i);
      sb.append(currentClass.getClassName().replaceAll("_", " "))
          .append(" : ")
          .append(String.format("%d%%", Math.round(100 * currentClass.getProbability())))
          .append(System.lineSeparator());
    }

    return sb.toString();
  }

  /**
   * This method updates the current predictions of the game
   *
   * @param predictions the most recent predictions
   */
  public void updatePredictions(List<Classification> predictions) {
    currentPredictions = predictions;
  }

  public String checkImprovement() {
    int currentRank = getCurrentPredictionRank();

    // The prediction rank has improved
    if (currentRank < prevRank) {
      prevRank = currentRank;
      return "Getting Closer...";
      // The rank got worse
    } else if (currentRank > prevRank) {
      prevRank = currentRank;
      return "Uh Oh, Getting Further";
      // No change
    } else {
      return "No Change...";
    }
  }

  private Integer getCurrentPredictionRank() {
    for (int i = 0; i < currentPredictions.size(); i++) {
      String currentPrediction = currentPredictions.get(i).getClassName().replaceAll("_", " ");
      if (currentPrediction.equals(categoryToDraw.getCategoryToDraw())) {
        return i;
      }
    }

    return null;
  }

  /**
   * @return true if there are no time left, false otherwise
   */
  public boolean checkTimeOut() {
    return remainingTime == 0;
  }

  /**
   * winningRank the prediction rank that the player need to get into to win
   *
   * @return true if the player has won, false otherwise
   */
  public boolean checkWon() {
    if (currentPredictions == null) {
      return false;
    }

    // The player wins if top chosen number AI predictions include the category
    for (int i = 0; i < winningRank; i++) {
      String currentPrediction = currentPredictions.get(i).getClassName().replaceAll("_", " ");
      double currentProbability = currentPredictions.get(i).getProbability();
      if (currentPrediction.equals(categoryToDraw.getCategoryToDraw())
          && currentProbability > confidence) {
        return true;
      }
    }

    return false;
  }
}
