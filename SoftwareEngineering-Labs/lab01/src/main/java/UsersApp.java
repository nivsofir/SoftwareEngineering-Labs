import java.io.*;
import java.util.*;

public class UsersApp {
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