import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main class of the application.
 * This class stores the list of valid users and starts the JavaFX application.
 * It also loads the users from the input file before showing the login screen.
 */
public class Main {
    private static final ArrayList<User> users = new ArrayList<>();
    private static int maxAttempts;
    private static int blockTimeSeconds;

    // Returns the list of valid users loaded from the file.
    public static ArrayList<User> getUsers() {
        return users;
    }

    public static int getMaxAttempts() {
        return maxAttempts;
    }

    public static int getBlockTimeSeconds() {
        return blockTimeSeconds;
    }

    // Launches the JavaFX application.
    public static void main(String[] args) {
        Application.launch(LoginApp.class, args);
    }

    // class that responsible for opening the login window and closing the application properly.
    public static class LoginApp extends Application {
        /**
         * Opens the first screen of the application - the login screen.
         * Also loads the valid users from the file.
         */
        @Override
        public void start(Stage stage) throws Exception {
            getRuntimeParameters();
            loadUsersFromFile();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 250);

            stage.setTitle("Users Login");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                stage.close();
                System.exit(0);
            });
        }

        private void getRuntimeParameters() {
            TextInputDialog attemptsDialog = new TextInputDialog();
            attemptsDialog.setTitle("Login Settings");
            attemptsDialog.setHeaderText("Enter maximum failed attempts");
            attemptsDialog.setContentText("n:");
            attemptsDialog.setGraphic(null);

            Optional<String> attemptsResult = attemptsDialog.showAndWait();
            if (attemptsResult.isPresent()) {
                maxAttempts = Integer.parseInt(attemptsResult.get());
            } else {
                System.exit(0);
            }

            TextInputDialog blockDialog = new TextInputDialog();
            blockDialog.setTitle("Login Settings");
            blockDialog.setHeaderText("Enter block time in seconds");
            blockDialog.setContentText("t:");
            blockDialog.setGraphic(null);

            Optional<String> blockResult = blockDialog.showAndWait();
            if (blockResult.isPresent()) {
                blockTimeSeconds = Integer.parseInt(blockResult.get());
            } else {
                System.exit(0);
            }
        }

        /**
         * Reads the users from the input file.
         * Only valid users are added to the users list.
         * Invalid users are printed to the console with an error message.
         */
        private void loadUsersFromFile() {
            try {
                File inputFile = new File("users.txt");
                Scanner scanner = new Scanner(inputFile);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.trim().split("\\s+");

                    String username = parts[0];
                    String password = parts[1];

                    try {
                        User user = new User(username, password);
                        users.add(user);
                    } catch (IllegalArgumentException e) {
                        System.out.println(line + " - " + e.getMessage());
                    }
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
        }
    }
}