package presentation;

import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RiskApp extends Application {

    private final String cssFileString = Objects.requireNonNull(
            getClass().getResource("styles.css")).toExternalForm();
    private final String iconImageString = Objects.requireNonNull(
            getClass().getResource("images/smile.PNG")).toExternalForm();
    private final URL fxmlFileUrl = Objects.requireNonNull(
            getClass().getResource("start_screen.fxml"));

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(fxmlFileUrl);
        Scene cssModifiedScene = addCssFileToScene(cssFileString, new Scene(root));
        SceneController.setRoot(cssModifiedScene);
        prepareScenes();
        stage.setScene(cssModifiedScene);
        performStageSetup(stage);
    }

    private Scene addCssFileToScene(String cssFileString, Scene sceneInQuestion) {
        sceneInQuestion.getStylesheets().add(cssFileString);
        return sceneInQuestion;
    }

    private void prepareScenes() {
        for (SceneType scene : SceneType.values()) {
            try {
                SceneController.getInstance().add(scene, FXMLLoader.load(Objects.requireNonNull(getClass()
                        .getResource(scene.getSceneName()))));
            } catch (Exception e) {
                System.err.println("Error loading scene: " + scene.getSceneName());
            }
        }
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
