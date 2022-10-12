package nz.ac.auckland.se206.game;

import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import java.util.List;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.CategorySelector.Difficulty;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class Game {

  // In seconds
  private Integer remainingTime;

  private String categoryToDraw;

  private List<Classification> currentPredictions;

  /**
   * This constructor instantiate a game with the specified time and difficulty
   *
   * @param gameTime the time for a game
   * @param difficulty the difficulty of the game
   */
  public Game(Integer gameTime, Difficulty difficulty) {
    remainingTime = gameTime;
    categoryToDraw = CategorySelector.getRandomCategory(difficulty);
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
    return categoryToDraw;
  }

  /**
   * This is a helper method that builds a string of the current top 10 predictions, which can be
   * displayed in a label.
   *
   * @return a string containing the top 10 predictions
   */
  public String getPredictionDisplay() {
    // Build the string to display the top ten predictions
    final StringBuilder sb = new StringBuilder();

    for (final Classifications.Classification classification : currentPredictions) {
      // Build the predictions string to be displayed
      sb.append(classification.getClassName().replaceAll("_", " "))
          .append(" : ")
          .append(String.format("%d%%", Math.round(100 * classification.getProbability())))
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

  /**
   * @return true if there are no time left, false otherwise
   */
  public boolean checkTimeOut() {
    return remainingTime == 0;
  }

  /**
   * @param winningRank the prediction rank that the player need to get into to win
   * @return true if the player has won, false otherwise
   */
  public boolean checkWon(int winningRank) {
    if (currentPredictions == null) {
      return false;
    }

    // The player wins if top chosen number AI predictions include the category
    for (int i = 0; i < winningRank; i++) {
      String currentPrediction = currentPredictions.get(i).getClassName().replaceAll("_", " ");
      if (currentPrediction.equals(categoryToDraw)) {
        return true;
      }
    }

    return false;
  }
}
