package datasource;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringsBundleLoader implements BundleLoader {
    ResourceBundle bundle;

    @Override
    public boolean open(String locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("strings", new Locale(locale));
        if (!resourceBundle.getLocale().toString().contains(locale.toLowerCase())) {
            throw new IllegalArgumentException("The requested bundle does not exist");
        }
        this.bundle = resourceBundle;
        return true;
    }

    @Override
    public ResourceBundle getBundle() {
        return this.bundle;
    }

}
