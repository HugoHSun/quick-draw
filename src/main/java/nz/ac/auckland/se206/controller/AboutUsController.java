package nz.ac.auckland.se206.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

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
  
  @FXML
  private void initialize() throws IOException {
	  if (MenuController.currentActiveUser != null) {
		  List<User> users = JsonReader.getUsers();
		  List<String> userNames = JsonReader.getUserNames();
		  User user = users.get(userNames.indexOf(MenuController.currentActiveUser));
		  if (!user.getVisitAboutUs()) {
			  
			  user.setVisitAboutUs(true);
			  user.obtainBadges();
			  FileWriter fw = new FileWriter(App.usersFileName, false);
			  new GsonBuilder().setPrettyPrinting().create().toJson(users, fw);
			  fw.close();
		  }  	  
	  }
  }

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
