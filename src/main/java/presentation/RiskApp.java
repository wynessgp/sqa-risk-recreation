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
        stage.getIcons().add(new Image(iconImageString));
        stage.setScene(cssModifiedScene);
        stage.setTitle("Risk");
        stage.setResizable(false);
        stage.show();
    }

    private Scene addCssFileToScene(String cssFileString, Scene sceneInQuestion) {
        sceneInQuestion.getStylesheets().add(cssFileString);
        return sceneInQuestion;
    }

    public static void main(String[] args) {
        launch();
    }
}
