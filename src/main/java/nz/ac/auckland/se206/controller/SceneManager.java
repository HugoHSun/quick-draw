package nz.ac.auckland.se206.controller;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    MAIN_MENU,
    CANVAS
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /**
   * Adds a UI to the scene manager
   * 
   * @param sceneName Name of the scene to be added
   * @param parentNode Name of the parent node
   */
  public static void addUi(AppUi sceneName, Parent parentNode) {
    sceneMap.put(sceneName, parentNode);
  }

  /**
   * Getter for the parent UI
   * 
   * @param uiName Child UI name
   * @return
   */
  public static Parent getUi(AppUi uiName) {
    return sceneMap.get(uiName);
  }
}
