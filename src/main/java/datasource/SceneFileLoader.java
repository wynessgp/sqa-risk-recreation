package datasource;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SceneFileLoader implements FileLoader {
    private File sceneFile;

    @Override
    public boolean open(String fileName) {
        try {
            URL filePath = getClass().getResource(fileName);
            Path path = Paths.get(filePath.toURI());
            this.sceneFile = path.toFile();
        } catch (Exception e) {
            throw new NullPointerException("The requested file does not exist");
        }
        return true;
    }

    @Override
    public File getFile() {
        return this.sceneFile;
    }

}
