package nz.ac.auckland.se206;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import nz.ac.auckland.se206.controller.MenuController;
import nz.ac.auckland.se206.user.User;

/**
 * This class deals with reading csv files and choosing categories randomly
 *
 * @author Haoran Sun
 */
public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H
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
  protected static void loadCategories() throws IOException, CsvException, URISyntaxException {
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
  public static String getRandomCategory(Difficulty dif) {
    String output = null;

    try {
      List<String> unplayedCategories = getUnplayedCategories(dif);
      // Generating a random index for retrieving element
      int randomIndex = new Random().nextInt(unplayedCategories.size());
      output = unplayedCategories.get(randomIndex);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
  }

  /**
   * This method returns all the unplayed, specific difficulty categories by the player
   *
   * @param dif the difficulty of categories
   * @return unplayed, specific difficulty categories by the player
   * @throws IOException
   */
  private static List<String> getUnplayedCategories(Difficulty dif) throws IOException {
    List<String> categories = categoryMap.get(dif);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader("user.json");
    List<User> users = gson.fromJson(fr, userListType);
    fr.close();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    // Remove all the categories that have been played by the current user (except
    // when the user has played all the categories)
    List<String> words =
        users.get(userNames.indexOf(MenuController.currentlyActiveUser)).getWordsEncountered();
    if (words.size() < categories.size()) {
      categories.removeAll(words);
    }

    return categories;
  }
}
