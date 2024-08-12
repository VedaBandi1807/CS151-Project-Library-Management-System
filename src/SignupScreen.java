import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;


public class SignupScreen extends JDialog {

	private LibraryManagementSystemGUI mainGUI;
    private MyLibrary myLibrary;

    public SignupScreen(LibraryManagementSystemGUI parent, MyLibrary myLibrary) {
        super(parent, "Sign Up", true);
        this.mainGUI = parent;
        this.myLibrary = myLibrary;
        setLayout(new BorderLayout());
        setSize(500, 500);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Adding input fields and labels
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        JTextField firstNameField = new JTextField(20);
        panel.add(firstNameField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        JTextField lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);
        
        // Adding role selection
        gbc.gridy++;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridy++;
        String[] roles = {"Normal User", "Librarian"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        panel.add(roleComboBox, gbc);

        // Creating a Submit button and adding it to a separate panel
        gbc.gridy++;
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String selectedRole = (String) roleComboBox.getSelectedItem();

                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(SignupScreen.this,
                            "Invalid email address! Please enter a valid email.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRole.isEmpty()) {
                    JOptionPane.showMessageDialog(SignupScreen.this,
                            "Some fields are missing! Please fill them in.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Utils.validatePassword(password);
                    LibraryUser newUser;

                    if ("Librarian".equals(selectedRole)) {
                        newUser = new Librarian(firstName, lastName, email, null, password, true);
                    } else {
                        newUser = new NormalUser(firstName, lastName, email, null, password, true);
                    }
                    
                    String newLibraryCardNumber = String.valueOf(MyLibrary.DB.generateLibraryCardNumber());
                    newUser.setLibraryCardNumber(newLibraryCardNumber);
                    
                    if (MyLibrary.DB.findUserByEmail(email)!= null) {
                        JOptionPane.showMessageDialog(SignupScreen.this,
                                "User already exists. Please login or use a different email.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                    	MyLibrary.DB.addUser(newUser);
                        JOptionPane.showMessageDialog(SignupScreen.this,
                                "Signup Successful! Your library card number is: " + newLibraryCardNumber,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } catch (PasswordException ex) {
                    JOptionPane.showMessageDialog(SignupScreen.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
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
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
         
        // Adding button panel to the main panel
        add(panel, BorderLayout.CENTER);
        
        // Adding components to dialog
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Email validation method using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
}
