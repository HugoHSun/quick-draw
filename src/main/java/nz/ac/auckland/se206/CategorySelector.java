package nz.ac.auckland.se206;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class deals with reading csv files and choosing categories randomly
 *
 * @author Haoran Sun
 */
public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H;
  }

  /**
   * This method randomly chooses a category with the chosen difficulty and return the name as a
   * string
   *
   * @param dif the difficulty of categories
   * @return a random category with the chose difficulty
   */
  public String getRandomCategory(Difficulty dif) {
    String output = null;

    try {
      // Generating a random index for retrieving element
      List<String> categories = getCategories(dif);
      int randomIndex = new Random().nextInt(categories.size());
      output = categories.get(randomIndex);
    } catch (IOException | CsvException | URISyntaxException e) {
      e.printStackTrace();
    }

    return output;
  }

  /**
   * This is a helper method that reads a csv file that contains all the categories and its
   * corresponding difficulty. Then returns all the categories with the chosen difficulty.
   *
   * @param dif the difficulty of categories
   * @return an ArrayList of Strings that contains all the categories of the chosen difficulty
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  private List<String> getCategories(Difficulty dif)
      throws IOException, CsvException, URISyntaxException {
    String diffculty = dif.toString();
    List<String> categories = new ArrayList<String>();

    // Read all the content in the file
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());
    try (var fr = new FileReader(fileName, StandardCharsets.UTF_8);
        var reader = new CSVReader(fr)) {
      List<String[]> lines = reader.readAll();
      // Add categories with the chosen difficulty to the output
      for (String[] line : lines) {
        if (line[1].equals(diffculty)) {
          categories.add(line[0]);
        }
      }
    }

    return categories;
  }
}
