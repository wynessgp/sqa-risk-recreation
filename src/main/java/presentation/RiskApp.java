package presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RiskApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("risk_app.fxml"));
        String image = getClass().getResource("images/smile.PNG").toExternalForm();
        Scene cssModifiedScene = addCssFileToScene(getClass().getResource("styles.css").toExternalForm(),
                new Scene(root));
        stage.getIcons().add(new Image(image));
        stage.setScene(cssModifiedScene);
        stage.setTitle("Risk");
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
