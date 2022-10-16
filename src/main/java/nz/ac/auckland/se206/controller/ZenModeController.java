package nz.ac.auckland.se206.controller;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.game.Game;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.CategorySelector.Difficulty;
import nz.ac.auckland.se206.util.JsonReader;

public class ZenModeController {
  @FXML private Button blackButton;
  @FXML private Button blueButton;
  @FXML private Button greenButton;
  @FXML private Button orangeButton;
  @FXML private Button purpleButton;
  @FXML private Button pinkButton;
  @FXML private Button brownButton;
  @FXML private Button redButton;

  @FXML private Label usernameLabel;

  @FXML private Canvas canvas;

  @FXML private Label categoryLabel;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Label topPredictionsLabel;

  @FXML private Label remainingPredictionsLabel;

  private Parent root;

  private Game game;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private Boolean sound;

  private Boolean music;
  private List<Difficulty> dif;

  // mouse coordinates
  private double currentX;
  private double currentY;
  private String category;

  private MediaPlayer playerDrawSFX;
  private MediaPlayer playerEraseSFX;

  private MediaPlayer playerBackgroundMusic;
  
  private Timer timer = new Timer();

  private Color penColour;
  private String penCursor = null;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws TranslateException
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
    categoryLabel.setText(category);
    usernameLabel.setText(MenuController.currentActiveUser);

    // start speech to speak out the word to draw
    Thread voiceOver =
        new Thread(
            () -> {
              new TextToSpeech().speak("Please draw: " + category);
            });
    voiceOver.start();
    graphic = canvas.getGraphicsContext2D();

    onPen();

    model = new DoodlePrediction();
    // By loading one prediction before the scene loads, it removes the GUI freezing
    model.getPredictions((BufferedImage) getCurrentSnapshot(), 10);

    // Initialise drawing, eraser, and background music sound effect
    try {
      Media drawSFX = new Media(App.class.getResource("/sounds/drawSFX.mp3").toURI().toString());
      playerDrawSFX = new MediaPlayer(drawSFX);
      Media eraseSFX = new Media(App.class.getResource("/sounds/eraserSFX.mp3").toURI().toString());
      playerEraseSFX = new MediaPlayer(eraseSFX);
      Media backgroundMusic =
          new Media(App.class.getResource("/sounds/zenMusic.wav").toURI().toString());
      playerBackgroundMusic = new MediaPlayer(backgroundMusic);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    // if user's music status is true, not mute, play background music
    if (music) {
      playerBackgroundMusic.play();
      playerBackgroundMusic.setVolume(0.1);
    }
    startGame();
  }

  /** This method is called when the "Pen" button is presses */
  @FXML
  private void onPen() {
    penButton.setDisable(true);
    eraserButton.setDisable(false);

    if (penCursor == null) {
      penCursor = "Pencil-icon.png";
    }
    // Change the cursor icon to pen
    changeCursor(penCursor);

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
          graphic.setStroke(penColour);
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
    changeCursor("Eraser-icon.png");

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
    onPen();
  }

  /** This method is called when the user go into Zen mode */
  private void startGame() {
    // Create a background timer thread that executes the task after 1 second delay for the first
    // time, then executes every second

    timer.scheduleAtFixedRate(
        new TimerTask() {
          public void run() {
            // Ask the GUI thread to update predictions display
            Platform.runLater(
                () -> {
                  if (checkEmptyCanvas()) {
                    topPredictionsLabel.setText("EMPTY CANVAS!!");
                    remainingPredictionsLabel.setText("");
                    game.updatePredictions(null);
                    // Update the predictions value and display
                  } else {
                    try {
                      List<Classifications.Classification> currentPredictions =
                          model.getPredictions((BufferedImage) getCurrentSnapshot(), 10);
                      game.updatePredictions(currentPredictions);
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

  /**
   * This method checks whether the canvas is empty and returns a boolean
   *
   * @return true if the canvas is empty, false otherwise
   */
  private boolean checkEmptyCanvas() {
    // Get the current canvas as an image
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    final int width = image.getWidth();
    final int height = image.getHeight();

    // Grab all the pixels
    int[] pixels = new int[width * height];
    PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
    try {
      pg.grabPixels();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Check each pixel to see if they are all white
    for (int pixel : pixels) {
      java.awt.Color color = new java.awt.Color(pixel);
      if (color.getAlpha() == 0 || color.getRGB() != java.awt.Color.WHITE.getRGB()) {
        return false;
      }
    }

    return true;
  }

  /**
   * This method is called when "Change category" button is pressed
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onChangeCategory(ActionEvent event) {
    timer.cancel();
    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new canvas FXML file which initializes everything
      Parent root = new FXMLLoader(App.class.getResource("/fxml/zenMode.fxml")).load();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when back button is pressed to return user to main menu
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onReturn(ActionEvent event) {
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

  /**
   * Save the drawing on a file when a game has ended
   *
   * @param event the event of clicking "Save Your Drawing" button
   * @throws IOException
   */
  @FXML
  private void onSaveDrawing(ActionEvent event) throws IOException {
    // Open a file dialog box
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Your Drawing");

    // You can change the location as you see fit.
    final File tmpFolder = new File("tmp");

    // tmpfolder is the default directory
    fileChooser.setInitialDirectory(tmpFolder);

    // make a tmp folder if it doesn't exist
    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    // Set default name and available file extensions
    fileChooser.setInitialFileName("MyDrawing");
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("img", "*.bmp"),
            new FileChooser.ExtensionFilter("img", "*.png"),
            new FileChooser.ExtensionFilter("img", "*.jpeg"));

    // open file dialog box
    Window stage = canvas.getScene().getWindow();
    File file = fileChooser.showSaveDialog(stage);

    try {
      // Save the image to a file and pop up a message to show if the image is saved
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
      Alert successfulSave = new Alert(Alert.AlertType.INFORMATION);
      successfulSave.setHeaderText("Image successfully saved");
      successfulSave.show();
    } catch (Exception e) {
      Alert unsuccessfulSave = new Alert(Alert.AlertType.ERROR);
      unsuccessfulSave.setHeaderText("Image not saved");
      unsuccessfulSave.show();
    }
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private RenderedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * This method is called to switch to blue coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onBlue(ActionEvent event) {
    enableAllColours();
    blueButton.setDisable(true);

    // Change to blue pen cursor icon and pen colour
    penCursor = "bluePen.png";
    penColour = Color.web("#00B5FF");
    onPen();
  }

  /**
   * This method is called to switch to red coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onRed(ActionEvent event) {
    enableAllColours();
    redButton.setDisable(true);

    // Change to red pen cursor icon and pen colour
    penCursor = "redPen.png";
    penColour = Color.RED;
    onPen();
  }

  /**
   * This method is called to switch to green coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onGreen(ActionEvent event) {
    enableAllColours();
    greenButton.setDisable(true);

    // Change to green pen cursor icon and pen colour
    penCursor = "greenPen.png";
    penColour = Color.web("#0FDD00");
    onPen();
  }

  /**
   * This method is called to switch to orange coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onOrange(ActionEvent event) {
    enableAllColours();
    orangeButton.setDisable(true);

    // Change to orange pen cursor icon and pen colour
    penCursor = "orangePen.png";
    penColour = Color.web("#FF7A00");
    onPen();
  }

  /**
   * This method is called to switch to purple coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onPurple(ActionEvent event) {
    enableAllColours();
    purpleButton.setDisable(true);

    // Change to purple pen cursor icon and pen colour
    penCursor = "purplePen.png";
    penColour = Color.web("#8E00FF");
    onPen();
  }

  /**
   * This method is called to switch to pink coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onPink(ActionEvent event) {
    enableAllColours();
    pinkButton.setDisable(true);

    // Change to pink pen cursor icon and pen colour
    penCursor = "pinkPen.png";
    penColour = Color.web("#FF00C9");
    onPen();
  }

  /**
   * This method is called to switch to brown coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onBrown(ActionEvent event) {
    enableAllColours();
    brownButton.setDisable(true);

    // Change to brown pen cursor icon and pen colour
    penCursor = "brownPen.png";
    penColour = Color.web("#894800");
    onPen();
  }

  /**
   * This method is called to switch to black coloured pen to draw
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onBlack(ActionEvent event) {
    enableAllColours();
    blackButton.setDisable(true);

    // Change to black pen cursor icon and pen colour
    penCursor = "Pencil-icon.png";
    penColour = Color.BLACK;
    onPen();
  }

  /** This method is called to enable all coloured pens before disabling the current coloured pen */
  private void enableAllColours() {
    // All the colours, 8 in total
    blackButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    redButton.setDisable(false);
  }

  /**
   * This is a helper method to change the cursor appearance on canvas
   *
   * @param cursorName the name of the cursor image
   */
  private void changeCursor(String cursorName) {
    URL cursorUrl = App.class.getResource("/images/" + cursorName);
    Image cursorImage = new Image(cursorUrl.toString());
    canvas.setCursor(
        new ImageCursor(cursorImage, cursorImage.getWidth() / 3.5, cursorImage.getHeight()));
  }
}
