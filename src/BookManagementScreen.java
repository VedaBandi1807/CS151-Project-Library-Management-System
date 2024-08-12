import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookManagementScreen extends JFrame {

    private LibraryUser librarian;
    
    // Constructor to set up the Book Management screen
    public BookManagementScreen(JFrame parent) {
        super("Book Management");
        this.librarian = librarian;

        setSize(400,300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Create a panel to hold the welcome label and add padding
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setPreferredSize(new Dimension(400, 80)); // Adjust height as needed
        northPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Top, Left, Bottom, Right padding

        JLabel welcomeLabel = new JLabel("Book Management", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        northPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(northPanel, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // "Add Book" button
        JButton addBookButton = new JButton("Add Book");
        addBookButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        addBookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to add a book
            	showAddBookDialog();
            }
        });

        // "Remove Book" button
        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        removeBookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to remove a book
            	showRemoveBookDialog();
            }
        });
        

        // Add buttons to buttonPanel
        buttonPanel.add(addBookButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
        buttonPanel.add(removeBookButton);

        add(buttonPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void showAddBookDialog() {
        JTextField titleField = new JTextField(10);
        JTextField authorField = new JTextField(10);
        JTextField isbnField = new JTextField(10);
        JCheckBox checkedOutField = new JCheckBox("Checked Out");

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Title:"));
        myPanel.add(titleField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Author:"));
        myPanel.add(authorField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("ISBN:"));
        myPanel.add(isbnField);
        myPanel.add(checkedOutField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Enter Book Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            boolean isCheckedOut = checkedOutField.isSelected();
            
            // Call method to add book to library
            Book newBook = new Book(title, author, isbn, isCheckedOut);
            MyLibrary.DB.addBook(newBook);
        }
    }

    private void showRemoveBookDialog() {
        // Example implementation for removing a book
        JTextField isbnField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("ISBN:"));
        myPanel.add(isbnField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Enter Book ISBN to Remove", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String isbn = isbnField.getText();
            MyLibrary.DB.removeBook(isbn);
        }
    }

}
