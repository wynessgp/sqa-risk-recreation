package datasource;

import java.net.URL;
import java.nio.file.Paths;

public class ImageFileLoader implements FileLoader {
    private static final String IMAGE_DIRECTORY = "images/";

    private URL imageFile;

    @Override
    public boolean open(String fileName) {
        URL file = getClass().getResource(IMAGE_DIRECTORY + fileName);
        try {
            Paths.get(file.toURI());
        } catch (Exception e) {
            throw new NullPointerException("The requested file does not exist");
        }
        this.imageFile = file;
        return true;
    }

    @Override
    public URL getFileUrl() {
        return this.imageFile;
    }

}
