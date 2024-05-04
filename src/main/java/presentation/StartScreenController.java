package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class StartScreenController {
    @FXML
    private ComboBox<String> languageSelect;

    @FXML
    private void initialize() {
        languageSelect.getItems().addAll("English", "Bruh");
        languageSelect.setValue(SceneController.getLanguage());
        languageSelect.addEventHandler(javafx.event.ActionEvent.ACTION, event ->
            SceneController.setLanguage(languageSelect.getValue()));
    }

    @FXML
    private void onStartButtonClick() {
        SceneController.getInstance().activate(SceneType.PLAYER_SELECT);
    }

}
