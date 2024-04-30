package datasource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

}
