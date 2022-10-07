package nz.ac.auckland.se206.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;

public class NewUserController {
  @FXML private Button menuButton;
  @FXML TextField newUsername;
  private Scene scene;
  private Parent root;

  public void initialize() {
    Image returnImg = new Image("/images/returnIcon.png");
    ImageView returnImgView = new ImageView(returnImg);
    menuButton.setGraphic(returnImgView);
  }

  @FXML
  public void onNameEntered(ActionEvent event) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    File f = new File("user.json");

    if (!f.exists()) {
      FileWriter fw = new FileWriter("user.json");
      fw.close();
    }
    FileReader fr = new FileReader("user.json");
    List<User> users = gson.fromJson(fr, userListType);
    fr.close();
    // If it was an empty one create initial list
    if (null == users) {
      users = new ArrayList<>();
    }
    // Add new item to the list

    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    if (!userNames.contains(newUsername.getText())) {
      users.add(new User(newUsername.getText()));
      // No append replace the whole file
      FileWriter fw = new FileWriter("user.json", false);
      gson.toJson(users, fw);
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

  @FXML
  public void onReturn(ActionEvent event) {
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
