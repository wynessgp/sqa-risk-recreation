package presentation;

import javafx.fxml.FXML;

public class RiskStartPageController {

    @FXML
    private void onStartButtonClick() {
        SceneController.getInstance().activate(SceneType.PLAYER_SELECT);
    }

}
