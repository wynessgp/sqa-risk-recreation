package datasource;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SceneFileLoader implements FileLoader {
    private File sceneFile;

    @Override
    public boolean open(String fileName) {
        Path path = generatePathObject(fileName);
        if (!path.toString().endsWith(".fxml")) {
            throw new IllegalArgumentException("The requested file is not an FXML file");
        }
        this.sceneFile = path.toFile();
        return true;
    }

    private Path generatePathObject(String fileName) {
        try {
            URL filePath = getClass().getResource(fileName);
            return Paths.get(filePath.toURI());
        } catch (Exception e) {
            throw new NullPointerException("The requested file does not exist");
        }
    }

    @Override
    public File getFile() {
        return this.sceneFile;
    }

}
