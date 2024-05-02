package datasource;

import java.util.ResourceBundle;

public interface BundleLoader {
    boolean open(String locale);

    ResourceBundle getBundle();
}
