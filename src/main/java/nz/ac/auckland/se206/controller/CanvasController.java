package nz.ac.auckland.se206.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CategorySelector;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This is the controller of the canvas. You are free to modify this class and
 * the corresponding FXML file as you see fit. For example, you might no longer
 * need the "Predict" button because the DL model should be automatically
 * queried in the background every second.
 *
 * <p>
 * !! IMPORTANT !!
 *
 * <p>
 * Although we added the scale of the image, you need to be careful when
 * changing the size of the drawable canvas and the brush size. If you make the
 * brush too big or too small with respect to the canvas size, the ML model will
 * not work correctly. So be careful. If you make some changes in the canvas and
 * brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

	@FXML
	private Canvas canvas;

	private GraphicsContext graphic;

	private DoodlePrediction model;

	@FXML
	private Label categoryLabel;

	@FXML
	private VBox gameVerticalBox;

	@FXML
	private Button penButton;

	@FXML
	private Button eraserButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button startDrawingButton;

	@FXML
	private Button newGameButton;

	@FXML
	private Button saveDrawingButton;

	@FXML
	private Label timerLabel;

	@FXML
	private HBox countdownHorizontalBox;

	@FXML
	private Label winLostLabel;

	@FXML
	private Label predictionLabel;

	@FXML
	private TextField folderNameText;

	@FXML
	private TextField imageNameText;

	private boolean isWon = false;

//mouse coordinates
	private double currentX;
	private double currentY;

	/**
	 * JavaFX calls this method once the GUI elements are loaded. In our case we
	 * create a listener for the drawing, and we load the ML model.
	 *
	 * @throws ModelException If there is an error in reading the input/output of
	 *                        the DL model.
	 * @throws IOException    If the model cannot be found on the file system.
	 */
	public void initialize() throws ModelException, IOException {
		// Randomly chose a category and update the label to display it
		String category = new CategorySelector().getRandomCategory(CategorySelector.Difficulty.E);
		categoryLabel.setText(category);
		Thread voiceOver = new Thread(() -> {
			TextToSpeech textToSpeech = new TextToSpeech();
			textToSpeech.speak("Please draw: " + category);
		});
		voiceOver.start();
		graphic = canvas.getGraphicsContext2D();

		// save coordinates when mouse is pressed on the canvas
		canvas.setOnMousePressed(e -> {
			currentX = e.getX();
			currentY = e.getY();
		});

		canvas.setOnMouseDragged(e -> {
			// Brush size (you can change this, it should not be too small or too large).
			final double size = 6;

			final double x = e.getX() - size / 2;
			final double y = e.getY() - size / 2;

			// This is the colour of the brush.
			graphic.setFill(Color.BLACK);
			graphic.setLineWidth(size);

			// Create a line that goes from the point (currentX, currentY) and (x,y)
			graphic.strokeLine(currentX, currentY, x, y);

			// update the coordinates
			currentX = x;
			currentY = y;
		});

		model = new DoodlePrediction();
	}

	/** This method is called when the "Clear" button is pressed. */
	@FXML
	private void onClear() {
		graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	/** This method is called when the "Pen" button is presses */
	@FXML
	private void onPen() {
		penButton.setDisable(true);
		eraserButton.setDisable(false);
		canvas.setOnMouseDragged(e -> {
			// Brush size (you can change this, it should not be too small or too large).
			final double size = 5.0;

			final double x = e.getX() - size / 2;
			final double y = e.getY() - size / 2;

			// This is the colour of the brush.
			graphic.setFill(Color.BLACK);
			graphic.fillOval(x, y, size, size);
		});
	}

	/** This method is called when the "Eraser" button is pressed */
	@FXML
	private void onErase() {
		penButton.setDisable(false);
		eraserButton.setDisable(true);
		canvas.setOnMouseDragged(e -> {
			// Brush size (you can change this, it should not be too small or too large).
			final double size = 5.0;

			final double x = e.getX() - size / 2;
			final double y = e.getY() - size / 2;

			// This is the colour of the brush.
			graphic.setFill(Color.WHITE);
			graphic.fillOval(x, y, size, size);
		});
	}

	/** This method is called when the user click on "Start drawing" button */
	@FXML
	private void onStartDrawing() {
		// Turn on the canvas and remove the button
		canvas.setDisable(false);
		gameVerticalBox.getChildren().remove(startDrawingButton);

		// Create a timer thread running in the background that counts from 60 to 0, and
		// also update the predictions
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			private Integer secondsLeft = 60;

			public void run() {
				// Ask the GUI thread to update time display. Also get predictions and update
				// the display.
				Platform.runLater(() -> {
					timerLabel.setText(secondsLeft.toString());
					List<Classification> currentPredictions;
					try {
						currentPredictions = model.getPredictions(getCurrentSnapshot(), 10);
						// Build the string to display the top ten predictions
						final StringBuilder sb = new StringBuilder();
						int i = 1;
						for (final Classifications.Classification classification : currentPredictions) {
							// When the top 3 predictions contain the category
							if (classification.getClassName().replaceAll("_", " ").equals(categoryLabel.getText())
									&& i <= 3) {
								isWon = true;
							}
							// Build the predictions string to be displayed
							sb.append("TOP ").append(i).append(" : ")
									.append(classification.getClassName().replaceAll("_", " ")).append(" : ")
									.append(String.format("%.2f%%", 100 * classification.getProbability()))
									.append(System.lineSeparator());
							i++;
						}
						predictionLabel.setText(sb.toString());
					} catch (TranslateException e) {
						e.printStackTrace();
					}
				});
				secondsLeft--;

				// Remind the user when there are 10 seconds left
				if (secondsLeft == 11) {
					Thread timeReminder = new Thread(() -> new TextToSpeech().speak("Ten seconds left"));
					timeReminder.start();
				}

				// When a game ends
				if (secondsLeft == 0 || isWon) {
					timer.cancel();
					// Setting the buttons
					canvas.setDisable(true);
					canvas.setOnMouseDragged(null);
					folderNameText.setDisable(false);
					imageNameText.setDisable(false);
					penButton.setDisable(true);
					eraserButton.setDisable(true);
					clearButton.setDisable(true);
					saveDrawingButton.setDisable(false);
					newGameButton.setDisable(false);
					// Change the background of count down to red
					Platform.runLater(() -> {
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
		}, 1000, 1000);
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
		final BufferedImage imageBinary = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);

		final Graphics2D graphics = imageBinary.createGraphics();

		graphics.drawImage(image, 0, 0, null);

		// To release memory we dispose.
		graphics.dispose();

		return imageBinary;
	}

	/**
	 * This method is called when "Start a new game" is pressed
	 *
	 * @param event the event of clicking the button
	 */
	@FXML
	private void onNewGame(ActionEvent event) {
		Scene scene = ((Node) event.getSource()).getScene();
		try {
			// Load a new version of node tree
			Parent root = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml")).load();
			scene.setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** This method is called when the "Save drawing" button is pressed */
	@FXML
	private void onSaveDrawing() {
		String folderName = folderNameText.getText();
		String imageName = imageNameText.getText();

		// When the user didn't type anything for name
		if (folderName.isBlank() || imageName.isBlank()) {
			try {
				// Save the image as default
				saveCurrentSnapshotOnFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				saveCurrentSnapshotOnFile(folderName, imageName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		final File imageToClassify = new File(tmpFolder.getName() + "/snapshot" + System.currentTimeMillis() + ".bmp");

		// Save the image to a file.
		ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

		return imageToClassify;
	}

	/**
	 * @param folderName The folder to save the image
	 * @param fileName   The name for the image to be saved as
	 * @return The file of the saved image.
	 * @throws IOException If the image cannot be saved.
	 */
	private File saveCurrentSnapshotOnFile(String folderName, String fileName) throws IOException {
		// The folder where the image will be stored in, it will be created if there is
		// no such folder
		final File tmpFolder = new File(folderName);

		if (!tmpFolder.exists()) {
			tmpFolder.mkdir();
		}

		// We save the image to a file in the chosen folder.
		final File imageToClassify = new File(tmpFolder.getName() + "/" + fileName + ".bmp");

		// Save the image to a file.
		ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

		return imageToClassify;
	}
}
