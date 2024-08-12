import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryManagementSystemGUI extends JFrame {


    public LibraryManagementSystemGUI() {

        // Set up the frame
        setTitle("EloquentReads");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set icon for the JFrame
        ImageIcon frameIcon = new ImageIcon("C:\\Users\\Veda_\\eclipse_workspace\\CS151-Library_Managment_System\\src\\resources\\logo.png");
        setIconImage(frameIcon.getImage());

        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            private Image img = new ImageIcon("C:\\Users\\Veda_\\eclipse_workspace\\CS151-Library_Managment_System\\src\\resources\\backdrop1.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(mainPanel);
        mainPanel.setBackground(new Color(13, 37, 63));
        mainPanel.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to EloquentReads!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, -10, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setOpaque(false);
        topButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Removing default button styling
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        Dimension buttonSize = new Dimension(200, 50);
        Border roundedBorder = new Utils.createRoundedBorder(20);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        loginButton.setMaximumSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);
        loginButton.setBorder(roundedBorder);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open LoginScreen
                new LoginScreen(LibraryManagementSystemGUI.this, MyLibrary.DB.getUsers()).setVisible(true);
            }
        });
        topButtonPanel.add(loginButton);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        exitButton.setMaximumSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        exitButton.setBorder(roundedBorder);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        topButtonPanel.add(exitButton);

        buttonPanel.add(topButtonPanel);

        // Signup label and button
        JLabel notAUserLabel = new JLabel("Not a User? Click here to sign up");
        notAUserLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        notAUserLabel.setForeground(Color.BLACK);
        notAUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(notAUserLabel);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signupButton.setMaximumSize(buttonSize);
        signupButton.setPreferredSize(buttonSize);
        signupButton.setBorder(roundedBorder);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupScreen(LibraryManagementSystemGUI.this, MyLibrary.DB).setVisible(true);
            }
        });
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(signupButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
    	MyLibrary.DB.loadLibraryData();
        SwingUtilities.invokeLater(() -> new LibraryManagementSystemGUI().setVisible(true));
    }
}
