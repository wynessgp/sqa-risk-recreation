package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

public class StartScreenController implements GameScene {
    @FXML
    private ComboBox<String> languageSelect;

    @FXML
    private void initialize() {
        SceneController.setCurrentScene(this);
        languageSelect.getItems().addAll("English", "Bruh");
        languageSelect.setValue(SceneController.getLanguage());
        languageSelect.addEventHandler(javafx.event.ActionEvent.ACTION, event ->
            SceneController.setLanguage(languageSelect.getValue()));
    }

    @FXML
    private void onStartButtonClick() {
        SceneController.getInstance().activate(SceneType.PLAYER_SELECT);
    }

    @Override
    public void onKeyPress(KeyEvent event) {}

}
