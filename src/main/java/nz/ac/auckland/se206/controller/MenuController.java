package nz.ac.auckland.se206.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;

/**
 * This is the controller for main menu
 *
 * @author Haoran Sun
 */
public class MenuController {

  public static String currentActiveUser = null;

  private Scene scene;

  private Parent root;

  private List<User> users;

  private List<String> userNames;

  @FXML private Label currentUserLabel;

  @FXML private Button deleteUserButton;

  @FXML private Label createUserMessage;

  @FXML private HBox selectUserMessage;

  @FXML private Label welcomeBackMessage;

  @FXML private HBox editUserBox;

  @FXML private VBox changeUserBox;

  @FXML private ComboBox<String> userComboBox;

  /**
   * This method runs after the FXML file is loaded
   *
   * @throws URISyntaxException
   * @throws IOException
   * @throws CsvException
   */
  public void initialize() throws URISyntaxException, IOException, CsvException {
    // Get all users from the users json file by gson library
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader(App.usersFileName);
    users = gson.fromJson(fr, userListType);
    fr.close();
    userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    // Menu layout when there is no created user
    if (users.isEmpty()) {
      // Update the displayed message
      createUserMessage.setVisible(true);
      selectUserMessage.setVisible(false);
      welcomeBackMessage.setVisible(false);

      editUserBox.setVisible(false);
      // Menu layout when there is created users
    } else {
      deleteUserButton.setDisable(true);
      editUserBox.setVisible(true);
      changeUserBox.setVisible(false);
      userComboBox.getItems().addAll(userNames);
    }
  }

  /**
   * This method is called when the combo box of users changes value
   *
   * @param event the event of the combo box changing value
   */
  @FXML
  private void onUserComboBox(ActionEvent event) {
    currentActiveUser = userComboBox.getValue();
    currentUserLabel.setText(currentActiveUser);
    // Move the combo box to the top right corner
    if (!changeUserBox.getChildren().contains(userComboBox)) {
      changeUserBox.getChildren().add(userComboBox);
    }
    changeUserBox.setVisible(true);

    // Update the displayed message
    createUserMessage.setVisible(false);
    selectUserMessage.setVisible(false);
    welcomeBackMessage.setVisible(true);

    // When no user is chosen, the delete current user button should be disabled
    if (currentActiveUser == null) {
      deleteUserButton.setDisable(true);
    } else {
      deleteUserButton.setDisable(false);
    }
  }

  /**
   * This method is called when the user clicks on "Normal Mode" button, which starts a regular game
   *
   * @param event the event of clicking this button
   */
  @FXML
  private void onStartNormalGame(ActionEvent event) {
    // When no user is selected, a game cannot start
    if (currentActiveUser == null) {
      Alert noChosenUser = new Alert(AlertType.INFORMATION);
      noChosenUser.setHeaderText("You need to chose an user to start playing!");
      noChosenUser.show();
      return;
    }

    // Get the current scene
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * This method is called when the user clicks on "Hidden-Word Mode" button, which starts a game
   * with the word to draw hidden
   *
   * @param event the event of clicking this button
   */
  @FXML
  private void onStartHiddenWordGame(ActionEvent event) {
    // When no user is selected
    if (currentActiveUser == null) {
      Alert noChosenUser = new Alert(AlertType.INFORMATION);
      noChosenUser.setHeaderText("You need to chose an user to start playing!");
      noChosenUser.show();
      return;
    }

    // Get the current scene
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * This method is called when the user clicks on "Zen Mode" button, which starts a game that has
   * no time limit and no win or loss
   *
   * @param event the event of clicking this button
   */
  @FXML
  private void onStartZenGame(ActionEvent event) {
    // When no user is selected
    if (currentActiveUser == null) {
      Alert noChosenUser = new Alert(AlertType.INFORMATION);
      noChosenUser.setHeaderText("You need to chose an user to start playing!");
      noChosenUser.show();
      return;
    }

    // Get the current scene
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * This method is called when the user clicks on "Add User" button, which go to the new user UI
   *
   * @param event the event of clicking this button
   * @throws IOException
   */
  @FXML
  private void onAddUser(ActionEvent event) throws IOException {
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/newuser.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * This method is called when the user clicks on "Delete User" button, which deletes the current
   * chosen user
   *
   * @param event the event of clicking this button
   * @throws IOException
   */
  @FXML
  private void onDeleteUser(ActionEvent event) throws IOException {
    // Delete the current user
    int userIndex = userNames.indexOf(currentActiveUser);
    users.remove(userIndex);
    userNames.remove(currentActiveUser);
    currentActiveUser = null;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileWriter fw = new FileWriter(App.usersFileName, false);
    gson.toJson(users, fw);
    fw.close();

    // When the last user is deleted
    if (users.isEmpty()) {
      // Update the displayed message
      createUserMessage.setVisible(true);
      selectUserMessage.setVisible(false);
      welcomeBackMessage.setVisible(false);

      editUserBox.setVisible(false);
      // When there is at least one user left
    } else {
      userComboBox.getItems().clear();
      userComboBox.getItems().addAll(userNames);

      // Update the displayed message
      createUserMessage.setVisible(false);
      selectUserMessage.setVisible(true);
      welcomeBackMessage.setVisible(false);

      selectUserMessage.getChildren().add(userComboBox);
      changeUserBox.setVisible(false);
    }
  }

  /**
   * This method is called when "Statistics" button is clicked, which goes to the statistics page
   * for the current chosen user
   *
   * @param event the event of clicking this button
   * @throws IOException
   */
  @FXML
  private void onStatistics(ActionEvent event) throws IOException {
    if (userComboBox.getValue() == null) {
      Alert noChosenUser = new Alert(AlertType.INFORMATION);
      noChosenUser.setHeaderText("You must choose a profile!");
      noChosenUser.show();
      return;
    }
    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/stats.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }
}
