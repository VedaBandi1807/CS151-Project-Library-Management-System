import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class UserManager {

    public UserManager() {
    }

    // Add a new user
    public void addUser(LibraryUser user) {
        if (MyLibrary.DB.getUsers().containsKey(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        MyLibrary.DB.addUser(user);
    }

    // Method to check if a user exists by email
    public boolean userExistsByEmail(String email) {
    	return MyLibrary.DB.getUsers().values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    // Method to get a user by library card number
    public LibraryUser getUser(String cardNumber) {
    	return MyLibrary.DB.findUserByLibraryCardNumber(cardNumber);
    }

    // Remove a user
    public void removeUser(String libraryCardNumber) {
    	LibraryUser user = MyLibrary.DB.findUserByLibraryCardNumber(libraryCardNumber);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        MyLibrary.DB.removeUser(libraryCardNumber);
        MyLibrary.DB.saveLibraryData();  // to persist changes
    }

    // Enable a user's account
    public void enableUser(String libraryCardNumber) {
    	LibraryUser user = MyLibrary.DB.findUserByLibraryCardNumber(libraryCardNumber);
        if (user instanceof NormalUser) {
            ((NormalUser) user).setActive(true);
        } else {
            throw new UnsupportedOperationException("Only NormalUsers can have their accounts enabled.");
        }
    }
    
    // Disable a user's account
    public void disableUser(String libraryCardNumber) {
    	LibraryUser user = MyLibrary.DB.findUserByLibraryCardNumber(libraryCardNumber);
        if (user instanceof NormalUser) {
            ((NormalUser) user).setActive(false);
        } else {
            throw new UnsupportedOperationException("Only NormalUsers can have their accounts disabled.");
        }
    }
    
    public List<LibraryUser> getActiveUsers() {
        List<LibraryUser> activeUsers = new ArrayList<>();
        for (LibraryUser user : getUsers().values()) {
            if (user instanceof NormalUser && ((NormalUser) user).isActive()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    public List<LibraryUser> getInactiveUsers() {
        List<LibraryUser> inactiveUsers = new ArrayList<>();
        for (LibraryUser user : getUsers().values()) {
            if (user instanceof NormalUser && !((NormalUser) user).isActive()) {
                inactiveUsers.add(user);
            }
        }
        return inactiveUsers;
    }


    // Method to get a user by email
    public LibraryUser getUserByEmail(String email) {
    	return MyLibrary.DB.getUsers().values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Method to get all users
    public Map<String, LibraryUser> getUsers() {
    	return MyLibrary.DB.getUsers(); // Returns a copy of the map to prevent modification
    }
    
    
    // Method to get the list of ISBNs of books checked out by a specific user
    public List<String> getCheckedOutBooks(LibraryUser user) {
        if (user instanceof NormalUser) {
            return ((NormalUser) user).getBorrowedBooks();
        }	else if (user instanceof Librarian) {
            return ((Librarian) user).getBorrowedBooks();
        }
        return List.of(); // Return an empty list if the user is not a NormalUser
    }

    // Method to get the list of available books in the library
    public List<Book> getAvailableBooks() {
        return MyLibrary.DB.getAvailableBooks();
    }
    
    public void checkOutBook(String cardNumber, String isbn) throws IllegalArgumentException {
        LibraryUser user = MyLibrary.DB.findUserByLibraryCardNumber(cardNumber);
        Book book = MyLibrary.DB.findBookByIsbn(isbn);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        if (!book.isCheckedOut()) {
        	((NormalUser) user).getBorrowedBooks().add(isbn); // Add ISBN to user's borrowed list
            updateBookStatus(isbn, true); // Mark book as checked out
        } else {
            throw new IllegalArgumentException("Book is already checked out.");
        }
    }
    
    public void removeBookFromUserData(LibraryUser user, Book book) {
        String bookISBN = book.getIsbn();

        List<String> borrowedBooks = user.getBorrowedBooks();
        if (borrowedBooks != null && borrowedBooks.contains(bookISBN)) {
            borrowedBooks.remove(bookISBN); // Remove the book's ISBN from the list
            user.setBorrowedBooks(borrowedBooks); // Update the user's borrowed books list
            saveUserData(user); // Persist the changes
        }
    }

    public void updateBookStatus(String isbn, boolean isCheckedOut) {
        Book book = MyLibrary.DB.findBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book with this ISBN not found.");
        }
        book.setCheckedOut(isCheckedOut);
        MyLibrary.DB.saveLibraryData();  // to persist changes
    }
    
    private void saveUserData(LibraryUser user) {
        // Implement saving logic, e.g., writing to a file or updating a database
        MyLibrary.DB.updateUser(user); // Update the user's data in the database or file
    }
    

}
