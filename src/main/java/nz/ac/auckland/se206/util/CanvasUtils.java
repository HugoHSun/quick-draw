package nz.ac.auckland.se206.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public class CanvasUtils {

  /**
   * This method checks whether the canvas is empty and returns a boolean
   *
   * @param canvas the canvas to check
   * @return true if the canvas is empty, false otherwise
   */
  public static boolean checkEmptyCanvas(Canvas canvas) {
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

  public static void saveDrawing(Window stage, Canvas canvas) throws IOException {
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
    File file = fileChooser.showSaveDialog(stage);

    try {
      // Save the image to a file and pop up a message to show if the image is saved
      ImageIO.write(CanvasUtils.getCurrentSnapshot(canvas), "bmp", file);
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
  public static BufferedImage getCurrentSnapshot(Canvas canvas) {
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
