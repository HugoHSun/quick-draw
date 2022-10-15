package nz.ac.auckland.se206.game;

import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class Category {
  private String categoryToDraw;
  private Difficulty difficulty;

  public Category(String categoryToDraw, Difficulty difficulty) {
    this.setCategoryToDraw(categoryToDraw);
    this.setDifficulty(difficulty);
  }

  public String getCategoryToDraw() {
    return categoryToDraw;
  }

  public void setCategoryToDraw(String categoryToDraw) {
    this.categoryToDraw = categoryToDraw;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }
}
