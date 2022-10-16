package nz.ac.auckland.se206.user;

import java.util.ArrayList;
import java.util.Arrays;
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

  // 5 element arraylist -> Accuracy, Word difficulty, Time, Confidence, Visibility
  private List<Difficulty> currentDifficulty;

  private Boolean soundStatus;

  private Boolean musicStatus;

  private Boolean topTen;
  private Boolean playHidden;
  private Boolean visitAboutUs;

  public User(String name) {
    // Default values
    this.name = name;
    this.gamesWon = 0;
    this.gamesLost = 0;
    this.fastestWon = 61;
    this.soundStatus = true;
    this.musicStatus = true;

    this.wordsEncountered = new HashMap<Difficulty, List<String>>();
    wordsEncountered.put(Difficulty.E, new ArrayList<String>());
    wordsEncountered.put(Difficulty.M, new ArrayList<String>());
    wordsEncountered.put(Difficulty.H, new ArrayList<String>());

    this.badgesEarned = new ArrayList<Integer>();
    this.previousResults = new ArrayList<Boolean>();
    this.currentDifficulty = new ArrayList<Difficulty>();
    // Initialise everything as Easy when first created
    for (int i = 0; i < 5; i++) {
      currentDifficulty.add(Difficulty.E);
    }

    this.topTen = false;
    this.playHidden = false;
    this.visitAboutUs = false;
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

  public List<Difficulty> getCurrentDifficulty() {
    return currentDifficulty;
  }

  public Boolean getSoundStatus() {
    return soundStatus;
  }

  public void setSoundStatus(Boolean sound) {
    soundStatus = sound;
  }

  public Boolean getMusicStatus() {
    return musicStatus;
  }

  public Boolean getVisitAboutUs() {
    return visitAboutUs;
  }

  public void setMusicStatus(Boolean music) {
    musicStatus = music;
  }

  public void setCurrentDifficulty(List<Difficulty> dif) {
    currentDifficulty = dif;
  }

  public void setTopTen(Boolean isWon, int finalIndex) {
    if (!isWon && finalIndex < 10) {
      topTen = true;
    }
  }

  public void setPlayHidden(Boolean isWordHidden) {
    playHidden = isWordHidden;
  }

  public void setVisitAboutUs(Boolean visit) {
    visitAboutUs = visit;
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
    if (this.topTen) {
      newBadges.add(4);
    }

    // Silver badges
    if (this.gamesWon == 10) {
      newBadges.add(5);
    }
    if (this.currentDifficulty.get(1).equals(Difficulty.M)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(6);
    }
    if (this.fastestWon <= 15) {
      newBadges.add(7);
    }
    if (this.currentDifficulty.get(1).equals(Difficulty.H)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(8);
    }
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
    if (this.currentDifficulty.get(1).equals(Difficulty.X)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(11);
    }
    if (this.fastestWon <= 3) {
      newBadges.add(12);
    }
    if (this.playHidden) {
      newBadges.add(13);
    }
    if (previousResults.size() == 10 && !(previousResults.contains(false))) {
      newBadges.add(14);
    }

    // Diamond badges
    double winRate = 100.0 * (double) gamesWon / (gamesWon + gamesLost);
    if ((this.gamesWon + this.gamesLost >= 50) && winRate > 0.9) {
      newBadges.add(15);
    }
    if (this.gamesWon + this.gamesLost >= 100) {
      newBadges.add(16);
    }
    List<Difficulty> masterList =
        new ArrayList<Difficulty>(
            Arrays.asList(Difficulty.X, Difficulty.X, Difficulty.X, Difficulty.X, Difficulty.X));
    if (currentDifficulty.equals(masterList)
        && previousResults.get(previousResults.size() - 1).equals(true)) {
      newBadges.add(17);
    }
    if (visitAboutUs) {
      newBadges.add(18);
    }
    if (badgesEarned.size() >= 19) {
      newBadges.add(19);
    }

    newBadge(newBadges);
  }

  public String getWinsStats() {
    return String.valueOf(gamesWon);
  }

  public String getLossesStats() {
    return String.valueOf(gamesLost);
  }

  public String getWinRateStats() {
    double winRate = 100.0 * (double) gamesWon / (gamesWon + gamesLost);
    if (gamesWon == 0 && gamesLost == 0) {
      winRate = 0.0;
    }
    return String.format("%.2f", winRate) + "%";
  }

  public String getFastestTimeStats() {
    String fastestTime = String.valueOf(fastestWon);
    // Default
    if (fastestWon > 60) {
      return "You have never won! :(";
    }
    return fastestTime + " seconds";
  }

  public String getEasyWords() {
    return String.valueOf(wordsEncountered.get(Difficulty.E));
  }

  public String getMediumWords() {
    return String.valueOf(wordsEncountered.get(Difficulty.M));
  }

  public String getHardWords() {
    return String.valueOf(wordsEncountered.get(Difficulty.H));
  }
}
