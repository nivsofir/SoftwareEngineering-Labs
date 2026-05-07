import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the login screen.
 * Handles user input and checks if the entered username and password
 * match one of the valid users loaded into the system.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User foundUser = null;

        for (User user : Main.getUsers()) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            messageLabel.setText("User does not exist");
            return;
        }

        if (foundUser.getPassword().equals(password)) {
            Runnable checkBlockedTask = new CheckBlockedTask(foundUser, event);
            Thread thread = new Thread(checkBlockedTask);
            thread.start();
        } else {
            Runnable failedAttemptTask = new FailedAttemptTask(foundUser);
            Thread thread = new Thread(failedAttemptTask);
            thread.start();
        }
    }

    /**
     * Task responsible for updating failed attempts
     * and blocking the user if needed.
     */
    private class FailedAttemptTask implements Runnable {
        private final User user;

        public FailedAttemptTask(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            synchronized (user) {
                if (user.isBlocked()) {
                    long remainingSeconds = user.getRemainingBlockedSeconds();
                    Platform.runLater(() ->
                            messageLabel.setText("User is blocked. Try again in " + remainingSeconds + " seconds"));
                    return;
                }

                user.incrementFailedAttempts();

                if (user.getFailedAttempts() >= Main.getMaxAttempts()) {
                    user.blockForSeconds(Main.getBlockTimeSeconds());
                    user.resetFailedAttempts();

                    Platform.runLater(() ->
                            messageLabel.setText("Too many failed attempts. User is blocked for "
                                    + Main.getBlockTimeSeconds() + " seconds"));
                } else {
                    int left = Main.getMaxAttempts() - user.getFailedAttempts();
                    Platform.runLater(() ->
                            messageLabel.setText("Wrong password. Attempts left: " + left));
                }
            }
        }
    }

    /**
     * Task responsible for checking whether a user is blocked
     * before allowing successful login.
     */
    private class CheckBlockedTask implements Runnable {
        private final User user;
        private final ActionEvent event;

        public CheckBlockedTask(User user, ActionEvent event) {
            this.user = user;
            this.event = event;
        }

        @Override
        public void run() {
            synchronized (user) {
                if (user.isBlocked()) {
                    long remainingSeconds = user.getRemainingBlockedSeconds();
                    Platform.runLater(() ->
                            messageLabel.setText("User is blocked. Try again in " + remainingSeconds + " seconds"));
                    return;
                }

                user.resetFailedAttempts();

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
                        Scene welcomeScene = new Scene(loader.load(), 400, 250);

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(welcomeScene);
                        stage.setTitle("Welcome");

                        stage.setOnCloseRequest(e -> {
                            stage.close();
                            System.exit(0);
                        });
                    } catch (IOException e) {
                        messageLabel.setText("Failed to load welcome screen");
                    }
                });
            }
        }
    }
}