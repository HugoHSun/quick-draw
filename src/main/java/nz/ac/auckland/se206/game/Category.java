package nz.ac.auckland.se206.game;

import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class Category {
  private String categoryToDraw;
  private Difficulty difficulty;

  /**
   * Constructor for category class, initializes category to draw and the corresponding difficulty
   * 
   * @param categoryToDraw
   * @param difficulty
   */
  public Category(String categoryToDraw, Difficulty difficulty) {
    this.setCategoryToDraw(categoryToDraw);
    this.setDifficulty(difficulty);
  }

  /**
   * Getter for category to draw
   * 
   * @return
   */
  public String getCategoryToDraw() {
    return categoryToDraw;
  }

  /**
   * Setter for category to draw
   * 
   * @param categoryToDraw
   */
  public void setCategoryToDraw(String categoryToDraw) {
    this.categoryToDraw = categoryToDraw;
  }

  /**
   * Getter for difficulty
   * 
   * @return
   */
  public Difficulty getDifficulty() {
    return difficulty;
  }

  /**
   * Setter for difficulty
   * 
   * @param difficulty
   */
  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }
}
