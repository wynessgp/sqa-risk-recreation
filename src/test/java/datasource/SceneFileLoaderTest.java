package datasource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SceneFileLoaderTest {

    @Test
    public void test00_open_withValidFxmlFile_returnsTrue() {
        FileLoader fileLoader = new SceneFileLoader();
        String fileName = "start_screen.fxml";
        assertTrue(fileLoader.open(fileName));
        assertNotNull(fileLoader.getFile());
    }
}
