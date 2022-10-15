package nz.ac.auckland.se206.game;

import ai.djl.modality.Classifications.Classification;
import java.util.List;
import nz.ac.auckland.se206.CategorySelector.Difficulty;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class Game {

  // In seconds
  protected Integer remainingTime;

  protected Integer winningRank;

  protected category categoryToDraw;

  protected List<Classification> currentPredictions;

  protected Double confidence;

  protected Integer visiblePrediction;

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
