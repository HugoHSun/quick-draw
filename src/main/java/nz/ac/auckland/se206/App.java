package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controller.SceneManager;
import nz.ac.auckland.se206.controller.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Menu" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // User cannot resize the window
    stage.setResizable(false);
    stage.setTitle("Quick Draw! - SE206 Edition");

    // Initialize the CategorySelector
    try {
      CategorySelector.loadCategories();
    } catch (IOException | CsvException | URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    SceneManager.addUi(SceneManager.AppUi.MAIN_MENU, loadFxml("menu"));
    final Scene scene = new Scene(SceneManager.getUi(AppUi.MAIN_MENU), 780, 500);

    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    System.exit(0);
  }
}
