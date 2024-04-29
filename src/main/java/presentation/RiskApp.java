package presentation;

import datasource.FileLoader;
import datasource.SceneFileLoader;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RiskApp extends Application {
    private final String cssFileString = Objects.requireNonNull(FileLoader.class.getResource("styles.css"))
            .toExternalForm();
    private final String iconImageString = Objects.requireNonNull(FileLoader.class
            .getResource("images/smile.PNG")).toExternalForm();

    @Override
    public void start(Stage stage) {
        Parent root = loadStartScreen();
        if (root != null) {
            Scene cssModifiedScene = addCssFileToScene(cssFileString, new Scene(root));
            SceneController.setRoot(cssModifiedScene);
            stage.setScene(cssModifiedScene);
            performStageSetup(stage);
        }
    }

    private Parent loadStartScreen() {
        try {
            FileLoader fileLoader = new SceneFileLoader();
            fileLoader.open(SceneType.START.getSceneName());
            return FXMLLoader.load(fileLoader.getFileUrl());
        } catch (Exception e) {
            System.err.println("Error loading scene: " + SceneType.START.getSceneName());
            return null;
        }
    }

    private Scene addCssFileToScene(String cssFileString, Scene sceneInQuestion) {
        sceneInQuestion.getStylesheets().add(cssFileString);
        return sceneInQuestion;
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
