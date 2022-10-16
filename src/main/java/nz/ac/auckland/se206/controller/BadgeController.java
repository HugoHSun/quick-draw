package nz.ac.auckland.se206.controller;

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
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class BadgeController {
  @FXML private Button menuButton;
  private Parent root;

  @FXML private ImageView bronzeOne;
  @FXML private ImageView bronzeTwo;
  @FXML private ImageView bronzeThree;
  @FXML private ImageView bronzeFour;
  @FXML private ImageView bronzeFive;

  @FXML private ImageView silverOne;
  @FXML private ImageView silverTwo;
  @FXML private ImageView silverThree;
  @FXML private ImageView silverFour;
  @FXML private ImageView silverFive;

  @FXML private ImageView goldOne;
  @FXML private ImageView goldTwo;
  @FXML private ImageView goldThree;
  @FXML private ImageView goldFour;
  @FXML private ImageView goldFive;

  @FXML private ImageView diamondOne;
  @FXML private ImageView diamondTwo;
  @FXML private ImageView diamondThree;
  @FXML private ImageView diamondFour;
  @FXML private ImageView diamondFive;

  public void initialize() throws IOException {
    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    List<Integer> badgesIdx =
        users.get(userNames.indexOf(MenuController.currentActiveUser)).getBadgesEarned();
    for (int badgeIdx : badgesIdx) {
      if (badgeIdx == 0) {
        bronzeOne.setOpacity(1.0);
      } else if (badgeIdx == 1) {
        bronzeTwo.setOpacity(1.0);
      } else if (badgeIdx == 2) {
        bronzeThree.setOpacity(1.0);
      } else if (badgeIdx == 3) {
        bronzeFour.setOpacity(1.0);
      } else if (badgeIdx == 4) {
        bronzeFive.setOpacity(1.0);
      } else if (badgeIdx == 5) {
        silverOne.setOpacity(1.0);
      } else if (badgeIdx == 6) {
        silverTwo.setOpacity(1.0);
      } else if (badgeIdx == 7) {
        silverThree.setOpacity(1.0);
      } else if (badgeIdx == 8) {
        silverFour.setOpacity(1.0);
      } else if (badgeIdx == 9) {
        silverFive.setOpacity(1.0);
      } else if (badgeIdx == 10) {
        goldOne.setOpacity(1.0);
      } else if (badgeIdx == 11) {
        goldTwo.setOpacity(1.0);
      } else if (badgeIdx == 12) {
        goldThree.setOpacity(1.0);
      } else if (badgeIdx == 13) {
        goldFour.setOpacity(1.0);
      } else if (badgeIdx == 14) {
        goldFive.setOpacity(1.0);
      } else if (badgeIdx == 15) {
        diamondOne.setOpacity(1.0);
      } else if (badgeIdx == 16) {
        diamondTwo.setOpacity(1.0);
      } else if (badgeIdx == 17) {
        diamondThree.setOpacity(1.0);
      } else if (badgeIdx == 18) {
        diamondFour.setOpacity(1.0);
      } else if (badgeIdx == 19) {
        diamondFive.setOpacity(1.0);
      }
    }
  }

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
  private void onBadge(ActionEvent event) {
    System.out.println("Hi");
  }
}
