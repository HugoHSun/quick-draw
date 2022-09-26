package nz.ac.auckland.se206.controller;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  public enum AppUi {
    MAIN_MENU,
    CANVAS
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi sceneName, Parent parentNode) {
    sceneMap.put(sceneName, parentNode);
  }

  public static Parent getUi(AppUi uiName) {
    return sceneMap.get(uiName);
  }
}
