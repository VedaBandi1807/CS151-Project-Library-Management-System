import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDashboard extends JFrame {

	private UserManager userManager;
	private LibraryUser currentUser; // The user who is currently logged in
	private JTextPane checkedOutBooksPane;
    private JTextPane booksInLibraryPane;
    private StyledDocument checkedOutDoc;
    private StyledDocument libraryDoc;

	public UserDashboard(JFrame parent, LibraryUser user, UserManager userManager) {
		this.userManager = userManager;
		this.currentUser = user;

		setTitle("User Dashboard");
		setSize(800, 600);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		// Create header panel with name and logout button
		JPanel headerPanel = new JPanel(new BorderLayout());
		JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel userInfoLabel = new JLabel(String.format("%s %s : %s", currentUser.getFirstName(),
				currentUser.getLastName(), currentUser.getLibraryCardNumber()), SwingConstants.RIGHT);
		JButton logoutButton = new JButton("Logout");

		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Logic for logging out (return to login screen)
				dispose(); // Close the current window
				new LibraryManagementSystemGUI().setVisible(true); // Show the main GUI
			}
		});

		// Add the user info and logout button to the user panel
		userPanel.add(userInfoLabel);
		userPanel.add(logoutButton);

		// Add the user panel to the header panel on the east side
		headerPanel.add(userPanel, BorderLayout.EAST);

		add(headerPanel, BorderLayout.NORTH);

		// Create main panel for the dashboard
		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));

		// Create checked out books panel
		JPanel checkedOutPanel = new JPanel(new BorderLayout());
		JLabel checkedOutLabel = new JLabel("Checked Out Books:", SwingConstants.CENTER);
		checkedOutLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Set larger font size
		checkedOutBooksPane = new JTextPane();
        checkedOutBooksPane.setEditable(false);
        checkedOutDoc = checkedOutBooksPane.getStyledDocument();
        JScrollPane checkedOutScrollPane = new JScrollPane(checkedOutBooksPane);
		JButton checkInButton = new JButton("Check In");

		checkedOutPanel.add(checkedOutLabel, BorderLayout.NORTH);
		checkedOutPanel.add(checkedOutScrollPane, BorderLayout.CENTER);
		checkedOutPanel.add(checkInButton, BorderLayout.SOUTH);

		// Create books in library panel
		JPanel booksInLibraryPanel = new JPanel(new BorderLayout());
		JLabel booksInLibraryLabel = new JLabel("Books in Library:", SwingConstants.CENTER);
		booksInLibraryLabel.setFont(new Font("Serif", Font.BOLD, 24));
		booksInLibraryPane = new JTextPane();
        booksInLibraryPane.setEditable(false);
        libraryDoc = booksInLibraryPane.getStyledDocument();
        JScrollPane libraryScrollPane = new JScrollPane(booksInLibraryPane);
		JButton checkOutButton = new JButton("Check Out");

		booksInLibraryPanel.add(booksInLibraryLabel, BorderLayout.NORTH);
		booksInLibraryPanel.add(libraryScrollPane, BorderLayout.CENTER);
		booksInLibraryPanel.add(checkOutButton, BorderLayout.SOUTH);

		// Add both panels to the main panel
		mainPanel.add(checkedOutPanel);
		mainPanel.add(booksInLibraryPanel);
		add(mainPanel, BorderLayout.CENTER);

		// Create footer panel for sorting and searching
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JComboBox<String> sortOrderComboBox = new JComboBox<>(new String[] { "Ascending", "Descending" });
		JComboBox<String> searchByComboBox = new JComboBox<>(new String[] { "Title", "Author", "ISBN" });
		JTextField searchField = new JTextField(20);
		JButton sortButton = new JButton("Sort");
		JButton searchButton = new JButton("Search");

		footerPanel.add(new JLabel("Sort Order:"));
		footerPanel.add(sortOrderComboBox);
		footerPanel.add(new JLabel("Search/Sort By:"));
		footerPanel.add(searchByComboBox);
		footerPanel.add(sortButton);
		footerPanel.add(searchField);
		footerPanel.add(searchButton);

		add(footerPanel, BorderLayout.SOUTH);

		populateBooks();

		// Add MouseListeners to handle book clicks
        checkedOutBooksPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	highlightSelectedText(checkedOutBooksPane, e.getPoint());
            	List<String> checkedOutBooksIsbns = userManager.getCheckedOutBooks(currentUser);
                List<Book> checkedOutBooks = MyLibrary.DB.getBooksByIsbns(checkedOutBooksIsbns);
                displayBookDetails(checkedOutBooksPane, checkedOutBooks, true);
            }
        });

        booksInLibraryPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	highlightSelectedText(booksInLibraryPane, e.getPoint());
                List<Book> availableBooks = userManager.getAvailableBooks();
                displayBookDetails(booksInLibraryPane, availableBooks, false);
            }
        });

		// Inside the UserDashboard constructor

        // Check In Button ActionListener
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBookTitle = getSelectedText(checkedOutBooksPane).trim();
                if (selectedBookTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(UserDashboard.this, "Please select a book to check in.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<String> checkedOutBooksIsbns = userManager.getCheckedOutBooks(currentUser);
                List<Book> checkedOutBooks = MyLibrary.DB.getBooksByIsbns(checkedOutBooksIsbns);
                Book bookToReturn = checkedOutBooks.stream()
                        .filter(book -> book.getTitle().equals(selectedBookTitle))
                        .findFirst()
                        .orElse(null);

                if (bookToReturn != null) {
                    currentUser.returnBook(bookToReturn);
                    userManager.updateBookStatus(bookToReturn.getIsbn(), false); // Update LibraryData
                    userManager.removeBookFromUserData(currentUser, bookToReturn);
                    populateBooks(); // Refresh book lists
                    JOptionPane.showMessageDialog(UserDashboard.this, "Book checked in successfully.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(UserDashboard.this, "Selected book is not in your checked out list.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }	
            }
        });

        // Check Out Button ActionListener
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBookTitle = getSelectedText(booksInLibraryPane).trim();
                if (selectedBookTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(UserDashboard.this, "Please select a book to check out.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Book> availableBooks = userManager.getAvailableBooks();
                Book bookToBorrow = availableBooks.stream()
                        .filter(book -> book.getTitle().equals(selectedBookTitle))
                        .findFirst()
                        .orElse(null);

                if (bookToBorrow != null) {
                    try {
                        currentUser.borrowBook(bookToBorrow);
                        userManager.updateBookStatus(bookToBorrow.getIsbn(), true); // Update LibraryData
                        populateBooks(); // Refresh book lists
                        JOptionPane.showMessageDialog(UserDashboard.this, "Book checked out successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalStateException ex) {
                        JOptionPane.showMessageDialog(UserDashboard.this, ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (CustomExceptions.BookNotAvailableException ex) {
                        JOptionPane.showMessageDialog(UserDashboard.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(UserDashboard.this, "Selected book is not available.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

     // Search Button ActionListener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim().toLowerCase();
                String searchBy = (String) searchByComboBox.getSelectedItem();
                String sortOrder = (String) sortOrderComboBox.getSelectedItem();
                
                List<Book> booksToSearch = userManager.getAvailableBooks();
                
                booksToSearch.removeIf(book -> !matchesSearchCriteria(book, searchBy, searchText));

                sortBooks(booksToSearch, sortOrder, searchBy);

                updateBooksPane(booksToSearch, booksInLibraryPane);
            }
        });

        // Sort Button ActionListener
        sortButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		String sortOrder = (String) sortOrderComboBox.getSelectedItem();
                String sortBy = (String) searchByComboBox.getSelectedItem(); // Assuming sorting is based on the same criteria as searching

                // Get the current list of available books or checked out books
                List<Book> booksToSort = userManager.getAvailableBooks();
                
                // Sort books based on the selected sort order
                sortBooks(booksToSort, sortOrder, sortBy);

                updateBooksPane(booksToSort, booksInLibraryPane);
            }
        });
    }

    private void populateBooks() {
    	 // Populate books in library and checked out books
        List<Book> availableBooks = userManager.getAvailableBooks();
        updateBooksPane(availableBooks, booksInLibraryPane);
        
        List<String> checkedOutBooksIsbns = userManager.getCheckedOutBooks(currentUser);
        List<Book> checkedOutBooks = MyLibrary.DB.getBooksByIsbns(checkedOutBooksIsbns);
        updateBooksPane(checkedOutBooks, checkedOutBooksPane);
    }


    private void displayBookDetails(JTextPane pane, List<Book> books	, boolean isCheckedOut) {
    	String selectedText = getSelectedText(pane).trim();
    	if (selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No book selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder details = new StringBuilder();
        for (Book book : books) {
            if (book.getTitle().equals(selectedText)) {
                details.append("Title: ").append(book.getTitle()).append("\n")
                       .append("Author: ").append(book.getAuthor()).append("\n")
                       .append("ISBN: ").append(book.getIsbn()).append("\n")
                       .append("Checked Out: ").append(isCheckedOut ? "Yes" : "No").append("\n\n");
                break;
            }
        }
        JOptionPane.showMessageDialog(this, details.toString(), "Book Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void highlightSelectedText(JTextPane textPane, Point point) {
        int pos = textPane.viewToModel(point);
        try {
            Element element = textPane.getStyledDocument().getCharacterElement(pos);
            int start = element.getStartOffset();
            int end = element.getEndOffset();
            textPane.setCaretPosition(start);
            textPane.moveCaretPosition(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSelectedText(JTextPane textPane) {        
    	return textPane.getSelectedText();
    }
    
    private void updateBooksPane(List<Book> books, JTextPane textPane) {
    	StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength()); // Clear existing text
            for (Book book : books) {
                doc.insertString(doc.getLength(), book.getTitle() + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    private void sortBooks(List<Book> books, String sortOrder, String sortBy) {
        books.sort((b1, b2) -> {
            int comparison = 0;
            if (sortBy.equals("Title")) {
                comparison = b1.getTitle().compareToIgnoreCase(b2.getTitle());
            } 
            else if (sortBy.equals("Author")) {
                comparison = (b1.getAuthor() != null ? b1.getAuthor() : "")
                        .compareToIgnoreCase(b2.getAuthor() != null ? b2.getAuthor() : "");
            }
            else if (sortBy.equals("ISBN")) {
                comparison = (b1.getIsbn() != null ? b1.getIsbn() : "")
                        .compareToIgnoreCase(b2.getIsbn() != null ? b2.getIsbn() : "");
            }
            
            return sortOrder.equals("Ascending") ? comparison : -comparison;
        });
    }

    private boolean matchesSearchCriteria(Book book, String searchBy, String searchText) {
        if (searchBy.equals("Title")) {
            return book.getTitle().toLowerCase().contains(searchText);
        } else if (searchBy.equals("Author")) {
            return book.getAuthor().toLowerCase().contains(searchText);
        } else if (searchBy.equals("ISBN")) {
            return book.getIsbn().contains(searchText);
        }
        return false;
    }
}
