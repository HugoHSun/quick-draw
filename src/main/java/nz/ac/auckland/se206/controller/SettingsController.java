package nz.ac.auckland.se206.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseDragEvent;
import nz.ac.auckland.se206.App;

public class SettingsController {
  @FXML private Slider timeSlider;
  @FXML private Slider wordsSlider;
  @FXML private Slider confidenceSlider;
  @FXML Slider accuracySlider;
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

  @FXML
  private void onAccuracySliderChanged(MouseDragEvent event) {
    int sliderValue = (int) accuracySlider.getValue();
    // easy=0, medium=50, hard=100
  }

  @FXML
  private void onTimeSliderChanged(MouseDragEvent event) {
    int sliderValue = (int) timeSlider.getValue();
    // easy=0, medium=50, hard=100, master=150
  }

  @FXML
  private void onWordsSliderChanged(MouseDragEvent event) {
    int sliderValue = (int) wordsSlider.getValue();
    // easy=0, medium=50, hard=100, master=150
  }

  @FXML
  private void onConfidenceSliderChanged(MouseDragEvent event) {
    int sliderValue = (int) confidenceSlider.getValue();
    // easy=0, medium=50, hard=100, master=150
  }
}
