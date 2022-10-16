package nz.ac.auckland.se206.controller;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class NewUserController {

  @FXML private TextField newUsername;

  private Scene scene;

  private Parent root;

  /**
   * This method adds a new user if the user name does not exist
   *
   * @param event the event of button click
   * @throws IOException
   */
  @FXML
  private void onNameEntered(ActionEvent event) throws IOException {
    List<User> users = JsonReader.getUsers();
    // If it was an empty one create initial list
    if (users == null) {
      users = new ArrayList<>();
    }
    // Add new item to the list

    List<String> userNames = new ArrayList<String>();

    if (!userNames.contains(newUsername.getText())) {
      users.add(new User(newUsername.getText()));
      // No append replace the whole file
      FileWriter fw = new FileWriter(App.usersFileName, false);
      new GsonBuilder().setPrettyPrinting().create().toJson(users, fw);
      fw.close();
    }

    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/menu.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * This method is called when the "return" button is clicked, the game goes back to main menu
   *
   * @param event the event of button click
   */
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
