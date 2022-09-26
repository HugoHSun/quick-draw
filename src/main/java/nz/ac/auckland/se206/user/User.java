package nz.ac.auckland.se206.user;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String name;
  private int gamesWon;
  private int gamesLost;
  private int fastestWon;
  private List<String> wordsEncountered;

  public User(String name) {
    this.name = name;
    this.gamesWon = 0;
    this.gamesLost = 0;
    this.fastestWon = 61;
    this.wordsEncountered = new ArrayList<String>();
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

  public List<String> getWordsEncountered() {
    return wordsEncountered;
  }

  public void newWord(String word) {
    wordsEncountered.add(word);
  }

  public void won() {
    gamesWon++;
  }

  public void lost() {
    gamesLost++;
  }

  public void updateFastestWon(int fasterWon) {
    if (fasterWon < fastestWon) {
      fastestWon = fasterWon;
    }
  }

  public String toString() {
    double winRate = 100.0 * (double) gamesWon / (gamesWon + gamesLost);
    String fastestTime = String.valueOf(fastestWon);
    if (fastestWon > 60) {
      fastestTime = "You have never won! :(";
    }

    if (gamesWon == 0 && gamesLost == 0) {
      winRate = 0.0;
    }

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
        + "\nWords Encountered : \n"
        + wordsEncountered;
  }
}
