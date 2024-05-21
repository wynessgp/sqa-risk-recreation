package presentation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;

class Dialog {
    private final DialogPane dialog;
    private final AnchorPane dialogBackground;

    protected Dialog(DialogPane dialog, AnchorPane background) {
        this.dialog = dialog;
        this.dialogBackground = background;
    }

    boolean isVisible() {
        return dialog.isVisible();
    }

    void toggleDisplay() {
        this.dialog.setVisible(!isVisible());
        this.dialogBackground.setVisible(isVisible());
    }

    void setTitleText(String key, Object[] replacements) {
        this.dialog.setHeaderText(SceneController.getString(key, replacements));
    }

    void setContentText(String key, Object[] replacements) {
        this.dialog.setContentText(SceneController.getString(key, replacements));
    }

    void setupButton(ButtonType buttonType, String key, EventHandler<Event> eventHandler) {
        ((Button) dialog.lookupButton(buttonType)).setText(SceneController.getString(key, null));
        dialog.lookupButton(buttonType).addEventHandler(javafx.event.ActionEvent.ACTION, eventHandler);
    }

    void showButton(ButtonType button) {
        dialog.lookupButton(button).setVisible(true);
    }

    void hideButton(ButtonType button) {
        dialog.lookupButton(button).setVisible(false);
    }

}
