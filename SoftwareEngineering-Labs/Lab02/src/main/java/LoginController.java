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

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        boolean found = false;

        for (User user : Main.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                found = true;
                break;
            }
        }

        if (found) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
            Scene welcomeScene = new Scene(loader.load(), 400, 250);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(welcomeScene);
            stage.setTitle("Welcome");

            stage.setOnCloseRequest(e -> {
                stage.close();
                System.exit(0);
            });
        } else {
            messageLabel.setText("user or password do not match");
        }
    }
}