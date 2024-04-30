package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class StyleSheetLoaderTest {

    @Test
    public void test00_open_withValidCssFile_returnsTrue() {
        FileLoader fileLoader = new StyleSheetLoader();
        String fileName = "styles.css";
        assertTrue(fileLoader.open(fileName));

        URL fileUrl = fileLoader.getFileUrl();
        try {
            Path file = Paths.get(fileUrl.toURI());
            assertNotNull(fileUrl);
            assertTrue(file.toFile().isFile());
            assertTrue(file.toString().endsWith(fileName));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test01_open_withInvalidCssFile_throwsException() {
        FileLoader fileLoader = new StyleSheetLoader();
        String fileName = "missing_file.css";
        Exception exception = assertThrows(NullPointerException.class, () -> fileLoader.open(fileName));

        String expectedMessage = "The requested file does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test02_open_withValidNonCssFile_throwsException() {
        FileLoader fileLoader = new StyleSheetLoader();
        String fileName = "start_screen.fxml";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> fileLoader.open(fileName));

        String expectedMessage = "The requested file is not a CSS file";
        assertEquals(expectedMessage, exception.getMessage());
    }

}
