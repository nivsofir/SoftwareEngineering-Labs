import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    private static final ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}