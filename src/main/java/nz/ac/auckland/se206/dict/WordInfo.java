package nz.ac.auckland.se206.dict;

import java.util.List;

public class WordInfo {

  private String word;
  private List<WordEntry> entries;

  /**
   * This is the constructor for WordInfo which defines the word and its corresponding entries
   *
   * @param word the word with information
   * @param entries the information associated with the word
   */
  WordInfo(String word, List<WordEntry> entries) {
    this.word = word;
    this.entries = entries;
  }

  /**
   * This is the getter method for getting the word of this WordInfo instance
   *
   * @return the word
   */
  public String getWord() {
    return word;
  }

  /**
   * This is the getter method to get all the entries associated with the word
   *
   * @return the entries associated with this word
   */
  public List<WordEntry> getWordEntries() {
    return entries;
  }

  /**
   * This is the getter method to get the number of entries associated with the word
   *
   * @return the number of entries associated with this word
   */
  public int getNumberOfEntries() {
    return entries.size();
  }
}
