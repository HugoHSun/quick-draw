package nz.ac.auckland.se206.controller;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class StatsController {
  @FXML private PieChart winPieChart;
  @FXML private Label statLabel;
  private Parent root;

  private int wins;
  private int losses;

  private Scene scene;

  public StatsController() {}

  public void initialize() throws IOException {
    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    statLabel.setText(users.get(userNames.indexOf(MenuController.currentActiveUser)).toString());
    wins = users.get(userNames.indexOf(MenuController.currentActiveUser)).getGamesWon();
    losses = users.get(userNames.indexOf(MenuController.currentActiveUser)).getGamesLost();

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("wins", wins), new PieChart.Data("losses", losses));
    winPieChart.setData(pieChartData);
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
