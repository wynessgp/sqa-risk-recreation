package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void test01_open_withInvalidFxmlFile_throwsException() {
        FileLoader fileLoader = new SceneFileLoader();
        String fileName = "missing_file.fxml";
        Exception exception = assertThrows(NullPointerException.class, () -> fileLoader.open(fileName));

        String expectedMessage = "The requested file does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

}
