package nz.ac.auckland.se206.controller;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Window;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.game.Game;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.CanvasUtils;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;
import nz.ac.auckland.se206.util.JsonReader;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

  @FXML private Canvas canvas;

  @FXML private Label categoryLabel;

  @FXML private Label timeLabel;

  @FXML private Label timerLabel;

  @FXML private Label usernameLabel;

  @FXML private Button startDrawingButton;

  @FXML private HBox toolBox;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Label topPredictionsLabel;

  @FXML private Label remainingPredictionsLabel;

  @FXML private Label winLostLabel;

  @FXML private Button definitionButton;

  @FXML private HBox endGameBox;

  @FXML private Label categoryContext;

  private Parent root;

  private Game game;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private List<Difficulty> dif;

  // mouse coordinates
  private double currentX;
  private double currentY;
  private String category;
  private Difficulty difficulty;
  private Boolean sound;

  private Boolean music;
  private MediaPlayer playerDrawSFX;
  private MediaPlayer playerEraseSFX;

  private MediaPlayer playerBackgroundMusic;

  private static boolean isHiddenWord;

  private Timer timer = new Timer();

  private String definition;

  private Popup popup = new Popup();

  /**
   * Set the static variable, isHiddenWord, depending on if the user selects hidden word or normal
   * mode
   *
   * @param isWordHidden
   */
  public static void setHiddenWord(boolean isWordHidden) {
    isHiddenWord = isWordHidden;
  }

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws TranslateException {@inheritDoc}
   */
  public void initialize() throws ModelException, IOException, TranslateException {

    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    // reads difficulty, sound status, and music status from user's json
    dif = users.get(userNames.indexOf(MenuController.currentActiveUser)).getCurrentDifficulty();
    sound = users.get(userNames.indexOf(MenuController.currentActiveUser)).getSoundStatus();
    music = users.get(userNames.indexOf(MenuController.currentActiveUser)).getMusicStatus();

    game = new Game(dif);

    category = game.getCategoryToDraw();

    // If the user shoe hidden word mode, play the hidden word mode.
    if (isHiddenWord) {
      System.out.println(category);
      initializeHiddenWord();
    } else {
      // Set category as to itself, not the definition
      categoryLabel.setText(category);
      Thread voiceOver =
          new Thread(
              () -> {
                new TextToSpeech().speak("Please draw: " + category);
              });
      voiceOver.start();
    }

    // Collect difficulty to record in the database
    difficulty = game.getCategoryDifficulty();
    graphic = canvas.getGraphicsContext2D();

    // Initialise drawing sound effect
    try {
      Media drawSFX = new Media(App.class.getResource("/sounds/drawSFX.mp3").toURI().toString());
      playerDrawSFX = new MediaPlayer(drawSFX);
      Media eraseSFX = new Media(App.class.getResource("/sounds/eraserSFX.mp3").toURI().toString());
      playerEraseSFX = new MediaPlayer(eraseSFX);
      Media backgroundMusic =
          new Media(App.class.getResource("/sounds/normalMusic.mp3").toURI().toString());
      playerBackgroundMusic = new MediaPlayer(backgroundMusic);
      playerBackgroundMusic.setVolume(0.1);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    // if user's music status is true, not mute, play background music
    if (music) {
      playerBackgroundMusic.play();
      playerBackgroundMusic.setVolume(0.1);
    }
    onPressPen();
    model = new DoodlePrediction();
    // By loading one prediction before the scene loads, it removes the GUI freezing
    model.getPredictions(CanvasUtils.getCurrentSnapshot(canvas), 10);

    // Set the name of the user as the current user
    usernameLabel.setText(MenuController.currentActiveUser);
    timerLabel.setText(game.getRemainingTime().toString());
  }

  /** This method initializes the hidden word mode if applicable, get definition from web */
  private void initializeHiddenWord() {
    categoryContext.setVisible(false);
    // Get the definition in the background
    Thread getdef =
        new Thread(
            () -> {
              // Get the first definition of the word
              WordInfo wordinfo;
              try {
                wordinfo = DictionaryLookup.searchWordInfo(category);
                definition = wordinfo.getWordEntries().get(0).getDefinitions().get(0);
                Platform.runLater(
                    () -> {
                      categoryLabel.setVisible(false);
                      definitionButton.setVisible(true);
                      definitionButton.setTooltip(new Tooltip(definition));
                    });
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              } catch (WordNotFoundException e) {
                definition = "Lucky! Definition Not Found\r\n" + "Draw " + category;
                e.printStackTrace();
              }
            });

    getdef.start();
  }

  /** This method is called when the user clicks on see definition */
  @FXML
  private void onSeeDefinition() {
    Window stage = canvas.getScene().getWindow();

    // Setting up the scene
    AnchorPane pane = new AnchorPane();
    Label defLabel = new Label(definition);
    defLabel.setFont(new Font("Segoe UI Black", 20));
    pane.setLayoutX(30);
    pane.setLayoutY(80);
    BackgroundFill myBF =
        new BackgroundFill(Color.ORANGE, new CornerRadii(2), new Insets(0.0, 0.0, 0.0, 0.0));
    // then you set to your node
    pane.setBackground(new Background(myBF));
    pane.getChildren().add(defLabel);

    popup.setX(30);
    popup.setY(80);
    popup.getContent().addAll(pane);
    popup.show(stage);
  }

  /** This method is called when the "Pen" button is presses */
  @FXML
  private void onPressPen() {
    penButton.setDisable(true);
    eraserButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/Pencil-icon.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;

          // play drawing sound effect
          if (sound) {
            playerDrawSFX.play();
          }
        });
    canvas.setOnMouseReleased(
        e -> {
          // stop drawing sound effect
          playerDrawSFX.stop();
        });
  }

  /** This method is called when the "Eraser" button is pressed */
  @FXML
  private void onErase() {
    penButton.setDisable(false);
    eraserButton.setDisable(true);
    // Change the cursor to eraser
    URL cursorUrl = App.class.getResource("/images/Eraser-icon.png");
    Image eraserCursor = new Image(cursorUrl.toString());
    canvas.setCursor(
        new ImageCursor(eraserCursor, eraserCursor.getWidth() / 3.5, eraserCursor.getHeight()));

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 9;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.WHITE);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;

          // play drawing sound effect
          if (sound) {
            playerEraseSFX.play();
          }
        });
    canvas.setOnMouseReleased(
        e -> {
          // stop drawing sound effect
          playerEraseSFX.stop();
        });
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    onPressPen();
  }

  /** This method is called when the user click on "Start drawing" button */
  @FXML
  private void onStartDrawing() {
    // Turn on the canvas and change the start button to pen, eraser and clear
    canvas.setDisable(false);
    startDrawingButton.setVisible(false);
    toolBox.setVisible(true);

    // Create a background timer thread that executes the task after 1 second delay for the first
    // time, then executes every second
    timer.scheduleAtFixedRate(
        new TimerTask() {
          public void run() {
            // Update time value and display, remind time left when applicable
            game.decreaseTime();
            game.remindTimeLeft(10);
            Platform.runLater(
                () -> {
                  timerLabel.setText(game.getRemainingTime().toString());
                });

            // When a ending condition of the game is met
            if (game.checkWon()) {
              timer.cancel();
              endGame(true);
              return;
            } else if (game.checkTimeOut()) {
              timer.cancel();
              endGame(false);
              return;
            }

            // Ask the GUI thread to update predictions display
            Platform.runLater(
                () -> {
                  if (CanvasUtils.checkEmptyCanvas(canvas)) {
                    winLostLabel.setText("EMPTY CANVAS!!");
                    topPredictionsLabel.setText("EMPTY CANVAS!!");
                    remainingPredictionsLabel.setText("");
                    game.updatePredictions(null);
                    // Update the predictions value and display
                  } else {
                    try {
                      // Collect all 345 predictions to track the correct answers confidence
                      List<Classification> currentPredictions =
                          model.getPredictions(CanvasUtils.getCurrentSnapshot(canvas), 345);
                      game.updatePredictions(currentPredictions);

                      // Check if the predictions has improved or not
                      winLostLabel.setText(game.checkImprovement());
                      topPredictionsLabel.setText(game.getTopPredictionsDisplay());
                      remainingPredictionsLabel.setText(game.getRemainingPredictionsDisplay());
                    } catch (TranslateException e) {
                      e.printStackTrace();
                    }
                  }
                });
          }
        },
        1000,
        1000);
  }

  /** This is a helper method that is executed when a game has ended */
  private void endGame(boolean isWon) {
    // Setting the buttons
    toolBox.setVisible(false);
    canvas.setDisable(true);
    canvas.setOnMouseDragged(null);
    try {
      // Record result with corresponding inputs
      recordResult(MenuController.currentActiveUser, isWon, 60 - game.getRemainingTime());
    } catch (IOException e) {
      e.printStackTrace();
    }
    endGameBox.setVisible(true);

    // If the mode is hidden word, then show the correct category at top
    if (isHiddenWord) {
      definitionButton.setVisible(false);
      categoryLabel.setVisible(true);
      categoryLabel.setFont(new Font("Segoe UI Black", 15));
      Platform.runLater(() -> categoryLabel.setText("The category was : " + category));
    }

    // Use text to speech to tell us the result
    TextToSpeech textToSpeech = new TextToSpeech();
    if (isWon) {
      Platform.runLater(() -> winLostLabel.setText("YOU WON!!!"));
      timeLabel.setBackground(
          new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
      textToSpeech.speak("Congratulations, you won!");
    } else {
      Platform.runLater(() -> winLostLabel.setText("YOU LOST!!!"));
      timeLabel.setBackground(
          new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
      textToSpeech.speak("Sorry you lost, try again next time.");
    }
  }

  /**
   * This method records the game result and the category played of the current user
   *
   * @param userName the name of the user
   * @param isWon whether the user won
   * @param timeTaken the time taken for the game to end
   * @throws IOException {@inheritDoc}
   */
  private void recordResult(String userName, boolean isWon, int timeTaken) throws IOException {
    List<User> users = JsonReader.getUsers();
    List<String> userNames = JsonReader.getUserNames();

    User user = users.get(userNames.indexOf(userName));

    // Record the game result
    if (isWon) {
      user.won();
      user.updateFastestWon(timeTaken);
    } else {
      user.lost();
    }

    user.record(isWon);

    // Record the category played
    user.newWord(difficulty, category);

    user.setPlayHidden(isHiddenWord);
    user.setTopTen(isWon, game.getCurrentPredictionRank());

    // Update any new badges
    user.obtainBadges();

    FileWriter fw = new FileWriter(App.usersFileName, false);
    new GsonBuilder().setPrettyPrinting().create().toJson(users, fw);
    fw.close();
  }

  /**
   * This method is called when "Play another round" button is pressed
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onPlayNewRound(ActionEvent event) {
    popup.hide();
    timer.cancel();
    playerBackgroundMusic.stop();
    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new canvas FXML file which initializes everything
      Parent root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Save the drawing on a file when a game has ended
   *
   * @param event the event of clicking "Save Your Drawing" button
   * @throws IOException {@inheritDoc}
   */
  @FXML
  private void onSaveDrawing(ActionEvent event) throws IOException {
    popup.hide();
    Window stage = canvas.getScene().getWindow();
    CanvasUtils.saveDrawing(stage, canvas);
  }

  /**
   * When the user presses the return button. Stop the timer (thread) Stop the music and set the
   * scene to main menu.
   *
   * @param event
   */
  @FXML
  private void onReturn(ActionEvent event) {
    popup.hide();
    timer.cancel();
    playerBackgroundMusic.stop();
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
