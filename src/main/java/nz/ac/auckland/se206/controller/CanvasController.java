package nz.ac.auckland.se206.controller;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
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
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.user.User;

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

  @FXML private HBox countdownHorizontalBox;

  @FXML private Label timerLabel;

  @FXML private Button startDrawingButton;

  @FXML private ToolBar toolBox;

  @FXML private Button penButton;

  @FXML private Button eraserButton;

  @FXML private Label predictionLabel;

  @FXML private Label winLostLabel;

  @FXML private Button newRoundButton;

  @FXML private Button saveDrawingButton;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private boolean isWon = false;

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
   */
  public void initialize() throws ModelException, IOException {
    // Randomly chose an easy category and display it
    category = CategorySelector.getRandomCategory(CategorySelector.Difficulty.E);
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
    onPen();
  }

  /** This method is called when the user click on "Start drawing" button */
  @FXML
  private void onStartDrawing() {
    // Turn on the canvas and change the start button to pen, eraser and clear
    canvas.setDisable(false);
    startDrawingButton.setVisible(false);
    toolBox.setVisible(true);

    // Create a background timer thread that counts down and ask GUI thread to update time and
    // predictions display every second
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          // In seconds
          private Integer remainingTime = 60;

          // First time called after 1 second delay, then called every second
          public void run() {
            remainingTime--;
            // When a ending condition of the game is met
            if (isWon || remainingTime == 0) {
              timer.cancel();
              endGame(remainingTime);
              return;
            }

            // Ask the GUI thread to update time and predictions display.
            Platform.runLater(
                () -> {
                  // Time display
                  timerLabel.setText(remainingTime.toString());

                  // Update current predictions
                  if (checkEmptyCanvas()) {
                    predictionLabel.setText("EMPTY CANVAS!");
                  } else {
                    try {
                      List<Classification> currentPredictions =
                          model.getPredictions(getCurrentSnapshot(), 10);
                      predictionLabel.setText(getPredictionDisplay(currentPredictions));
                    } catch (TranslateException e) {
                      e.printStackTrace();
                    }
                  }
                });

            // Remind the user when there are 10 seconds left
            remindTimeLeft(remainingTime, 10);
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
   * This is a helper method that builds a string of the current top 10 predictions, which can be
   * displayed in a label. Note that it also calls checkWon method to check whether the player has
   * won (the top 3 predictions contain the category to be drawn).
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
      sb.append(classification.getClassName().replaceAll("_", " "))
          .append(" : ")
          .append(String.format("%d%%", Math.round(100 * classification.getProbability())))
          .append(System.lineSeparator());

      checkWon(classification, predictionRank);

      predictionRank++;
    }

    return sb.toString();
  }

  /**
   * This method checks whether the winning condition is met (in the top 3 AI predictions)
   *
   * @param classification the category of prediction
   * @param predictionRank the rank of prediction
   */
  private void checkWon(Classification classification, int predictionRank) {
    // The player wins if top 3 AI predictions include the category
    if (predictionRank <= 3
        && classification.getClassName().replaceAll("_", " ").equals(categoryLabel.getText())) {
      isWon = true;
    }
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

  /**
   * This is a helper method that is called when the game ends
   *
   * @param remainingTime the time left when the game is ended
   */
  private void endGame(int remainingTime) {
    // Updating the GUI components
    toolBox.setVisible(false);
    canvas.setDisable(true);
    canvas.setOnMouseDragged(null);
    saveDrawingButton.setDisable(false);
    newRoundButton.setDisable(false);

    // Change the background of count down to red
    Platform.runLater(
        () -> {
          timerLabel.setText(String.valueOf(remainingTime));
          countdownHorizontalBox.setBackground(
              new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        });

    TextToSpeech textToSpeech = new TextToSpeech();
    if (isWon) {
      Platform.runLater(() -> winLostLabel.setText("YOU WON!!!"));
      textToSpeech.speak("Congratulations, you won!");
      try {
        // Record the result to the current user
        recordResult(MenuController.currentlyActiveUser, isWon, 60 - remainingTime);
      } catch (IOException e) {
        e.printStackTrace();
      }
      // The player has lost
    } else {
      Platform.runLater(() -> winLostLabel.setText("YOU LOST!!!"));
      textToSpeech.speak("Sorry you lost, try again next time.");
      try {
        recordResult(MenuController.currentlyActiveUser, isWon, 60 - remainingTime);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method records the game result and the category played of the current user
   *
   * @param userName the name of the user
   * @param isWon whether the user won
   * @param timeTaken the time taken for the game to end
   * @throws IOException
   */
  private void recordResult(String userName, boolean isWon, int timeTaken) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader("user.json");
    List<User> users = gson.fromJson(fr, userListType);
    fr.close();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }

    // Record the game result
    if (isWon) {
      users.get(userNames.indexOf(userName)).won();
      users.get(userNames.indexOf(userName)).updateFastestWon(timeTaken);
    } else {
      users.get(userNames.indexOf(userName)).lost();
    }

    // Record the category played
    users.get(userNames.indexOf(userName)).newWord(category);

    FileWriter fw = new FileWriter("user.json", false);
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
      Parent root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the "Statistics" button is pressed, it shows the statistics of the
   * current player in a new window
   *
   * @param event the event of clicking this button
   * @throws IOException
   */
  @FXML
  private void onStatistics(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/stats.fxml"));
    Stage stage = new Stage();
    stage.setTitle("Statistics");
    stage.setResizable(false);
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Save the drawing on a file when a game has ended
   *
   * @param event the event of clicking "Save Your Drawing" button
   * @throws IOException
   */
  @FXML
  private void onSaveDrawing(ActionEvent event) {
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
      Alert successfulSave = new Alert(AlertType.INFORMATION);
      successfulSave.setHeaderText("Image successfully saved");
      successfulSave.show();
    } catch (Exception e) {
      Alert unsuccessfulSave = new Alert(AlertType.ERROR);
      unsuccessfulSave.setHeaderText("Image not saved");
      unsuccessfulSave.show();
    }
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
}
