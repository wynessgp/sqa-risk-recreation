package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.ResourceBundle;

public class StringsBundleLoaderTest {

    @Test
    public void test00_open_withEnglishBundleLocale_returnsTrue() {
        BundleLoader bundleLoader = new StringsBundleLoader();
        String localeName = "English";
        assertTrue(bundleLoader.open(localeName));

        ResourceBundle bundle = bundleLoader.getBundle();
        assertNotNull(bundle);

        String expected = "Risk: World Domination";
        String actual = bundle.getString("startScreen.title");
        assertEquals(expected, actual);
    }

}
