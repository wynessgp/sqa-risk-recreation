package datasource;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringsBundleLoader {
    private static final String BUNDLE_NAME = "strings";
    private static final StringsBundleLoader INSTANCE = new StringsBundleLoader();
    private String localeName = "";

    private boolean openBundle(String locale) {
        ResourceBundle resourceBundle = locateResource(locale);
        if (resourceBundle == null || !resourceBundle.getLocale().toString().contains(locale.toLowerCase())) {
            localeName = "";
            return false;
        }
        localeName = locale;
        return true;
    }

    private ResourceBundle locateResource(String locale) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME, new Locale(locale));
        } catch (Exception e) {
            return null;
        }
    }

    private ResourceBundle getResourceBundle() {
        return localeName.isEmpty() ? null : ResourceBundle.getBundle(BUNDLE_NAME, new Locale(localeName));
    }

    public static boolean open(String locale) {
        if (!INSTANCE.openBundle(locale)) {
            throw new IllegalArgumentException("The requested bundle does not exist");
        }
        return true;
    }

    public static ResourceBundle getBundle() {
        return INSTANCE.getResourceBundle();
    }

}
