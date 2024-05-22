package presentation;

import datasource.FileLoader;
import datasource.ImageFileLoader;
import datasource.SceneFileLoader;
import datasource.StyleSheetLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RiskApp extends Application {
    private String cssFileString;
    private String iconImageString;

    @Override
    public void start(Stage stage) {
        SceneController.initializeLanguageBundle();
        Parent root = loadStartScreen();
        loadUniversalFiles();
        if (root != null) {
            initializeScreen(root, stage);
        }
    }

    private Parent loadStartScreen() {
        try {
            FileLoader fileLoader = new SceneFileLoader();
            fileLoader.open(SceneType.START.getSceneName());
            return FXMLLoader.load(fileLoader.getFileUrl(), SceneController.getLanguageBundle());
        } catch (Exception e) {
            System.err.println("Error loading start scene");
            return null;
        }
    }

    private void loadUniversalFiles() {
        FileLoader loader = new StyleSheetLoader();
        loader.open("styles.css");
        cssFileString = loader.getFileUrl().toExternalForm();
        loader = new ImageFileLoader();
        loader.open("smile.png");
        iconImageString = loader.getFileUrl().toExternalForm();
    }

    private void initializeScreen(Parent root, Stage stage) {
        Scene cssModifiedScene = addCssFileToScene(cssFileString, new Scene(root));
        Scene keypressScene = addKeyListenerToScene(cssModifiedScene);
        SceneController.setRoot(keypressScene);
        stage.setScene(keypressScene);
        performStageSetup(stage);
    }

    private Scene addCssFileToScene(String cssFileString, Scene sceneInQuestion) {
        sceneInQuestion.getStylesheets().add(cssFileString);
        return sceneInQuestion;
    }

    private Scene addKeyListenerToScene(Scene scene) {
        scene.setOnKeyPressed(event ->
            SceneController.getInstance().onKeyPress(event)
        );
        return scene;
    }

    private void performStageSetup(Stage stage) {
        stage.getIcons().add(new Image(iconImageString));
        stage.setTitle("Risk");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
