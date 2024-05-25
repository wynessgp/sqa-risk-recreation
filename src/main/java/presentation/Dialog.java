package presentation;

import java.util.HashMap;
import java.util.Map;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

class Dialog {
    private final DialogPane dialog;
    private final AnchorPane dialogBackground;
    private final Map<ButtonType, EventHandler<Event>> events = new HashMap<>();

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
        boolean eventHandlerExists = events.containsKey(buttonType);
        ((Button) dialog.lookupButton(buttonType)).setText(SceneController.getString(key, null));
        events.put(buttonType, eventHandler);
        if (!eventHandlerExists) {
            dialog.lookupButton(buttonType).addEventHandler(javafx.event.ActionEvent.ACTION, events.get(buttonType));
        }
    }

    void showButton(ButtonType button) {
        dialog.lookupButton(button).setVisible(true);
    }

    void hideButton(ButtonType button) {
        dialog.lookupButton(button).setVisible(false);
    }

    void setDialogContent(Node content) {
        dialog.setContent(new Pane(content));
    }

    DialogPane getDialog() {
        return dialog;
    }
}
