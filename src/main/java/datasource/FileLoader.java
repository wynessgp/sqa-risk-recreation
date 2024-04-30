package datasource;

import java.net.URL;

public interface FileLoader {
    boolean open(String fileName);

    URL getFileUrl();
}
