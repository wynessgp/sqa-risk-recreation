package datasource;

import java.io.File;

public class SceneFileLoader implements FileLoader {
    @Override
    public boolean open(String fileName) {
        if (fileName.equals("start_screen.fxml")) {
            return true;
        }
        throw new NullPointerException("The requested file does not exist");
    }

    @Override
    public File getFile() {
        return new File("");
    }
}
