package nz.ac.auckland.se206.dict;

import java.util.List;

public class WordEntry {

  private String partOfSpeech;
  private List<String> definitions;

  /**
   * This the constructor of WordEntry which defines the word entry's part of speech and definitions
   *
   * @param partOfSpeech the part of speech of the word
   * @param definitions the definitions of the word
   */
  public WordEntry(String partOfSpeech, List<String> definitions) {
    this.partOfSpeech = partOfSpeech;
    this.definitions = definitions;
  }

  /**
   * This is the getter method for the word entry's part of speech
   *
   * @return the word's part of speech
   */
  public String getPartOfSpeech() {
    return partOfSpeech;
  }

  /**
   * This is the getter method for the word's definitions associated with the part of speech
   *
   * @return the word's definitions
   */
  public List<String> getDefinitions() {
    return definitions;
  }
}
