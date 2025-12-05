import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.gamemanager.FileManager;
import game.gamemanager.GameManager;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class GameManagerTest {
    @BeforeAll
    public static void setupFiles() throws IOException {
        Platform.startup(() -> {});
        FileManager.init();
    }

    @Test
    public void testCheckLoggingInUsernameWithValidCredentials() throws Exception {
        String username = "user_" + UUID.randomUUID();
        String password = "Secret123";
        boolean saved = FileManager.saveUserAccount(username, password);
        assertTrue(saved, "New user account should be saved successfully.");
        GameManager gm = new GameManager();
        Label messageLabel = new Label();
        boolean ok = gm.checkLoggingInUsername(username, password, messageLabel);
        assertTrue(ok, "Valid credentials should return true.");
        assertEquals("",
            messageLabel.getText(),
            "Label text should remain empty on successful login.");
    }

    @Test
    public void testCheckLoggingInUsernameWithInvalidCredentials() {
        String username = "nonExistentUser_" + UUID.randomUUID();
        String password = "wrongPassword";
        GameManager gm = new GameManager();
        Label messageLabel = new Label();
        boolean ok = gm.checkLoggingInUsername(username, password, messageLabel);
        assertFalse(ok, "Invalid credentials should return false.");
        assertEquals("Incorrect Username or password",
            messageLabel.getText(),
            "Label should display an error message for invalid credentials.");
    }
}
