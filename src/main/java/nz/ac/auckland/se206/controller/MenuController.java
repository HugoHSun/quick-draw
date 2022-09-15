package nz.ac.auckland.se206.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import com.opencsv.exceptions.CsvException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.UserManager;

/**
 * This is the controller for menu, more features are to be added
 *
 * @author Haoran Sun
 */
public class MenuController {

  private Scene scene;
  private Parent root;
  @FXML private ChoiceBox<String> userChoiceBox;
  @FXML private Label currentUser;
  
  public void initialize() throws URISyntaxException, IOException, CsvException {
//	  UserManager users = new UserManager();
	  String user123[] = {"Samuel", "Chan", "Yoo","That's me"};
	  userChoiceBox.getItems().addAll(user123);
	  userChoiceBox.setValue(user123[0]);
	  currentUser.setText(user123[0]);
	  userChoiceBox.setOnAction(this::setUserLabel);
  }

  @FXML
  private void onStartNewGame(ActionEvent event) {
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
  
  @FXML
  private void onSwitchUser(ActionEvent event) {
	  System.out.println("Hi");
  }
  
  public void setUserLabel(ActionEvent event) {
	  String current = userChoiceBox.getValue();
	  currentUser.setText(current);
  }
  
 }
