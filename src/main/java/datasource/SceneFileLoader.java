package datasource;

import java.io.File;

public class SceneFileLoader implements FileLoader {
    @Override
    public boolean open(String fileName) {
        return true;
    }

    @Override
    public File getFile() {
        return new File("");
    }
}
