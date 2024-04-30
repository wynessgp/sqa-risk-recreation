package datasource;

import java.net.URL;

public class ImageFileLoader implements FileLoader {
    private static final String IMAGE_DIRECTORY = "images/";

    private URL imageFile;

    @Override
    public boolean open(String fileName) {
        this.imageFile = getClass().getResource(IMAGE_DIRECTORY + fileName);
        return true;
    }

    @Override
    public URL getFileUrl() {
        return this.imageFile;
    }

}
