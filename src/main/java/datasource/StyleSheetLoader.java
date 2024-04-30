package datasource;

import java.net.URL;

public class StyleSheetLoader implements FileLoader {
    private URL cssFile;

    @Override
    public boolean open(String fileName) {
        this.cssFile = getClass().getResource(fileName);
        return true;
    }

    @Override
    public URL getFileUrl() {
        return this.cssFile;
    }

}
