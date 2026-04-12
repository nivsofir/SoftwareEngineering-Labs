/**
 * The UsersApp class is the main class of the program.
 * It reads user data from the input file, validates each user,
 * stores only valid users, sorts them by username,
 * and writes the valid users to the output file.
 * Invalid users are printed to the console together with
 * the corresponding error message.
 */
import java.io.*;
import java.util.*;


public class UsersApp {
    /**
     * The main method of the program.
     * It reads lines from the input file "users.txt".
     * Each line contains a username and password.
     * For each line:
     * - A User object is created if the data is valid
     * - Invalid input is reported to the console
     * After reading all lines:
     * - The valid users are sorted by username
     * - The sorted users are written to "out.txt"
     */
    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList<>();

        try {
            File inputFile = new File("users.txt");
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] parts=line.trim().split("\\s+");

                if (parts.length!=2) {
                    System.out.println(line + " - Invalid input format");
                    continue;
                }

                String username=parts[0];
                String password=parts[1];

                try {
                    User user=new User(username,password);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    System.out.println(line + " - " + e.getMessage());
                }
            }

            scanner.close();

            Collections.sort(users, (u1, u2) -> u1.getUsername().compareTo(u2.getUsername()));

            PrintWriter writer=new PrintWriter("out.txt");

            for (User user : users) {
                writer.println(user);
            }

            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}