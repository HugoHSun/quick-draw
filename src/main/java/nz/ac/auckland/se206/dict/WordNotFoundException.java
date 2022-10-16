package nz.ac.auckland.se206.dict;

public class WordNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  private String word;
  private String subMessage;

  /**
   * This the constructor for WordNotFoundException which defines the word and corresponding error
   * message
   *
   * @param word the word that generated this exception
   * @param message the error message
   * @param subMessage the sub error message
   */
  public WordNotFoundException(String word, String message, String subMessage) {
    super(message);
    this.word = word;
    this.subMessage = subMessage;
  }

  /**
   * This is the getter method to get the word that generated this exception
   *
   * @return word that generated this exception
   */
  public String getWord() {
    return word;
  }

  /**
   * This is the getter method to get the sub error message of this exception
   *
   * @return the sub error message
   */
  public String getSubMessage() {
    return subMessage;
  }
}
