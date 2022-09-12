package nz.ac.auckland.se206.controller;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

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

  private GraphicsContext graphic;

  private DoodlePrediction model;

  @FXML private Label categoryLabel;

  @FXML private HBox countdownHorizontalBox;

  @FXML private Label timerLabel;

  @FXML private Button startDrawingButton;

  @FXML private ToolBar toolBox;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Label predictionLabel;

  @FXML private Label winLostLabel;

  @FXML private Button newGameButton;

  @FXML private Button saveDrawingButton;

  private boolean isWon = false;

  // mouse coordinates
  private double currentX;
  private double currentY;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException {
    // Randomly chose a category and update the categoryLabel to display it
    String category = new CategorySelector().getRandomCategory(CategorySelector.Difficulty.E);
    categoryLabel.setText(category);
    Thread voiceOver =
        new Thread(
            () -> {
              new TextToSpeech().speak("Please draw: " + category);
            });
    voiceOver.start();

    graphic = canvas.getGraphicsContext2D();

    // Change the cursor icon to eraser in canvas
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

    model = new DoodlePrediction();
  }

  /** This method is called when the "Pen" button is presses */
  @FXML
  private void onPen() {
    // Setting the buttons
    penButton.setDisable(true);
    eraserButton.setDisable(false);
    // Change the cursor icon to eraser
    URL cursorUrl = App.class.getResource("/images/Pencil-icon.png");
    Image pencilCursor = new Image(cursorUrl.toString());
    canvas.setCursor(new ImageCursor(pencilCursor, 0, pencilCursor.getHeight()));

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
    // Setting the buttons
    penButton.setDisable(false);
    eraserButton.setDisable(true);
    // Change the cursor to eraser
    URL cursorUrl = App.class.getResource("/images/Eraser-icon.png");
    Image eraserCursor = new Image(cursorUrl.toString());
    canvas.setCursor(
        new ImageCursor(eraserCursor, eraserCursor.getWidth() / 3.5, eraserCursor.getHeight()));

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
    // Change to pen after clearing canvas
    onPen();
  }

  /** This method is called when the user click on "Start drawing" button */
  @FXML
  private void onStartDrawing() {
    // Turn on the canvas and change the buttons
    canvas.setDisable(false);
    startDrawingButton.setVisible(false);
    toolBox.setVisible(true);

    // Create a timer thread running in the background that counts down from 60 to
    // 0, and ask GUI thread to update time and predictions display every second
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          // The remaining time in seconds
          private Integer remainingTime = 60;

          // This method will be called every second by the timer thread
          public void run() {
            // Ask the GUI thread to update time and predictions display.
            Platform.runLater(
                () -> {
                  // Time display
                  timerLabel.setText(remainingTime.toString());

                  // Get current predictions and update the display
                  try {
                    List<Classification> currentPredictions =
                        model.getPredictions(getCurrentSnapshot(), 10);
                    String predictionDisplay = getPredictionDisplay(currentPredictions);
                    predictionLabel.setText(predictionDisplay);
                  } catch (TranslateException e) {
                    e.printStackTrace();
                  }
                });

            // Remind the user when there are 10 seconds left
            remindTimeLeft(remainingTime, 10);

            // When a ending condition of the game is met
            if (remainingTime == 0 || isWon) {
              timer.cancel();
              endGame();
            }

            remainingTime--;
          }
        },
        1000,
        1000);
  }

  /**
   * This method is called when "Start a new game" button is pressed
   *
   * @param event the event of clicking the button
   */
  @FXML
  private void onNewGame(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    try {
      // Load a new canvas FXML file which initializes everything
      Parent root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** This method is called when the "Save drawing" button is pressed */
  // TO BE EDITED
  @FXML
  private void onSaveDrawing() {
    // Save the image as default
    try {
      saveCurrentSnapshotOnFile();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   */
  private File saveCurrentSnapshotOnFile() throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File("tmp");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    // We save the image to a file in the tmp folder.
    final File imageToClassify =
        new File(tmpFolder.getName() + "/snapshot" + System.currentTimeMillis() + ".bmp");

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

    return imageToClassify;
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
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
   * This is a helper method that builds a string of the current top 10 predictions, which can be
   * displayed in a label. Note that it also checks whether the player has won (the top 3
   * predictions contain the category to be drawn).
   *
   * @param currentPredictions a list of of type Classification, which is the category of AI
   *     predictions
   * @return a string containing the top 10 predictions
   */
  private String getPredictionDisplay(List<Classification> currentPredictions) {
    // Build the string to display the top ten predictions
    final StringBuilder sb = new StringBuilder();
    int predictionRank = 1;

    for (final Classifications.Classification classification : currentPredictions) {
      // Build the predictions string to be displayed
      sb.append("TOP ")
          .append(predictionRank)
          .append(" : ")
          .append(classification.getClassName().replaceAll("_", " "))
          .append(" : ")
          .append(String.format("%d%%", Math.round(100 * classification.getProbability())))
          .append(System.lineSeparator());

      // When the category to be drawn is in the top 3 predictions
      if (classification.getClassName().replaceAll("_", " ").equals(categoryLabel.getText())
          && predictionRank <= 3) {
        isWon = true;
      }

      predictionRank++;
    }

    return sb.toString();
  }

  /**
   * This is a helper method which reminds user of the remaining time by text to speech
   *
   * @param currentRemainingTime the remaining time in seconds
   * @param reminderTime the time to remind the user
   */
  private void remindTimeLeft(int currentRemainingTime, int reminderTime) {
    if (currentRemainingTime == (reminderTime + 1)) {
      Thread timeReminder =
          new Thread(
              () -> new TextToSpeech().speak(Integer.toString(reminderTime) + " seconds left"));
      timeReminder.start();
    }
  }

  /** This is a helper method that is executed when a game has ended */
  private void endGame() {
    // Setting the buttons
    toolBox.setVisible(false);
    canvas.setDisable(true);
    canvas.setOnMouseDragged(null);
    saveDrawingButton.setDisable(false);
    newGameButton.setDisable(false);

    // Change the background of count down to red
    Platform.runLater(
        () -> {
          countdownHorizontalBox.setBackground(
              new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        });

    TextToSpeech textToSpeech = new TextToSpeech();
    if (isWon) {
      Platform.runLater(() -> winLostLabel.setText("YOU WON!!!"));
      textToSpeech.speak("Congratulations, you won!");
    } else {
      Platform.runLater(() -> winLostLabel.setText("YOU LOST!!!"));
      textToSpeech.speak("Sorry you lost, try again next time.");
    }
  }
}
