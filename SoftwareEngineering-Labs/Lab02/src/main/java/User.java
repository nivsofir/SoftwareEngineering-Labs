/**
 * Represents a single user in the system.
 * Each user has a username and a password.
 * The constructor validates the input according to the lab requirements.
 */
public class User {
    private final String username;
    private final String password;
    private int failedAttempts;
    private long blockedUntil;

    public User(String username, String password) {
        if (username == null || !isValidEmail(username)) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        if (username.length() > 50) {
            throw new IllegalArgumentException("Username is too long, try something shorter");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Your password is too short, add more characters");
        }

        if (password.length() > 12) {
            throw new IllegalArgumentException("Your password is too long, try a shorter one");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Please enter a valid password");
        }

        this.username = username;
        this.password = password;
        this.failedAttempts = 0;
        this.blockedUntil = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public synchronized int getFailedAttempts() {
        return failedAttempts;
    }

    public synchronized void incrementFailedAttempts() {
        failedAttempts++;
    }

    public synchronized void resetFailedAttempts() {
        failedAttempts = 0;
    }

    public synchronized void blockForSeconds(int seconds) {
        blockedUntil = System.currentTimeMillis() + seconds * 1000L;
    }

    public synchronized boolean isBlocked() {
        return System.currentTimeMillis() < blockedUntil;
    }

    public synchronized long getRemainingBlockedSeconds() {
        long remainingMillis = blockedUntil - System.currentTimeMillis();
        if (remainingMillis <= 0) {
            return 0;
        }
        return (remainingMillis + 999) / 1000;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9][A-Za-z0-9.\\-]*\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

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

    @Override
    public String toString() {
        return username + " " + password;
    }
}