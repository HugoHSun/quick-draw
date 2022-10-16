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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;

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
  }

  private void set(List<Difficulty> dif) {
    if (dif.get(0).equals(Difficulty.E)) {
      easyAccuracyButton.setDisable(true);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(false);

      easyAccuracyImage.setOpacity(0.5);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(1.0);
    } else if (dif.get(0).equals(Difficulty.M)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(true);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(false);

      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(0.5);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(1.0);
    } else if (dif.get(0).equals(Difficulty.H)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(true);
      masterAccuracyButton.setDisable(false);

      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(0.5);
      masterAccuracyImage.setOpacity(1.0);
    } else if (dif.get(0).equals(Difficulty.X)) {
      easyAccuracyButton.setDisable(false);
      mediumAccuracyButton.setDisable(false);
      hardAccuracyButton.setDisable(false);
      masterAccuracyButton.setDisable(true);

      easyAccuracyImage.setOpacity(1.0);
      mediumAccuracyImage.setOpacity(1.0);
      hardAccuracyImage.setOpacity(1.0);
      masterAccuracyImage.setOpacity(0.5);
    }

    if (dif.get(1).equals(Difficulty.E)) {
      easyWordButton.setDisable(true);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(false);

      easyWordImage.setOpacity(0.5);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(1.0);
    } else if (dif.get(1).equals(Difficulty.M)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(true);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(false);

      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(0.5);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(1.0);
    } else if (dif.get(1).equals(Difficulty.H)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(true);
      masterWordButton.setDisable(false);

      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(0.5);
      masterWordImage.setOpacity(1.0);
    } else if (dif.get(1).equals(Difficulty.X)) {
      easyWordButton.setDisable(false);
      mediumWordButton.setDisable(false);
      hardWordButton.setDisable(false);
      masterWordButton.setDisable(true);

      easyWordImage.setOpacity(1.0);
      mediumWordImage.setOpacity(1.0);
      hardWordImage.setOpacity(1.0);
      masterWordImage.setOpacity(0.5);
    }

    if (dif.get(2).equals(Difficulty.E)) {
      easyTimeButton.setDisable(true);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(false);

      easyTimeImage.setOpacity(0.5);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(1.0);
    } else if (dif.get(2).equals(Difficulty.M)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(true);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(false);

      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(0.5);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(1.0);
    } else if (dif.get(2).equals(Difficulty.H)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(true);
      masterTimeButton.setDisable(false);

      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(0.5);
      masterTimeImage.setOpacity(1.0);
    } else if (dif.get(2).equals(Difficulty.X)) {
      easyTimeButton.setDisable(false);
      mediumTimeButton.setDisable(false);
      hardTimeButton.setDisable(false);
      masterTimeButton.setDisable(true);

      easyTimeImage.setOpacity(1.0);
      mediumTimeImage.setOpacity(1.0);
      hardTimeImage.setOpacity(1.0);
      masterTimeImage.setOpacity(0.5);
    }

    if (dif.get(3).equals(Difficulty.E)) {
      easyConfidenceButton.setDisable(true);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(false);

      easyConfidenceImage.setOpacity(0.5);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(1.0);
    } else if (dif.get(3).equals(Difficulty.M)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(true);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(false);

      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(0.5);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(1.0);
    } else if (dif.get(3).equals(Difficulty.H)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(true);
      masterConfidenceButton.setDisable(false);

      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(0.5);
      masterConfidenceImage.setOpacity(1.0);
    } else if (dif.get(3).equals(Difficulty.X)) {
      easyConfidenceButton.setDisable(false);
      mediumConfidenceButton.setDisable(false);
      hardConfidenceButton.setDisable(false);
      masterConfidenceButton.setDisable(true);

      easyConfidenceImage.setOpacity(1.0);
      mediumConfidenceImage.setOpacity(1.0);
      hardConfidenceImage.setOpacity(1.0);
      masterConfidenceImage.setOpacity(0.5);
    }

    if (dif.get(4).equals(Difficulty.E)) {
      easyVisibilityButton.setDisable(true);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(false);

      easyVisibilityImage.setOpacity(0.5);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(1.0);
    } else if (dif.get(4).equals(Difficulty.M)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(true);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(false);

      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(0.5);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(1.0);
    } else if (dif.get(4).equals(Difficulty.H)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(true);
      masterVisibilityButton.setDisable(false);

      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(0.5);
      masterVisibilityImage.setOpacity(1.0);
    } else if (dif.get(4).equals(Difficulty.X)) {
      easyVisibilityButton.setDisable(false);
      mediumVisibilityButton.setDisable(false);
      hardVisibilityButton.setDisable(false);
      masterVisibilityButton.setDisable(true);

      easyVisibilityImage.setOpacity(1.0);
      mediumVisibilityImage.setOpacity(1.0);
      hardVisibilityImage.setOpacity(1.0);
      masterVisibilityImage.setOpacity(0.5);
    }
  }

  @FXML
  private void onSound(ActionEvent event) {}

  @FXML
  private void onMusic(ActionEvent event) {}

  @FXML
  private void onReturn(ActionEvent event) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }
    users.get(userNames.indexOf(MenuController.currentActiveUser)).setCurrentDifficulty(dif);

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
  private void onEasyAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.E);
    set(dif);
  }

  @FXML
  private void onMediumAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.M);
    set(dif);
  }

  @FXML
  private void onHardAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.H);
    set(dif);
  }

  @FXML
  private void onMasterAccuracyButton(ActionEvent event) {
    dif.set(0, Difficulty.X);
    set(dif);
  }

  @FXML
  private void onEasyWordButton(ActionEvent event) {
    dif.set(1, Difficulty.E);
    set(dif);
  }

  @FXML
  private void onMediumWordButton(ActionEvent event) {
    dif.set(1, Difficulty.M);
    set(dif);
  }

  @FXML
  private void onHardWordButton(ActionEvent event) {
    dif.set(1, Difficulty.H);
    set(dif);
  }

  @FXML
  private void onMasterWordButton(ActionEvent event) {
    dif.set(1, Difficulty.X);
    set(dif);
  }

  @FXML
  private void onEasyTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.E);
    set(dif);
  }

  @FXML
  private void onMediumTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.M);
    set(dif);
  }

  @FXML
  private void onHardTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.H);
    set(dif);
  }

  @FXML
  private void onMasterTimeButton(ActionEvent event) {
    dif.set(2, Difficulty.X);
    set(dif);
  }

  @FXML
  private void onEasyConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.E);
    set(dif);
  }

  @FXML
  private void onMediumConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.M);
    set(dif);
  }

  @FXML
  private void onHardConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.H);
    set(dif);
  }

  @FXML
  private void onMasterConfidenceButton(ActionEvent event) {
    dif.set(3, Difficulty.X);
    set(dif);
  }

  @FXML
  private void onEasyVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.E);
    set(dif);
  }

  @FXML
  private void onMediumVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.M);
    set(dif);
  }

  @FXML
  private void onHardVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.H);
    set(dif);
  }

  @FXML
  private void onMasterVisibilityButton(ActionEvent event) {
    dif.set(4, Difficulty.X);
    set(dif);
  }
}
