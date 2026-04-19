import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Main class of the application.
 * This class stores the list of valid users and starts the JavaFX application.
 * It also loads the users from the input file before showing the login screen.
 */
public class Main {
    private static final ArrayList<User> users = new ArrayList<>();

    // Returns the list of valid users loaded from the file.
    public static ArrayList<User> getUsers() {
        return users;
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