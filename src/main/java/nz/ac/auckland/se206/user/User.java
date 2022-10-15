package nz.ac.auckland.se206.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

public class User {

  private String name;

  private int gamesWon;

  private int gamesLost;

  private int fastestWon;

  private HashMap<Difficulty, List<String>> wordsEncountered;

  private List<Integer> badgesEarned;

  private List<Boolean> previousResults;

  private Difficulty currentDifficulty;

  public User(String name) {
    // Default values
    this.name = name;
    this.gamesWon = 0;
    this.gamesLost = 0;
    this.fastestWon = 61;

    this.wordsEncountered = new HashMap<Difficulty, List<String>>();
    wordsEncountered.put(Difficulty.E, new ArrayList<String>());
    wordsEncountered.put(Difficulty.M, new ArrayList<String>());
    wordsEncountered.put(Difficulty.H, new ArrayList<String>());

    this.badgesEarned = new ArrayList<Integer>();
    this.previousResults = new ArrayList<Boolean>();
    this.currentDifficulty = Difficulty.E;
  }

  public String getName() {
    return name;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public int getGamesLost() {
    return gamesLost;
  }

  public int getFastestWon() {
    return fastestWon;
  }

  public List<String> getWordsEncountered(Difficulty diff) {
    return wordsEncountered.get(diff);
  }

  public List<Integer> getBadgesEarned() {
    return badgesEarned;
  }

  public Difficulty getCurrentDifficulty() {
    return currentDifficulty;
  }

  public void setCurrentDifficulty(Difficulty dif) {
    currentDifficulty = dif;
  }

  public void newWord(Difficulty diff, String word) {
    wordsEncountered.get(diff).add(word);
  }

  public void newBadge(List<Integer> newBadges) {
    for (int newBadge : newBadges) {
      if (!(badgesEarned.contains(newBadge))) {
        badgesEarned.add(newBadge);
      }
    }
  }

  public void won() {
    gamesWon++;
  }

  public void lost() {
    gamesLost++;
  }

  public void record(boolean isWon) {
    if (previousResults.size() < 10) {
      previousResults.add(isWon);
    } else {
      previousResults.remove(0);
      previousResults.add(isWon);
    }
  }

  public void updateFastestWon(int fasterWon) {
    if (fasterWon < fastestWon) {
      fastestWon = fasterWon;
    }
  }

  public void obtainBadges() {
    List<Integer> newBadges = new ArrayList<Integer>();

    // Bronze badges
    if (this.gamesWon == 1) {
      newBadges.add(0);
    }
    if (this.gamesLost == 1) {
      newBadges.add(1);
    }
    if (this.fastestWon <= 30) {
      newBadges.add(2);
    }
    if (previousResults.size() >= 3
        && !(previousResults
            .subList(previousResults.size() - 3, previousResults.size())
            .contains(true))) {
      newBadges.add(3);
    }
    // implement badge 4

    // Silver badges
    if (this.gamesWon == 10) {
      newBadges.add(5);
    }
    if (this.currentDifficulty.equals(Difficulty.M)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(6);
    }
    if (this.fastestWon <= 15) {
      newBadges.add(7);
    }
    // Implement badge 8
    if (previousResults.size() >= 3
        && !(previousResults
            .subList(previousResults.size() - 3, previousResults.size())
            .contains(false))) {
      newBadges.add(9);
    }

    // Gold badges
    if (this.gamesWon == 50) {
      newBadges.add(10);
    }
    if (this.currentDifficulty.equals(Difficulty.X)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(11);
    }
    if (this.fastestWon <= 3) {
      newBadges.add(12);
    }
    // Implement badge 13
    if (previousResults.size() == 10 && !(previousResults.contains(false))) {
      newBadges.add(14);
    }

    // Diamond badges
    double winRate = 100.0 * (double) gamesWon / (gamesWon + gamesLost);
    if ((this.gamesWon + this.gamesLost >= 50) && winRate > 0.9) {
      newBadges.add(15);
    }
    if (this.gamesWon + this.gamesLost >= 1000) {
      newBadges.add(16);
    }
    // implement badge 17
    // implement badge 18
    if (badgesEarned.size() >= 19) {
      newBadges.add(19);
    }

    newBadge(newBadges);
  }

  public String toString() {
    double winRate = 100.0 * (double) gamesWon / (gamesWon + gamesLost);
    String fastestTime = String.valueOf(fastestWon);
    // Default
    if (fastestWon > 60) {
      fastestTime = "You have never won! :(";
    }

    if (gamesWon == 0 && gamesLost == 0) {
      winRate = 0.0;
    }

    // Return all the stats of the user as string
    return "Name : "
        + name
        + "\nGames Won : "
        + gamesWon
        + "\nGames Lost : "
        + gamesLost
        + "\nWin Rate : "
        + winRate
        + "%\nFastest time to victory (secs): "
        + fastestTime
        + "\nDifficulty Setting : "
        + currentDifficulty
        + "\nWords Encountered : \n"
        + "Level Easy: "
        + wordsEncountered.get(Difficulty.E)
        + "\nLevel Medium: "
        + wordsEncountered.get(Difficulty.M)
        + "\nLevel Hard: "
        + wordsEncountered.get(Difficulty.H);
  }
}
