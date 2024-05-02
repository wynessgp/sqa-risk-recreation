package datasource;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringsBundleLoader {
    private static final String BUNDLE_NAME = "strings";
    private static String localeName = "";

    public static boolean open(String locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(locale));
        if (!resourceBundle.getLocale().toString().contains(locale.toLowerCase())) {
            localeName = "";
            throw new IllegalArgumentException("The requested bundle does not exist");
        }
        localeName = locale;
        return true;
    }

    public static ResourceBundle getBundle() {
        return localeName.isEmpty() ? null :  ResourceBundle.getBundle(BUNDLE_NAME, new Locale(localeName));
    }

}
