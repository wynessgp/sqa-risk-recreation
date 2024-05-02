package datasource;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringsBundleLoader implements BundleLoader {

    @Override
    public boolean open(String locale) {
        return true;
    }

    @Override
    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("strings", new Locale("English"));
    }

}
