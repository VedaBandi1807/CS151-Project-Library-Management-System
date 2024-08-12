import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map	;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class UserManagementScreen extends JFrame {

	private UserManager userManager;
	private LibraryUser currentUser; // The user who is currently logged in
	private JTextPane inactiveUsersPane;
	private JTextPane activeUsersPane;
	private StyledDocument inactiveUsersDoc;
	private StyledDocument activeUsersDoc;
	private Map<String, String> nameToCardNumberMap = new HashMap<>();

	public UserManagementScreen(JFrame parent, LibraryUser user, UserManager userManager) {
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
		JPanel inactiveUsersPanel = new JPanel(new BorderLayout());
		JLabel inactiveUsersLabel = new JLabel("Library Inactive Users:", SwingConstants.CENTER);
		inactiveUsersLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Set larger font size
		inactiveUsersPane = new JTextPane();
		inactiveUsersPane.setEditable(false);
		inactiveUsersDoc = inactiveUsersPane.getStyledDocument();
		JScrollPane inactiveUsersScrollPane  = new JScrollPane(inactiveUsersPane);
		JButton reactivateButton = new JButton("Re-activate");

		inactiveUsersPanel.add(inactiveUsersLabel, BorderLayout.NORTH);
		inactiveUsersPanel.add(inactiveUsersScrollPane, BorderLayout.CENTER);
		inactiveUsersPanel.add(reactivateButton, BorderLayout.SOUTH);

		// Create books in library panel
		JPanel activeUsersPanel = new JPanel(new BorderLayout());
		JLabel activeUsersLabel = new JLabel("Library Active Users:", SwingConstants.CENTER);
		activeUsersLabel.setFont(new Font("Serif", Font.BOLD, 24));
		activeUsersPane = new JTextPane();
		activeUsersPane.setEditable(false);
		activeUsersDoc = activeUsersPane.getStyledDocument();
		JScrollPane activeUsersScrollPane = new JScrollPane(activeUsersPane);
		JButton inactivateButton = new JButton("Inactivate");

		activeUsersPanel.add(activeUsersLabel, BorderLayout.NORTH);
		activeUsersPanel.add(activeUsersScrollPane, BorderLayout.CENTER);
		activeUsersPanel.add(inactivateButton, BorderLayout.SOUTH);

		// Add both panels to the main panel
		mainPanel.add(inactiveUsersPanel);
		mainPanel.add(activeUsersPanel);
		add(mainPanel, BorderLayout.CENTER);

		// Create footer panel for sorting and searching
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JComboBox<String> sortOrderComboBox = new JComboBox<>(new String[] { "Ascending", "Descending" });
		JComboBox<String> searchByComboBox = new JComboBox<>(new String[] { "FirstName", "LastName", "LibraryCardNumber" });
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

		populateUsers();

		// Add MouseListeners to handle book clicks
		inactiveUsersPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				highlightSelectedText(inactiveUsersPane, e.getPoint());
				List<LibraryUser> inactiveUsers = userManager.getInactiveUsers();
				displayUserDetails(inactiveUsersPane, inactiveUsers);
			}
		});

		activeUsersPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				highlightSelectedText(activeUsersPane, e.getPoint());
				List<LibraryUser> activeUsers = userManager.getActiveUsers();
				displayUserDetails(activeUsersPane, activeUsers);
			}
		});

		// Inside the UserDashboard constructor

		// Check In Button ActionListener
		reactivateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFullName = getSelectedText(inactiveUsersPane);
				System.out.println(selectedFullName);
				String selectedUserCardNumber = nameToCardNumberMap.get(selectedFullName.trim());
				System.out.println(selectedUserCardNumber);
				
				if (selectedUserCardNumber == null || selectedUserCardNumber.isEmpty()) {
		            JOptionPane.showMessageDialog(UserManagementScreen.this, "Please select a user to reactivate.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            return;
		        }

				LibraryUser userToInactivate = userManager.getUser(selectedUserCardNumber);
                if (userToInactivate != null) {
                    userManager.enableUser(selectedUserCardNumber);
                    populateUsers(); // Refresh user lists
                    JOptionPane.showMessageDialog(UserManagementScreen.this, "User reactivated successfully.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(UserManagementScreen.this, "Selected user is not in the list.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
			}
		});

		// Check Out Button ActionListener
		inactivateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFullName = getSelectedText(activeUsersPane);
				System.out.println(selectedFullName);
				System.out.println(selectedFullName.trim());
				String selectedUserCardNumber = nameToCardNumberMap.get(selectedFullName.trim());
				System.out.println(selectedUserCardNumber);
				System.out.println(nameToCardNumberMap.get("Varun, Valiveti"));
				System.out.println("nameToCardNumberMap contents:");
			    for (Map.Entry<String, String> entry : nameToCardNumberMap.entrySet()) {
			        String name = entry.getKey();
			        String cardNumber = entry.getValue();
			        System.out.println("Name: " + name + ", Library Card Number: " + cardNumber);
			    }
				
				if (selectedUserCardNumber == null || selectedUserCardNumber.isEmpty()) {
		            JOptionPane.showMessageDialog(UserManagementScreen.this, "Please select a user to Inactivate.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            return;
		        }

				LibraryUser userToReactivate = userManager.getUser(selectedUserCardNumber);
                if (userToReactivate != null) {
                    userManager.disableUser(selectedUserCardNumber);
                    populateUsers(); // Refresh user lists
                    JOptionPane.showMessageDialog(UserManagementScreen.this, "User inactivated successfully.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(UserManagementScreen.this, "Selected user is not in the list.", "Error",
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

				List<LibraryUser> usersToSearch = new ArrayList<>(userManager.getUsers().values());
				usersToSearch.removeIf(user -> user instanceof Librarian);

				usersToSearch.removeIf(user -> !matchesSearchCriteria(user, searchBy, searchText));

				sortUsers(usersToSearch, sortOrder, searchBy);

				updateUsersPane(usersToSearch,activeUsersPane);
			}
		});

		// Sort Button ActionListener
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sortOrder = (String) sortOrderComboBox.getSelectedItem();
				String sortBy = (String) searchByComboBox.getSelectedItem(); // Assuming sorting is based on the same
																				// criteria as searching

				// Get the current list of active or inactive users
                List<LibraryUser> usersToSort = new ArrayList<>(userManager.getUsers().values());
                usersToSort.removeIf(user -> user instanceof Librarian);

                // Sort users based on the selected sort order
                sortUsers(usersToSort, sortOrder, sortBy);

                updateUsersPane(usersToSort, activeUsersPane);
			}
		});
	}

	private void populateUsers() {
		// Populate active and inactive users
        List<LibraryUser> activeUsers = new ArrayList<>();
        List<LibraryUser> inactiveUsers = new ArrayList<>();
        Map<String, LibraryUser> users = userManager.getUsers();

        for (LibraryUser user : users.values()) {
            if (user instanceof NormalUser) {
                if (((NormalUser) user).isActive()) {
                    activeUsers.add(user);
                    nameToCardNumberMap.put(user.getFirstName() + ", " + user.getLastName(), user.getLibraryCardNumber());
                } else {
                    inactiveUsers.add(user);
                    nameToCardNumberMap.put(user.getFirstName() + ", " + user.getLastName(), user.getLibraryCardNumber());
                }
            }
        }

        updateUsersPane(activeUsers, activeUsersPane);
        updateUsersPane(inactiveUsers, inactiveUsersPane);
    }

	private void displayUserDetails(JTextPane pane, List<LibraryUser> users) {
		try {
	        String selectedText = getSelectedText(pane).trim();
	        for (LibraryUser user : users) {
	            String userText = user.getFirstName() + ", " + user.getLastName();
	            if (userText.equals(selectedText)) {
	                String details = String.format("Name: %s %s\nLibrary Card Number: %s\nEmail: %s\nActive: %s\nPassword: %s\nBorrowed Books: %s",
	                        user.getFirstName(), user.getLastName(),
	                        user.getLibraryCardNumber(), user.getEmail(),
	                        user.isActive(), user.getPassword(),
	                        String.join(", ", user.getBorrowedBooks()));
	                JOptionPane.showMessageDialog(this, details, "User Details", JOptionPane.INFORMATION_MESSAGE);
	                return;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

	
	private void updateUsersPane(List<LibraryUser> users, JTextPane textPane) {
    	StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength()); // Clear existing text
            for (LibraryUser user : users) {
            	String fullname = user.getFirstName()+ ", " + user.getLastName();
                doc.insertString(doc.getLength(), fullname + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
	

    private boolean matchesSearchCriteria(LibraryUser user, String searchBy, String searchText) {
        switch (searchBy) {
            case "FirstName":
                return user.getFirstName().toLowerCase().contains(searchText);
            case "LastName":
                return user.getLastName().toLowerCase().contains(searchText);
            case "LibraryCardNumber":
                return user.getLibraryCardNumber().toLowerCase().contains(searchText);
            default:
                return false;
        }
    }

    private void sortUsers(List<LibraryUser> users, String sortOrder, String sortBy) {
        users.sort((u1, u2) -> {
            int comparison = 0;
            if (sortBy.equals("FirstName")) {
                comparison = u1.getFirstName().compareToIgnoreCase(u2.getFirstName());
            } 
            else if (sortBy.equals("LastName")) {
            	comparison = u1.getLastName().compareTo(u2.getLastName());
            }
            else if (sortBy.equals("LibraryCardNumber")) {
            	comparison = u1.getLibraryCardNumber().compareTo(u2.getLibraryCardNumber());
            }
          
            return sortOrder.equals("Ascending") ? comparison : -comparison;
        });
    }


    private String getSelectedText(JTextPane pane) {
    	System.out.println(pane);
    	return pane.getSelectedText();
    }

    private void highlightSelectedText(JTextPane pane, Point point) {
    	int pos = pane.viewToModel(point);
        try {
        	Element element = pane.getStyledDocument().getCharacterElement(pos);
            int start = element.getStartOffset();
            int end = element.getEndOffset();
            pane.setCaretPosition(start);
            pane.moveCaretPosition(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
