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
        return this.dialog.isVisible();
    }

    void toggleDisplay() {
        this.dialog.setVisible(!this.isVisible());
        this.dialogBackground.setVisible(this.isVisible());
    }

    void setTitleText(String key, Object[] replacements) {
        this.dialog.setHeaderText(SceneController.getString(key, replacements));
    }

    void setContentText(String key, Object[] replacements) {
        this.dialog.setContentText(SceneController.getString(key, replacements));
    }

    void setupButton(ButtonType buttonType, String key, EventHandler<Event> eventHandler) {
        ((Button) this.dialog.lookupButton(buttonType)).setText(SceneController.getString(key, null));
        this.dialog.lookupButton(buttonType).addEventHandler(javafx.event.ActionEvent.ACTION, eventHandler);
    }

}
