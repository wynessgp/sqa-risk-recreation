package datasource;

import java.io.File;

public interface FileLoader {
    boolean open(String fileName);
    File getFile();
}
