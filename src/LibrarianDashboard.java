import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarianDashboard extends JFrame {

    private LibraryUser librarian;

    public LibrarianDashboard(JFrame parent, LibraryUser librarian) {
        super("Librarian Dashboard");
        this.librarian = librarian;
        UserManager userManager = new UserManager(); 

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Create a panel to hold the welcome label and add padding
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setPreferredSize(new Dimension(400, 80)); // Adjust height as needed
        northPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Top, Left, Bottom, Right padding

        JLabel welcomeLabel = new JLabel("Welcome, " + librarian.getFirstName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        northPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(northPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Button for managing users
        JButton manageUsersButton = new JButton("Manage Users");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        panel.add(manageUsersButton, gbc);

        // Button for managing books
        JButton manageBooksButton = new JButton("Manage Books");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        panel.add(manageBooksButton, gbc);

        // Button for login as user
        JButton loginAsUserButton = new JButton("Login as User");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(loginAsUserButton, gbc);

        // Adding action listeners
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserManagementScreen(LibrarianDashboard.this, librarian, userManager).setVisible(true);
            }
        });

        manageBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookManagementScreen(LibrarianDashboard.this).setVisible(true);
            }
        });

        loginAsUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserDashboard(LibrarianDashboard.this, librarian, userManager).setVisible(true);
            }
        });

        add(panel, BorderLayout.CENTER);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
