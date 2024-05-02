package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ResourceBundle;
import org.junit.jupiter.api.Test;

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

    @Test
    public void test01_open_withBruhBundleLocale_returnsTrue() {
        BundleLoader bundleLoader = new StringsBundleLoader();
        String localeName = "Bruh";
        assertTrue(bundleLoader.open(localeName));

        ResourceBundle bundle = bundleLoader.getBundle();
        assertNotNull(bundle);

        String expected = "Risk VI: Not the Architecture";
        String actual = bundle.getString("startScreen.title");
        assertEquals(expected, actual);
    }

    @Test
    public void test02_open_withInvalidLocale_throwsException() {
        BundleLoader bundleLoader = new StringsBundleLoader();
        String fileName = "Nonexistent";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bundleLoader.open(fileName));

        String expectedMessage = "The requested bundle does not exist";
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(bundleLoader.getBundle());
    }

}
