package presentation;

import datasource.FileLoader;
import datasource.SceneFileLoader;
import domain.PlayerColor;
import domain.WorldDominationGameEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneController {
    private static SceneController sceneController;
    private static ResourceBundle languageBundle = ResourceBundle.getBundle("strings", new Locale("English"));
    private static String languageName = "English";
    private final Map<SceneType, Pane> screenMap = new HashMap<>();
    private final Scene main;
    private WorldDominationGameEngine gameEngine;
    private List<PlayerColor> originalPlayerOrder;

    private SceneController(Scene main) {
        this.main = main;
    }

    protected void add(SceneType scene, Pane pane) {
        screenMap.put(scene, pane);
    }

    protected void activate(SceneType scene) {
        try {
            FileLoader fileLoader = new SceneFileLoader();
            fileLoader.open(scene.getSceneName());
            sceneController.add(scene, FXMLLoader.load(fileLoader.getFileUrl(), languageBundle));
            main.setRoot(screenMap.get(scene));
        } catch (Exception e) {
            System.err.println("Error loading scene: " + scene.getSceneName());
        }
    }

    protected void initializePlayers(List<PlayerColor> players) {
        this.gameEngine = new WorldDominationGameEngine(players);
        this.originalPlayerOrder = new ArrayList<>(players);
    }

    protected WorldDominationGameEngine getGameEngine() {
        return gameEngine;
    }

    protected List<PlayerColor> getOriginalPlayerOrder() {
        return originalPlayerOrder;
    }

    protected static void setRoot(Scene scene) {
        sceneController = new SceneController(scene);
    }

    protected static SceneController getInstance() {
        return sceneController;
    }

    protected static void setLanguage(String language) {
        languageBundle = ResourceBundle.getBundle("strings", new Locale(language));
        languageName = language;
        getInstance().activate(SceneType.START);
    }

    protected static ResourceBundle getLanguageBundle() {
        return languageBundle;
    }

    protected static String getLanguage() {
        return languageName;
    }

}
