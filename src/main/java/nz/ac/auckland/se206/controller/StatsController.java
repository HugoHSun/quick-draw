package nz.ac.auckland.se206.controller;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class StatsController {
  @FXML private Label statLabel;
  private Parent root;

  /**
   * When user enters stats page, initialize by going through the user data
   * 
   * @throws IOException
   */
  public void initialize() throws IOException {
    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    statLabel.setText(users.get(userNames.indexOf(MenuController.currentActiveUser)).toString());
  }

  /**
   * When user presses the return button, return to the main menu
   * 
   * @param event
   */
  @FXML
  private void onReturn(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/menu.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }
}
