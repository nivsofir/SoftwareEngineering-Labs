package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        hostField.setText("localhost");
        portField.setText("3000");
        errorLabel.setText("");
    }

    @FXML
    void connectToServer() {
        try {
            String host = hostField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());

            SimpleClient.createClient(host, port);
            SimpleClient.getClient().connect();

            App.setRoot("primary");
        } catch (NumberFormatException e) {
            errorLabel.setText("Port must be a number");
        } catch (IOException e) {
            errorLabel.setText("Could not connect to server");
        }
    }
}