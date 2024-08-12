import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MyLibrary {
	DB; // singleton design

	// other useful fields here
	private List<Book> books;
	private Map<String, LibraryUser> users;

	private static final int STARTING_CARD_NUMBER = 12345;
	private boolean useInMemory = false;

	private MyLibrary() { // must be private
		books = new ArrayList<>();
		users = new HashMap<>();
	}

	public void setUseInMemory(boolean useInMemory) {
		this.useInMemory = useInMemory;
	}

	public void initializeLibraryData() {
		if (!useInMemory) {
			loadLibraryData(); // Load data from file if not in-memory mode
		}
	}

	public static synchronized int generateLibraryCardNumber() {
		if (DB.getUsers().isEmpty()) {
			// No users, start from the initial value
			return STARTING_CARD_NUMBER;
		} else {
			// Get the highest library card number from existing users
			int highestCardNumber = DB.getUsers().keySet().stream().mapToInt(Integer::parseInt).max()
					.orElse(STARTING_CARD_NUMBER - 1);
			return highestCardNumber + 1;
		}
	}

	public void addBook(Book book) {
		if (!books.contains(book)) {
			books.add(book);
		}
		saveLibraryData(); // Save changes to file
	}

	public void removeBook(String isbn) {
		books.removeIf(book -> book.getIsbn().equalsIgnoreCase(isbn));
		saveLibraryData(); // Save changes to file
	}

	public void addUser(LibraryUser user) {
		String newLibraryCardNumber = String.valueOf(generateLibraryCardNumber());
		user.setLibraryCardNumber(newLibraryCardNumber);
		users.put(newLibraryCardNumber, user);
		saveLibraryData(); // Save changes to file
	}

	public void removeUser(String libraryCardNumber) {
		users.remove(libraryCardNumber);
		saveLibraryData(); // Save changes to file
	}

	public Book findBookByTitle(String title) {
		for (Book book : books) {
			if (book.getTitle().equalsIgnoreCase(title)) {
				return book;
			}
		}
		return null;
	}

	public Book findBookByIsbn(String isbn) {
		for (Book book : books) {
			if (book.getIsbn().equalsIgnoreCase(isbn)) {
				return book;
			}
		}
		return null;
	}

	public LibraryUser findUserByLibraryCardNumber(String libraryCardNumber) {
		return users.get(libraryCardNumber);
	}

	public LibraryUser findUserByEmail(String email) {
		for (LibraryUser user : users.values()) {
			if (user.getEmail().equalsIgnoreCase(email)) {
				return user;
			}
		}
		return null;
	}

	public List<Book> getBooks() {
		return books;
	}

	public Map<String, LibraryUser> getUsers() {
		return users;
	}

	public List<Book> getCheckedOutBooksByUser(LibraryUser user) {
		List<Book> checkedOutBooks = new ArrayList<>();
		if (user instanceof NormalUser) {
			checkedOutBooks.addAll(((NormalUser) user).getCheckedOutBooks());
		}
		return checkedOutBooks;
	}

	public List<Book> getAvailableBooks() {
		List<Book> availableBooks = new ArrayList<>();
		for (Book book : books) {
			if (!book.isCheckedOut()) {
				availableBooks.add(book);
			}
		}
		return availableBooks;
	}

	public List<Book> getBooksByIsbns(List<String> isbns) {
		List<Book> result = new ArrayList<>();
		for (String isbn : isbns) {
			Book book = findBookByIsbn(isbn);
			if (book != null) {
				result.add(book);
			}
		}
		return result;
	}

	public void updateUser(LibraryUser user) {
		if (users.containsKey(user.getLibraryCardNumber())) {
			users.put(user.getLibraryCardNumber(), user); // Update the user in the map
			saveLibraryData(); // Persist the changes to file
		} else {
			throw new IllegalArgumentException("User not found.");
		}
	}

	public void saveLibraryData() {
		if (!useInMemory) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(
					"C:\\Users\\Veda_\\eclipse_workspace\\CS151-Library_Managment_System\\src\\libraryData.txt"))) {
				// Save books
				writer.write("Books:\n");
				for (Book book : books) {
					writer.write(book.toDataString() + "\n");
				}

				// Save users
				writer.write("Users:\n");
				for (LibraryUser user : users.values()) {
					writer.write(user.toDataString() + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadLibraryData() {
		if (!useInMemory) {
			File file = new File(
					"C:\\Users\\Veda_\\eclipse_workspace\\CS151-Library_Managment_System\\src\\libraryData.txt");
			if (!file.exists() || file.length() == 0) {
				System.out.println("Serialized file does not exist or is empty. Initializing new library data.");
				return;
			}

			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				boolean readingBooks = false;
				boolean readingUsers = false;

				while ((line = reader.readLine()) != null) {
					if (line.equals("Books:")) {
						readingBooks = true;
						readingUsers = false;
						continue;
					} else if (line.equals("Users:")) {
						readingBooks = false;
						readingUsers = true;
						continue;
					}

					if (readingBooks) {
						if (!line.trim().isEmpty()) {
							books.add(Book.fromDataString(line)); // Convert line to Book object
						}
					} else if (readingUsers) {
						if (!line.trim().isEmpty()) {
							String[] parts = line.split(";", 2); // Split the line into type and data
							if (parts.length < 2) {
								throw new IllegalArgumentException("User data is not properly formatted.");
							}
							String userType = parts[0];
							String userData = parts[1];

							LibraryUser user;

							switch (userType) {
							case "NormalUser":
								user = NormalUser.fromDataString(userData);
								break;
							case "Librarian":
								user = Librarian.fromDataString(userData);
								break;
							default:
								throw new IllegalArgumentException("Unknown user type in the data file.");
							}
							users.put(user.getLibraryCardNumber(), user);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Method to handle serialization
	private Object readResolve() {
		return DB; // Ensure that the enum constant is correctly resolved during deserialization
	}

}
