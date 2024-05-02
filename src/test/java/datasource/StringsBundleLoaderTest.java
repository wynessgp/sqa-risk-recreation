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
        String localeName = "English";
        assertTrue(StringsBundleLoader.open(localeName));

        ResourceBundle bundle = StringsBundleLoader.getBundle();
        assertNotNull(bundle);

        String expected = "Risk: World Domination";
        String actual = bundle.getString("startScreen.title");
        assertEquals(expected, actual);
    }

    @Test
    public void test01_open_withBruhBundleLocale_returnsTrue() {
        String localeName = "Bruh";
        assertTrue(StringsBundleLoader.open(localeName));

        ResourceBundle bundle = StringsBundleLoader.getBundle();
        assertNotNull(bundle);

        String expected = "Risk VI: Not the Architecture";
        String actual = bundle.getString("startScreen.title");
        assertEquals(expected, actual);
    }

    @Test
    public void test02_open_withInvalidLocale_throwsException() {
        String fileName = "Nonexistent";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> StringsBundleLoader.open(fileName));

        String expectedMessage = "The requested bundle does not exist";
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(StringsBundleLoader.getBundle());
    }

}
