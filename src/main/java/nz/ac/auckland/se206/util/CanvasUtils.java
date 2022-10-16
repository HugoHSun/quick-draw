package nz.ac.auckland.se206.util;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

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
}
