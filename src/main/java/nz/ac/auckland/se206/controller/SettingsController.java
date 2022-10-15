package nz.ac.auckland.se206.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CategorySelector.Difficulty;
import nz.ac.auckland.se206.user.User;

public class SettingsController {
  @FXML private ToggleButton soundButton;
  @FXML private ToggleButton musicButton;

  @FXML private ImageView easyImage;
  @FXML private ImageView mediumImage;
  @FXML private ImageView hardImage;
  @FXML private ImageView masterImage;

  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button masterButton;

  @FXML private Label difficultyLabel;

  private Parent root;

  private Difficulty dif;
  private List<User> users;

  private Boolean sound;
  private Boolean music;

  public void initialize() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader(App.usersFileName);
    users = gson.fromJson(fr, userListType);
    fr.close();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }
    dif = users.get(userNames.indexOf(MenuController.currentActiveUser)).getCurrentDifficulty();
    set(dif);
    sound = users.get(userNames.indexOf(MenuController.currentActiveUser)).getSoundStatus();
    setSoundState(sound);
    music = users.get(userNames.indexOf(MenuController.currentActiveUser)).getMusicStatus();
    setMusicState(music);
  }

  private void setSoundState(Boolean sound) {
    if (!sound) {
      soundButton.setSelected(true);
    }
  }

  private void setMusicState(Boolean music) {
    if (!music) {
      musicButton.setSelected(true);
    }
  }

  private void set(Difficulty dif) {
    if (dif.equals(Difficulty.E)) {
      easyButton.setDisable(true);
      mediumButton.setDisable(false);
      hardButton.setDisable(false);
      masterButton.setDisable(false);

      easyImage.setOpacity(0.5);
      mediumImage.setOpacity(1.0);
      hardImage.setOpacity(1.0);
      masterImage.setOpacity(1.0);

      difficultyLabel.setText("EASY");
    } else if (dif.equals(Difficulty.M)) {
      easyButton.setDisable(false);
      mediumButton.setDisable(true);
      hardButton.setDisable(false);
      masterButton.setDisable(false);

      easyImage.setOpacity(1.0);
      mediumImage.setOpacity(0.5);
      hardImage.setOpacity(1.0);
      masterImage.setOpacity(1.0);

      difficultyLabel.setText("MEDIUM");
    } else if (dif.equals(Difficulty.H)) {
      easyButton.setDisable(false);
      mediumButton.setDisable(false);
      hardButton.setDisable(true);
      masterButton.setDisable(false);

      easyImage.setOpacity(1.0);
      mediumImage.setOpacity(1.0);
      hardImage.setOpacity(0.5);
      masterImage.setOpacity(1.0);

      difficultyLabel.setText("HARD");
    } else {
      easyButton.setDisable(false);
      mediumButton.setDisable(false);
      hardButton.setDisable(false);
      masterButton.setDisable(true);

      easyImage.setOpacity(1.0);
      mediumImage.setOpacity(1.0);
      hardImage.setOpacity(1.0);
      masterImage.setOpacity(0.5);

      difficultyLabel.setText("MASTER");
    }
  }

  @FXML
  private void onSound(ActionEvent event) {
    sound = !soundButton.isSelected();
    setSoundState(sound);
  }

  @FXML
  private void onMusic(ActionEvent event) {
    music = !musicButton.isSelected();
    setMusicState(music);
  }

  @FXML
  private void onReturn(ActionEvent event) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }
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

  @FXML
  private void onEasyButton(ActionEvent event) {
    dif = Difficulty.E;
    set(dif);
  }

  @FXML
  private void onMediumButton(ActionEvent event) {
    dif = Difficulty.M;
    set(dif);
  }

  @FXML
  private void onHardButton(ActionEvent event) {
    dif = Difficulty.H;
    set(dif);
  }

  @FXML
  private void onMasterButton(ActionEvent event) {
    dif = Difficulty.X;
    set(dif);
  }
}
