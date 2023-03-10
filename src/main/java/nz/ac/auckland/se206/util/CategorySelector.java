package nz.ac.auckland.se206.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import nz.ac.auckland.se206.controller.MenuController;
import nz.ac.auckland.se206.game.Category;
import nz.ac.auckland.se206.user.User;

/**
 * This class deals with reading csv files and choosing categories randomly
 *
 * @author Haoran Sun
 */
public class CategorySelector {

  /**
   * This it the enum for different difficulty
   *
   * @author H
   */
  public enum Difficulty {
    E,
    M,
    H,
    X
  }

  private static HashMap<Difficulty, List<String>> categoryMap =
      new HashMap<Difficulty, List<String>>();

  /**
   * This method reads a csv file that contains all the categories and map them according to its
   * difficulty.
   *
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  public static void loadCategories() throws IOException, CsvException, URISyntaxException {
    categoryMap.put(Difficulty.E, new ArrayList<String>());
    categoryMap.put(Difficulty.M, new ArrayList<String>());
    categoryMap.put(Difficulty.H, new ArrayList<String>());

    // Get all the content from the csv file
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());
    try (var fr = new FileReader(fileName, StandardCharsets.UTF_8);
        var reader = new CSVReader(fr)) {
      List<String[]> lines = reader.readAll();
      // line[0] stores the category name, line[1] stores the category difficulty
      // Map each category to the categoryMap according to its difficulty
      for (String[] line : lines) {
        switch (line[1]) {
          case "E":
            categoryMap.get(Difficulty.E).add(line[0]);
            break;

          case "M":
            categoryMap.get(Difficulty.M).add(line[0]);
            break;

          case "H":
            categoryMap.get(Difficulty.H).add(line[0]);
            break;

          default:
            System.out.println("The diffculty is not found!");
            break;
        }
      }
    }
  }

  /**
   * This method randomly chooses a category with the chosen difficulty and return the name as a
   * string (Note: only the categories that the player has not encountered will be chosen)
   *
   * @param dif the difficulty of categories
   * @return a random category with the chosen difficulty
   */
  public static Category getRandomCategory(List<Difficulty> difficulty) {
    String output = null;
    Difficulty currentDifficulty = null;

    try {
      List<String> unplayedCategories = new ArrayList<String>();
      for (Difficulty dif : difficulty) {
        // In the selected difficulties, concatenante the unplayed categories
        unplayedCategories.addAll(getUnplayedCategories(dif));
      }
      // Generating a random index for retrieving element
      int randomIndex = new Random().nextInt(unplayedCategories.size());
      // randomly selecting an element from the concatenated list
      output = unplayedCategories.get(randomIndex);

      for (Difficulty dif : difficulty) {
        if (getUnplayedCategories(dif).contains(output)) {
          // Find the difficulty of the word played for tracking / recording
          currentDifficulty = dif;
          break;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return new Category(output, currentDifficulty);
  }

  /**
   * This method returns all the unplayed, specific difficulty categories by the player
   *
   * @param dif the difficulty of categories
   * @return unplayed specific difficulty categories by the player
   * @throws IOException
   */
  private static List<String> getUnplayedCategories(Difficulty dif) throws IOException {
    List<String> categories = categoryMap.get(dif);

    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    // Remove all the categories that have been played by the current user (except
    // when the user has played all the categories)
    List<String> words =
        users.get(userNames.indexOf(MenuController.currentActiveUser)).getWordsEncountered(dif);
    if (words.size() < categories.size()) {
      categories.removeAll(words);
    }

    return categories;
  }
}
