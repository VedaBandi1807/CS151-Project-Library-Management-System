import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginScreen extends JDialog {
	
	private Map<String, LibraryUser> users;

    public LoginScreen(JFrame parent, Map<String, LibraryUser> users) {
        super(parent, "Login", true);
        UserManager userManager = new UserManager();
        setLayout(new BorderLayout());
        setSize(300, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        
        JTextField cardNumberField = new JTextField(20);
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new DigitFilter());
        
        JPasswordField passwordField = new JPasswordField(20);

        panel.add(new JLabel("Library Card Number:"), gbc);
        gbc.gridy++;
        panel.add(cardNumberField, gbc);
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String libraryCardNumber = cardNumberField.getText();
                String password = new String(passwordField.getPassword());
                
                if (libraryCardNumber.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginScreen.this,
                            "Library card number and password are required",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LibraryUser user = users.get(libraryCardNumber);
                if (user == null) {
                    JOptionPane.showMessageDialog(parent, "Library card number does not exist.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else if (!user.isActive()) {
                    JOptionPane.showMessageDialog(parent, "This account is inactive. Please contact the library for assistance.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else if (!user.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(parent, "Invalid password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parent, "Login Successful! Welcome, " + user.getFirstName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if (user.isLibrarian()) {
                        // Librarian can choose to access UserDashboard or Librarian Dashboard
                        new LibrarianDashboard(parent, user).setVisible(true);
                    } else {
                        // Normal User gets the UserDashboard
                        new UserDashboard(parent, user, userManager).setVisible(true);
                    }
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);
    }
    
    private static class DigitFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
