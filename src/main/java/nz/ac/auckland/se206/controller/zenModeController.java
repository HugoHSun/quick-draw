package nz.ac.auckland.se206.controller;

import static nz.ac.auckland.se206.controller.MenuController.currentActiveUser;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.game.Game;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.user.User;

public class zenModeController {
  @FXML private Button blueButton;
  @FXML private Button greenButton;
  @FXML private Button orangeButton;
  @FXML private Button purpleButton;
  @FXML private Button pinkButton;
  @FXML private Button brownButton;
  @FXML private Button redButton;
  @FXML private Label colours;
  @FXML private Label currentUser;
  @FXML private Canvas canvas;

  @FXML private Label categoryLabel;

  @FXML private Button startDrawingButton;

  @FXML private HBox toolBox;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Label topPredictionsLabel;

  @FXML private Label remainingPredictionsLabel;

  @FXML private Label winLostLabel;

  @FXML private HBox endGameBox;

  private Parent root;

  private Game game;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  // mouse coordinates
  private double currentX;
  private double currentY;
  private String category;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws TranslateException
   */
  public void initialize() throws ModelException, IOException {
    // Initialize a game instance with 60 seconds and easy difficulty
    game = new Game(60, CategorySelector.Difficulty.E);
    category = game.getCategoryToDraw();
    categoryLabel.setText(category);
    currentUser.setText(currentActiveUser);
    Thread voiceOver =
        new Thread(
            () -> {
              new TextToSpeech().speak("Please draw: " + category);
            });
    voiceOver.start();

    graphic = canvas.getGraphicsContext2D();
    onPen();
    colours.setVisible(false);
    endGameBox.setVisible(true);
    model = new DoodlePrediction();
  }

  /** This method is called when the "Pen" button is presses */
  @FXML
  private void onPen() {
    penButton.setDisable(true);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
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
        });
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    onPen();
  }

  /** This method is called when the user click on "Start drawing" button */
  @FXML
  private void onStartDrawing() throws TranslateException {
    // Turn on the canvas and change the start button to pen, eraser and clear
    canvas.setDisable(false);
    startDrawingButton.setVisible(false);
    toolBox.setVisible(true);
    colours.setVisible(true);

    // Create a background timer thread that executes the task after 1 second delay for the first
    // time, then executes every second
    Timer timer = new Timer();
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
   * This method records the game result and the category played of the current user
   *
   * @param userName the name of the user
   * @throws IOException
   */
  private void recordResult(String userName) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader(App.usersFileName);
    List<User> users = gson.fromJson(fr, userListType);
    fr.close();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    User user = users.get(userNames.indexOf(userName));
    // Record the category played
    user.newWord(category);

    // Update any new badges
    user.obtainBadges();

    FileWriter fw = new FileWriter(App.usersFileName, false);
    gson.toJson(users, fw);
    fw.close();
  }

  /**
   * This method is called when "Play another round" button is pressed
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onPlayNewRound(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new canvas FXML file which initializes everything
      Parent root = new FXMLLoader(App.class.getResource("/fxml/zenMode.fxml")).load();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
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

  @FXML
  private void onBlue(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(true);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/bluePen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#00B5FF"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onRed(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(true);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/redPen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#FF0000"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onGreen(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(true);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/greenPen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#0FDD00"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onOrange(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(true);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/orangePen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#FF7A00"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onPurple(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(true);
    pinkButton.setDisable(false);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/purplePen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#8E00FF"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onPink(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(true);
    brownButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/pinkPen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#FF00C9"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onBrown(ActionEvent actionEvent) {
    penButton.setDisable(false);
    eraserButton.setDisable(false);
    redButton.setDisable(false);
    blueButton.setDisable(false);
    greenButton.setDisable(false);
    orangeButton.setDisable(false);
    purpleButton.setDisable(false);
    pinkButton.setDisable(false);
    brownButton.setDisable(true);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/brownPen.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.web("#894800"));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }
}
