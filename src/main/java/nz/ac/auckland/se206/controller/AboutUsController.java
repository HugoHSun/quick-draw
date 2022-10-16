package nz.ac.auckland.se206.controller;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class AboutUsController {
  private Scene scene;

  private Parent root;

  /**
   * Initializes the JavaFX scene. If the user (if any) has not yet got the badge for entering the
   * About Us page, give the badge.
   *
   * @throws IOException
   */
  @FXML
  private void initialize() throws IOException {
    // The user can access about us without logging in
    if (MenuController.currentActiveUser != null) {
      List<User> users = JsonReader.getUsers();
      List<String> userNames = JsonReader.getUserNames();
      User user = users.get(userNames.indexOf(MenuController.currentActiveUser));
      // If the user never visited about us
      if (!user.getVisitAboutUs()) {
        // Set the boolean to true
        user.setVisitAboutUs(true);
        user.obtainBadges();

        // Rewrite on the json file
        FileWriter fw = new FileWriter(App.usersFileName, false);
        new GsonBuilder().setPrettyPrinting().create().toJson(users, fw);
        fw.close();
      }
    }
  }

  /** When clicked the return button it loads the main menu */
  @FXML
  private void onReturn(ActionEvent event) {
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/menu.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }
}
