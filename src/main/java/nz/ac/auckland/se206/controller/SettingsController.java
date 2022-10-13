package nz.ac.auckland.se206.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import nz.ac.auckland.se206.App;

public class SettingsController {
  @FXML private ToggleButton soundButton;
  @FXML private ToggleButton musicButton;
  private Parent root;

  public void initialize() throws IOException {}

  @FXML
  private void onSound(ActionEvent event) {}

  @FXML
  private void onMusic(ActionEvent event) {}

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
