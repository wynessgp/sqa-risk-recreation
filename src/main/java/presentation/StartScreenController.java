package presentation;

import javafx.fxml.FXML;

public class StartScreenController {

    @FXML
    private void onStartButtonClick() {
        SceneController.getInstance().activate(SceneType.PLAYER_SELECT);
    }

}
