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

  /**
   * Constructor for the User class, when made a new user.
   * Default values are shown below, only the name is initialized correspondingly.
   * 
   * @param name Name of the user
   */
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

  /**
   * Getter for name
   * 
   * @return Name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Getter for games won
   * 
   * @return Games won
   */
  public int getGamesWon() {
    return gamesWon;
  }
  
  /**
   * Getter for games lost
   * 
   * @return Games lost
   */
  public int getGamesLost() {
    return gamesLost;
  }

  /**
   * Getter for fastest won
   * 
   * @return Fastest won
   */
  public int getFastestWon() {
    return fastestWon;
  }

  /**
   * Getter for the list of words encountered at specific difficulty
   * 
   * @param diff Difficulty of words
   * @return List of string of words
   */
  public List<String> getWordsEncountered(Difficulty diff) {
    return wordsEncountered.get(diff);
  }

  /**
   * Getter for badges earned
   * 
   * @return List of badges earned
   */
  public List<Integer> getBadgesEarned() {
    return badgesEarned;
  }
  
  /**
   * Getter for current difficulty settings
   * 
   * @return Current difficulty settings
   */
  public List<Difficulty> getCurrentDifficulty() {
    return currentDifficulty;
  }

  /**
   * Getter for the sound status
   * 
   * @return Boolean sound status
   */
  public Boolean getSoundStatus() {
    return soundStatus;
  }

  /**
   * Setter for sound status
   * 
   * @param sound
   */
  public void setSoundStatus(Boolean sound) {
    soundStatus = sound;
  }

  /**
   * Getter for music status
   * 
   * @return Boolean music status
   */
  public Boolean getMusicStatus() {
    return musicStatus;
  }
  
  /**
   * Getter for visit about us
   * 
   * @return Boolean visit about us
   */
  public Boolean getVisitAboutUs() {
	  return visitAboutUs;
  }

  /**
   * Setter for music status
   * 
   * @param music
   */
  public void setMusicStatus(Boolean music) {
    musicStatus = music;
  }

  /**
   * Setter for current difficulty for specific difficulty
   * 
   * @param dif
   */
  public void setCurrentDifficulty(List<Difficulty> dif) {
    currentDifficulty = dif;
  }
  
  /**
   * Setter for boolean if they reached top ten
   * 
   * @param isWon They won
   * @param finalIndex Final index of correct prediction
   */
  public void setTopTen(Boolean isWon, int finalIndex) {
	  if (!isWon && finalIndex < 10) {
		  topTen = true;
	  }
  }
  
  /**
   * Setter for boolean if they played hidden
   * 
   * @param isWordHidden
   */
  public void setPlayHidden(Boolean isWordHidden) {
	 playHidden = isWordHidden;
  }
  
  /**
   * Setter for boolean if they visit about us
   * 
   * @param visit
   */
  public void setVisitAboutUs(Boolean visit) {
	  visitAboutUs = visit;
  }

  /**
   * Add new word at specific difficulty
   * 
   * @param diff
   * @param word
   */
  public void newWord(Difficulty diff, String word) {
    wordsEncountered.get(diff).add(word);
  }

  /**
   * Add new badge it they never got the badge
   * 
   * @param newBadges
   */
  public void newBadge(List<Integer> newBadges) {
    for (int newBadge : newBadges) {
      if (!(badgesEarned.contains(newBadge))) {
        badgesEarned.add(newBadge);
      }
    }
  }

  /**
   * Add number of wins
   */
  public void won() {
    gamesWon++;
  }

  /**
   * Add number of losses
   */
  public void lost() {
    gamesLost++;
  }

  /**
   * Record the result if they won or not
   * 
   * @param isWon
   */
  public void record(boolean isWon) {
    if (previousResults.size() < 10) {
      previousResults.add(isWon);
    } else {
      previousResults.remove(0);
      previousResults.add(isWon);
    }
  }

  /**
   * Update the fastest won
   * 
   * @param fasterWon
   */
  public void updateFastestWon(int fasterWon) {
    if (fasterWon < fastestWon) {
      fastestWon = fasterWon;
    }
  }

  /**
   * Obtain badges with corresponding rules
   */
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
    List<Difficulty> masterList = new ArrayList<Difficulty>(Arrays.asList(Difficulty.X, Difficulty.X, Difficulty.X, Difficulty.X, Difficulty.X));
    if (currentDifficulty.equals(masterList) && previousResults.get(previousResults.size() - 1).equals(true)){
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

  /**
   * Overriden function that changes to string
   */
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
        + (int)winRate
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
