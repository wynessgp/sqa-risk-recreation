package presentation;

enum SceneType {
    START("start_screen"),
    PLAYER_SELECT("player_select_screen"),
    SHUFFLE("player_shuffle_screen"),
    GAME("game_map_screen"),
    WIN("win_screen");

    private final String sceneName;

    SceneType(String sceneName) {
        this.sceneName = sceneName;
    }

    String getSceneName() {
        return sceneName + ".fxml";
    }

}
