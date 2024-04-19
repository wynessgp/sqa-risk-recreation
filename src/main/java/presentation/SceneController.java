package presentation;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneController {
    private static SceneController sceneController;
    private final Map<SceneType, Pane> screenMap = new HashMap<>();
    private final Scene main;

    private SceneController(Scene main) {
        this.main = main;
    }

    protected void add(SceneType scene, Pane pane) {
        screenMap.put(scene, pane);
    }

    protected void activate(SceneType scene) {
        main.setRoot(screenMap.get(scene));
    }

    protected static void setRoot(Scene scene) {
        sceneController = new SceneController(scene);
    }

    protected static SceneController getInstance() {
        return sceneController;
    }
}
