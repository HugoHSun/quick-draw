package nz.ac.auckland.se206.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
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
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.user.User;
import nz.ac.auckland.se206.util.JsonReader;

public class SandBoxModeController {
  @FXML private Button blueButton;
  @FXML private Button greenButton;
  @FXML private Button orangeButton;
  @FXML private Button purpleButton;
  @FXML private Button pinkButton;
  @FXML private Button brownButton;
  @FXML private Button redButton;
  @FXML private Label colours;
  @FXML private Label usernameLabel;
  @FXML private Canvas canvas;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  private Parent root;

  private GraphicsContext graphic;

  private Boolean sound;

  private Boolean music;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private MediaPlayer playerDrawSFX;
  private MediaPlayer playerEraseSFX;

  private MediaPlayer playerBackgroundMusic;

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
    sound = users.get(userNames.indexOf(MenuController.currentActiveUser)).getSoundStatus();
    music = users.get(userNames.indexOf(MenuController.currentActiveUser)).getMusicStatus();

    // start speech to speak out the word to draw
    Thread voiceOver =
        new Thread(
            () -> {
              new TextToSpeech().speak("Draw whatever you want");
            });
    voiceOver.start();
    graphic = canvas.getGraphicsContext2D();

    onPen();
    usernameLabel.setText(MenuController.currentActiveUser);

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
    }
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
  /**
   * This method is called when back button is pressed to return user to main menu
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onReturn(ActionEvent event) {
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
        	new FileChooser.ExtensionFilter("BMP File", "*.bmp"),
            new FileChooser.ExtensionFilter("PNG File", "*.png"),
            new FileChooser.ExtensionFilter("JPG File", "*.jpeg"));

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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    blueButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    redButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    greenButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    orangeButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    purpleButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    pinkButton.setDisable(true);
    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    brownButton.setDisable(true);

    // Change the cursor icon to eraser
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
    penDisable(
        penButton,
        eraserButton,
        redButton,
        blueButton,
        greenButton,
        orangeButton,
        purpleButton,
        pinkButton,
        brownButton);
    penButton.setDisable(true);

    // Change the cursor icon to eraser
    penCursor = "Pencil-icon.png";
    penColour = Color.BLACK;
    onPen();
  }

  /**
   * This method is called to enable all pens before disabling the current pen in their respective
   * method
   */
  private void penDisable(
      Button penButton,
      Button eraserButton,
      Button redButton,
      Button blueButton,
      Button greenButton,
      Button orangeButton,
      Button purpleButton,
      Button pinkButton,
      Button brownButton) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
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
