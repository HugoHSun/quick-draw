package nz.ac.auckland.se206.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.exceptions.CsvException;
import java.io.File;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;

/**
 * This is the controller for menu, more features are to be added
 *
 * @author Haoran Sun
 */
public class MenuController {

  public static String currentlyActiveUser = null;

  private Scene scene;
  private Parent root;
  private List<User> users;
  private List<String> userNames;

  @FXML private Button addUserButton;
  @FXML private Button removeUserButton;
  @FXML private Button statsButton;
  @FXML private Button startGameButton;

  @FXML private HBox createUserMessage;

  @FXML private HBox selectUserMessage;

  @FXML private HBox welcomeBackMessage;

  @FXML private HBox editUserBox;

  @FXML private VBox changeUserBox;

  @FXML private ChoiceBox<String> userChoiceBox;
  @FXML private Label currentUser;

  public void initialize() throws URISyntaxException, IOException, CsvException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    File f = new File("user.json");
    if (!f.exists()) {
      FileWriter fw = new FileWriter("user.json");
      fw.write("[]");
      fw.close();
    }
    FileReader fr = new FileReader("user.json");
    users = gson.fromJson(fr, userListType);
    fr.close();

    userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    // Change the menu layout when there is no created user
    if (userNames.isEmpty()) {
      selectUserMessage.setVisible(false);
      welcomeBackMessage.setVisible(false);
      editUserBox.setVisible(false);
      createUserMessage.setVisible(true);
    } else {
      changeUserBox.setVisible(false);
      userChoiceBox.getItems().addAll(userNames);
      userChoiceBox.setValue(null);
      currentUser.setText(null);
      userChoiceBox.setOnAction(this::setUserLabel);
    }
  }

  @FXML
  private void onStartNewGame(ActionEvent event) {
    // Get the current scene
    if (userChoiceBox.getValue() == null) {
      return;
    }

    scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

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

  @FXML
  private void onDeleteUser(ActionEvent event) throws IOException {
    if (userNames.size() == 0) {
      return;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    users.remove(userNames.indexOf(currentUser.getText()));
    userNames.remove(currentUser.getText());

    FileWriter fw = new FileWriter("user.json", false);
    gson.toJson(users, fw);
    fw.close();
    userChoiceBox.getItems().clear();
    userChoiceBox.getItems().addAll(userNames);
    userChoiceBox.setValue(userNames.get(0));
  }

  public void setUserLabel(ActionEvent event) {
    String currentUserName = userChoiceBox.getValue();
    currentUser.setText(currentUserName);
    selectUserMessage.setVisible(false);
    createUserMessage.setVisible(false);
    editUserBox.setVisible(true);
    changeUserBox.setVisible(true);
    welcomeBackMessage.setVisible(true);
    currentlyActiveUser = currentUser.getText();
  }

  @FXML
  private void onStatistics(ActionEvent event) throws IOException {
    if (userChoiceBox.getValue() == null) {
      currentUser.setText("You must choose your profile!");
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
