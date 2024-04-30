package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageFileLoaderTest {

    @Test
    public void test00_open_withValidImageFile_returnsTrue() {
        FileLoader fileLoader = new ImageFileLoader();
        String fileName = "risk.png";
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
    public void test01_open_withInvalidImageFile_throwsException() {
        FileLoader fileLoader = new ImageFileLoader();
        String fileName = "missing_file.png";
        Exception exception = assertThrows(NullPointerException.class, () -> fileLoader.open(fileName));

        String expectedMessage = "The requested file does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

}
