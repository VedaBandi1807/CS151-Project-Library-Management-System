import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

public class Utils {

    public static void validatePassword(String password) throws PasswordException {
        if (password.length() < 8) {
            throw new Minimum8CharactersRequired("Password must be at least 8 characters long.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new UpperCaseCharacterMissing("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new LowerCaseCharacterMissing("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new NumberCharacterMissing("Password must contain at least one number.");
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            throw new SpecialCharacterMissing("Password must contain at least one special character.");
        }
    }

    public static String generateUsername(String firstName, String lastName) {
        String initial = firstName.charAt(0) + "" + lastName.charAt(0);
        String randomNumbers = String.format("%04d", new Random().nextInt(10000));
        return initial.toUpperCase() + "-" + randomNumbers;
    }

    
    // Custom class for rounded border for the buttons
    public static class  createRoundedBorder implements Border {
        private int radius;

        public createRoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
