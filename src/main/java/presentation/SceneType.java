package presentation;

public enum SceneType {
    START("start_screen"),
    PLAYER_SELECT("player_select_screen"),
    GAME("game_map_screen");

    private final String sceneName;

    SceneType(String sceneName) {
        this.sceneName = sceneName;
    }

    String getSceneName() {
        return sceneName + ".fxml";
    }

}
