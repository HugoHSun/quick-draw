package nz.ac.auckland.se206.controller;

import com.google.gson.Gson;
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
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;
import nz.ac.auckland.se206.util.JsonReader;

public class SettingsController {
  @FXML private ToggleButton soundButton;
  @FXML private ToggleButton musicButton;

  @FXML private ImageView easyAccuracyImage;
  @FXML private ImageView mediumAccuracyImage;
  @FXML private ImageView hardAccuracyImage;
  @FXML private ImageView masterAccuracyImage;

  @FXML private ImageView easyWordImage;
  @FXML private ImageView mediumWordImage;
  @FXML private ImageView hardWordImage;
  @FXML private ImageView masterWordImage;

  @FXML private ImageView easyTimeImage;
  @FXML private ImageView mediumTimeImage;
  @FXML private ImageView hardTimeImage;
  @FXML private ImageView masterTimeImage;

  @FXML private ImageView easyConfidenceImage;
  @FXML private ImageView mediumConfidenceImage;
  @FXML private ImageView hardConfidenceImage;
  @FXML private ImageView masterConfidenceImage;

  @FXML private ImageView easyVisibilityImage;
  @FXML private ImageView mediumVisibilityImage;
  @FXML private ImageView hardVisibilityImage;
  @FXML private ImageView masterVisibilityImage;

  @FXML private Button easyAccuracyButton;
  @FXML private Button mediumAccuracyButton;
  @FXML private Button hardAccuracyButton;
  @FXML private Button masterAccuracyButton;

  @FXML private Button easyWordButton;
  @FXML private Button mediumWordButton;
  @FXML private Button hardWordButton;
  @FXML private Button masterWordButton;

  @FXML private Button easyTimeButton;
  @FXML private Button mediumTimeButton;
  @FXML private Button hardTimeButton;
  @FXML private Button masterTimeButton;

  @FXML private Button easyConfidenceButton;
  @FXML private Button mediumConfidenceButton;
  @FXML private Button hardConfidenceButton;
  @FXML private Button masterConfidenceButton;

  @FXML private Button easyVisibilityButton;
  @FXML private Button mediumVisibilityButton;
  @FXML private Button hardVisibilityButton;
  @FXML private Button masterVisibilityButton;

  private Parent root;

  private List<Difficulty> dif;

  private List<User> users;

  private Boolean sound;
  private Boolean music;

  /**
   * Grab the current settings for difficulty and music from the user data
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    // reads the difficulty, sound status, and music status from users json
    dif = users.get(userNames.indexOf(MenuController.currentActiveUser)).getCurrentDifficulty();
    set(dif);
    sound = users.get(userNames.indexOf(MenuController.currentActiveUser)).getSoundStatus();
    setSoundState(sound);
    music = users.get(userNames.indexOf(MenuController.currentActiveUser)).getMusicStatus();
    setMusicState(music);
  }

  /**
   * This method is called to set the sound state and sound button
   *
   * @param sound true if you want sound
   */
  private void setSoundState(Boolean sound) {
    if (!sound) {
      soundButton.setSelected(true);
    }
  }

  /**
   * This method is called to set the music state and music button
   *
   * @param music true if you want sound
   */
  private void setMusicState(Boolean music) {
    if (!music) {
      musicButton.setSelected(true);
    }
  }

  /**
   * A helper function which checks the list of difficulty consisting of accuracy, word difficulty,
   * time, confidence and visibility
   *
   * @param dif
   */
  private void set(List<Difficulty> dif) {
    // If the accuracy level is Easy
    if (dif.get(0).equals(Difficulty.E)) {
      easyAccuracyButton.setDisable(true);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(false);
      // Set opacity correspondingly
      easyAccuracyImage.setOpacity(0.5);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(1.0);
      // If the accuracy level is Medium
    } else if (dif.get(0).equals(Difficulty.M)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(true);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(false);
      // Set opacity correspondingly
      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(0.5);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(1.0);
      // If the accuracy level is Hard
    } else if (dif.get(0).equals(Difficulty.H)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(true);
      masterAccuracyButton.setDisable(false);
      // Set opacity correspondingly
      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(0.5);
      masterAccuracyImage.setOpacity(1.0);
      // If the accuracy level is Master
    } else if (dif.get(0).equals(Difficulty.X)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(true);
      // Set opacity correspondingly
      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(0.5);
    }

    // If the word difficulty level is Easy
    if (dif.get(1).equals(Difficulty.E)) {
      easyWordButton.setDisable(true);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(false);
      // Set opacity correspondingly
      easyWordImage.setOpacity(0.5);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(1.0);
      // If the word difficulty level is Medium
    } else if (dif.get(1).equals(Difficulty.M)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(true);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(false);
      // Set opacity correspondingly
      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(0.5);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(1.0);
      // If the word difficulty level is Hard
    } else if (dif.get(1).equals(Difficulty.H)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(true);
      masterWordButton.setDisable(false);
      // Set opacity correspondingly
      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(0.5);
      masterWordImage.setOpacity(1.0);
      // If the word difficulty level is Master
    } else if (dif.get(1).equals(Difficulty.X)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(true);
      // Set opacity correspondingly
      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(0.5);
    }

    // If the time level is Easy
    if (dif.get(2).equals(Difficulty.E)) {
      easyTimeButton.setDisable(true);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(false);
      // Set opacity correspondingly
      easyTimeImage.setOpacity(0.5);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(1.0);
      // If the time level is Medium
    } else if (dif.get(2).equals(Difficulty.M)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(true);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(false);
      // Set opacity correspondingly
      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(0.5);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(1.0);
      // If the time level is Hard
    } else if (dif.get(2).equals(Difficulty.H)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(true);
      masterTimeButton.setDisable(false);
      // Set opacity correspondingly
      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(0.5);
      masterTimeImage.setOpacity(1.0);
      // If the time level is Master
    } else if (dif.get(2).equals(Difficulty.X)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(true);
      // Set opacity correspondingly
      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(0.5);
    }

    // If the confidence level is Easy
    if (dif.get(3).equals(Difficulty.E)) {
      easyConfidenceButton.setDisable(true);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(false);
      // Set opacity correspondingly
      easyConfidenceImage.setOpacity(0.5);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(1.0);
      // If the confidence level is Medium
    } else if (dif.get(3).equals(Difficulty.M)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(true);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(false);
      // Set opacity correspondingly
      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(0.5);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(1.0);
      // If the confidence level is Hard
    } else if (dif.get(3).equals(Difficulty.H)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(true);
      masterConfidenceButton.setDisable(false);
      // Set opacity correspondingly
      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(0.5);
      masterConfidenceImage.setOpacity(1.0);
      // If the confidence level is Master
    } else if (dif.get(3).equals(Difficulty.X)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(true);
      // Set opacity correspondingly
      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(0.5);
    }

    // If the visibility level is Easy
    if (dif.get(4).equals(Difficulty.E)) {
      easyVisibilityButton.setDisable(true);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(false);
      // Set opacity correspondingly
      easyVisibilityImage.setOpacity(0.5);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(1.0);
      // If the visibility level is Medium
    } else if (dif.get(4).equals(Difficulty.M)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(true);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(false);
      // Set opacity correspondingly
      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(0.5);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(1.0);
      // If the visibility level is Hard
    } else if (dif.get(4).equals(Difficulty.H)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(true);
      masterVisibilityButton.setDisable(false);
      // Set opacity correspondingly
      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(0.5);
      masterVisibilityImage.setOpacity(1.0);
      // If the visibility level is Master
    } else if (dif.get(4).equals(Difficulty.X)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(true);
      // Set opacity correspondingly
      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(0.5);
    }
  }

  /**
   * This method is called when sound button is pressed and will toggle sound boolean and sound
   * state
   *
   * @param event when sound button is pressed
   */
  @FXML
  private void onPressSound(ActionEvent event) {
    sound = !soundButton.isSelected();
    setSoundState(sound);
  }

  /**
   * This method is called when music button is pressed and will toggle music boolean and music
   * state
   *
   * @param event when music button is pressed
   */
  @FXML
  private void onPressMusic(ActionEvent event) {
    music = !musicButton.isSelected();
    setMusicState(music);
  }

  /**
   * When the user presses the return button, if there was any changes to the settings update to the
   * user data.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void onReturn(ActionEvent event) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }
    // Record the changed settings to the user data
    users.get(userNames.indexOf(MenuController.currentActiveUser)).setCurrentDifficulty(dif);
    users.get(userNames.indexOf(MenuController.currentActiveUser)).setSoundStatus(sound);
    users.get(userNames.indexOf(MenuController.currentActiveUser)).setMusicStatus(music);

    FileWriter fw = new FileWriter(App.usersFileName, false);
    gson.toJson(users, fw);
    fw.close();

    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new parent node
      root = new FXMLLoader(App.class.getResource("/fxml/menu.fxml")).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    scene.setRoot(root);
  }

  /**
   * When user selects level easy for accuracy
   *
   * @param event
   */
  @FXML
  private void onPressEasyAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.E);
    set(dif);
  }

  /**
   * When user selects level medium for accuracy
   *
   * @param event
   */
  @FXML
  private void onPressMediumAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.M);
    set(dif);
  }

  /**
   * When user selects level hard for accuracy
   *
   * @param event
   */
  @FXML
  private void onPressHardAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.H);
    set(dif);
  }

  /**
   * When user selects level master for accuracy
   *
   * @param event
   */
  @FXML
  private void onPressMasterAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.X);
    set(dif);
  }

  /**
   * When user selects level easy for word difficulty
   *
   * @param event
   */
  @FXML
  private void onPressEasyWordButton(ActionEvent event) {
    dif.set(1, Difficulty.E);
    set(dif);
  }

  /**
   * When user selects level medium for word difficulty
   *
   * @param event
   */
  @FXML
  private void onPressMediumWordButton(ActionEvent event) {
    dif.set(1, Difficulty.M);
    set(dif);
  }

  /**
   * When user selects level hard for word difficulty
   *
   * @param event
   */
  @FXML
  private void onPressHardWordButton(ActionEvent event) {
    dif.set(1, Difficulty.H);
    set(dif);
  }

  /**
   * When user selects level master for word difficulty
   *
   * @param event
   */
  @FXML
  private void onPressMasterWordButton(ActionEvent event) {
    dif.set(1, Difficulty.X);
    set(dif);
  }

  /**
   * When user selects level easy for time
   *
   * @param event
   */
  @FXML
  private void onPressEasyTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.E);
    set(dif);
  }

  /**
   * When user selects level medium for time
   *
   * @param event
   */
  @FXML
  private void onPressMediumTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.M);
    set(dif);
  }

  /**
   * When user selects level hard for time
   *
   * @param event
   */
  @FXML
  private void onPressHardTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.H);
    set(dif);
  }
  /**
   * When user selects level master for time
   *
   * @param event
   */
  @FXML
  private void onPressMasterTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.X);
    set(dif);
  }

  /**
   * When user selects level easy for confidence
   *
   * @param event
   */
  @FXML
  private void onPressEasyConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.E);
    set(dif);
  }

  /**
   * When user selects level medium for confidence
   *
   * @param event
   */
  @FXML
  private void onPressMediumConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.M);
    set(dif);
  }

  /**
   * When user selects level hard for confidence
   *
   * @param event
   */
  @FXML
  private void onPressHardConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.H);
    set(dif);
  }

  /**
   * When user selects level master for confidence
   *
   * @param event
   */
  @FXML
  private void onPressMasterConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.X);
    set(dif);
  }

  /**
   * When user selects level easy for visibility
   *
   * @param event
   */
  @FXML
  private void onPressEasyVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.E);
    set(dif);
  }

  /**
   * When user selects level medium for visibility
   *
   * @param event
   */
  @FXML
  private void onPressMediumVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.M);
    set(dif);
  }

  /**
   * When user selects level hard for visibility
   *
   * @param event
   */
  @FXML
  private void onPressHardVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.H);
    set(dif);
  }

  /**
   * When user selects level master for visibility
   *
   * @param event
   */
  @FXML
  private void onPressMasterVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.X);
    set(dif);
  }
}
