package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlayerSelectScreenController {
    @FXML
    private Button blackPlayer;
    @FXML
    private Button redPlayer;
    @FXML
    private Button yellowPlayer;
    @FXML
    private Button bluePlayer;
    @FXML
    private Button greenPlayer;
    @FXML
    private Button purplePlayer;

    public void initialize() {
        blackPlayer.setOnAction(e -> onPlayerSelect(blackPlayer));
        redPlayer.setOnAction(e -> onPlayerSelect(redPlayer));
        yellowPlayer.setOnAction(e -> onPlayerSelect(yellowPlayer));
        bluePlayer.setOnAction(e -> onPlayerSelect(bluePlayer));
        greenPlayer.setOnAction(e -> onPlayerSelect(greenPlayer));
        purplePlayer.setOnAction(e -> onPlayerSelect(purplePlayer));
    }

    @FXML
    private void onBackButtonClick() {
        SceneController.getInstance().activate(SceneType.START);
    }

    private void onPlayerSelect(Button button) {
    }

}
