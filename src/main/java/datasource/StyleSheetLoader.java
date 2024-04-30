package datasource;

import java.net.URL;
import java.nio.file.Paths;

public class StyleSheetLoader implements FileLoader {
    private URL cssFile;

    @Override
    public boolean open(String fileName) {
        URL file = getClass().getResource(fileName);
        try {
            Paths.get(file.toURI());
        } catch (Exception e) {
            throw new NullPointerException("The requested file does not exist");
        }
        this.cssFile = file;
        return true;
    }

    @Override
    public URL getFileUrl() {
        return this.cssFile;
    }

}
