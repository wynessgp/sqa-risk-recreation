package datasource;

import java.net.URL;
import java.nio.file.Paths;

public class ImageFileLoader implements FileLoader {
    private static final String IMAGE_DIRECTORY = "images/";

    private URL imageFile;

    @Override
    public boolean open(String fileName) {
        URL file = getClass().getResource(IMAGE_DIRECTORY + fileName);
        checkFileExistence(fileName, file);
        this.imageFile = file;
        return true;
    }

    private void checkFileExistence(String fileName, URL file) {
        if (!fileName.endsWith(".png")) {
            throw new IllegalArgumentException("The requested file is not an image file");
        }
        try {
            Paths.get(file.toURI());
        } catch (Exception e) {
            throw new NullPointerException("The requested file does not exist");
        }
    }

    @Override
    public URL getFileUrl() {
        return this.imageFile;
    }

}
