package nz.ac.auckland.se206.controller;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
import nz.ac.auckland.se206.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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
	  Gson gson = new GsonBuilder().setPrettyPrinting().create();
	  // construct Type that tells Gson about the generic type
	  Type userListType = new TypeToken<List<User>>(){}.getType();
	  FileReader fr = new FileReader("user.json");
	  List<User> users = gson.fromJson(fr, userListType);
	  fr.close();
	  
	  List<String> userNames = new ArrayList<String>();
	  for (User user : users) {
		  userNames.add(user.getName());
	  }
	  
	  userChoiceBox.getItems().addAll(userNames);
	  userChoiceBox.setValue(userNames.get(0));
	  currentUser.setText(userNames.get(0));
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
  
  public void setUserLabel(ActionEvent event) {
	  String current = userChoiceBox.getValue();
	  currentUser.setText(current);
  } 
 }
