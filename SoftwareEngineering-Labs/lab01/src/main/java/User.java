/**
 * The User class represents a single user in the system.
 * Each user has a username and a password.
 * The constructor validates the input according to the lab requirements.
 * If one of the conditions is not met, an IllegalArgumentException is thrown
 * with a matching error message.
 */
public class User {
    private final String username;
    private final String password;

// Creates a new User object after validating the username and password.
    public User(String username, String password) {
        if (username==null || !isValidEmail(username)) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        if (username.length()>50) {
            throw new IllegalArgumentException("Username is too long, try something shorter");
        }

        if (password == null || password.length()<8) {
            throw new IllegalArgumentException("Your password is too short, add more characters");
        }

        if (password.length()>12) {
            throw new IllegalArgumentException("Your password is too long, try a shorter one");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Please enter a valid password");
        }

        this.username = username;
        this.password = password;
    }
    // Returns the username of the user.
    public String getUsername() {
        return username;
    }
    //Returns the password of the user.
    public String getPassword() {
        return password;
    }
//Checks whether the given email is in a valid format.
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9][A-Za-z0-9.\\-]*\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
// Checks whether the password is valid.
    private boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        String allowedSymbols = "~!@#$%^&*()_+";

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (allowedSymbols.indexOf(ch) != -1) {
                hasSymbol = true;
            } else {
                return false;
            }
        }

        return hasLetter && hasDigit && hasSymbol;
    }
//  Returns a string representation of the user. The format is: username + space + password
    @Override
    public String toString() {
        return username + " " + password;
    }


}