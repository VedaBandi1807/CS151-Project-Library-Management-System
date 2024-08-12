import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Librarian implements LibraryUser, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String firstName;
    private String lastName;
    private String email;
    private String libraryCardNumber;
    private String password;
    private boolean isActive;
    private List<String> borrowedBooks;
    
    // Constructor to initialize a Librarian object 
    public Librarian(String firstName, String lastName, String email, String libraryCardNumber, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.libraryCardNumber = libraryCardNumber;
        this.password = password;
        this.isActive = isActive;
        this.borrowedBooks = new ArrayList<>();
    }

    // Enable a user account
    public void enableUserAccount(NormalUser user) {
    	user.setActive(true);
    	MyLibrary.DB.saveLibraryData();
   }

    // Disable a user account
    public void disableUserAccount(NormalUser user) {
        user.setActive(false);
        MyLibrary.DB.saveLibraryData();
    }
    
    @Override
    public void borrowBook(Book book) throws CustomExceptions.BookNotAvailableException {
    	// Check if the librarian has already borrowed the maximum number of books
        if (borrowedBooks.size() >= 2) {
            throw new IllegalStateException("Cannot borrow more than 2 books at a time.");
        }
        if (book.isCheckedOut()) {
            throw new CustomExceptions.BookNotAvailableException("Book is already checked out.");
        }
        // Add the book to the list of borrowed books and mark it as checked out
        borrowedBooks.add(book.getIsbn());
        book.setCheckedOut(true);
        MyLibrary.DB.saveLibraryData();
    }

    @Override
    public void returnBook(Book book) {
    	// Remove the book from the list of borrowed books and mark it as not checked out
        borrowedBooks.remove(book);
        book.setCheckedOut(false);
        MyLibrary.DB.saveLibraryData();
    }

    @Override
    public List<String> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void setBorrowedBooks(List<String> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}
    
    public void setLibraryCardNumber(String libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }
    
    @Override
    public boolean canBorrow() {
        return borrowedBooks.size() < 2;
    }

    @Override
    public String getUserType() {
        return "Librarian";
    }
    
    @Override
    public int compareTo(LibraryUser otherUser) {
        // Compare based on library card number or name, for example
        return this.libraryCardNumber.compareTo(otherUser.getLibraryCardNumber());
    }
    
    @Override
    public String getDetails() {
        return String.format("Name: %s %s, Email: %s, Library Card Number: %s, Active: %s, Borrowed Books: %s",
                             firstName, lastName, email, libraryCardNumber, isActive, String.join(", ", borrowedBooks));
    }

    @Override
    public String toString() {
        return "Librarian [firstName=" + getFirstName() + ", lastName=" + getLastName() + 
               ", email=" + getEmail() + ", libraryCardNumber=" + getLibraryCardNumber() + 
               ", isActive=" + isActive() + "]";
    }
    
    // Getters and setters for the fields
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getLibraryCardNumber() { return libraryCardNumber; }
    public String getPassword() { return password; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    // Method to get the list of books checked out by all librarians
    public List<Book> getCheckedOutBooksByLibrarians() {
        List<Book> checkedOutBooks = new ArrayList<>();
        for (LibraryUser user : MyLibrary.DB.getUsers().values()) {
            if (user instanceof Librarian) {
                Librarian librarian = (Librarian) user;
                List<String> borrowedIsbns = librarian.getBorrowedBooks(); // ISBNs as Strings
                List<Book> books = MyLibrary.DB.getBooksByIsbns(borrowedIsbns); // Retrieve books by ISBNs
                checkedOutBooks.addAll(books.stream()
                    .filter(Book::isCheckedOut)
                    .collect(Collectors.toList())); // Filter checked out books
            }
        }
        return checkedOutBooks;
    }
    
   
    
    // Method to convert object to data string
    public String toDataString() {
    	String booksData = String.join("|", borrowedBooks);
        return "Librarian;" + firstName + ";" + lastName + ";" + email + ";" + libraryCardNumber + ";" +
               password + ";" + isActive + ";" + booksData;
    }
    

    // Static method to convert data string back to object
    public static Librarian fromDataString(String data) {
        String[] parts = data.split(";");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Data string is not properly formatted.");
        }
        String firstName = parts[0];
        String lastName = parts[1];
        String email = parts[2];
        String libraryCardNumber = parts[3];
        String password = parts[4];
        boolean isActive = Boolean.parseBoolean(parts[5]);

        List<String> borrowedBooks = new ArrayList<>();
        if (parts.length > 6 && !parts[6].isEmpty()) {
            String[] booksData = parts[6].split("\\|");
            for (String bookData : booksData) {
                borrowedBooks.addAll(List.of(parts[6].split("\\|")));
            }
        }

        Librarian user = new Librarian(firstName, lastName, email, libraryCardNumber, password, isActive);
        List<Book> books = MyLibrary.DB.getBooksByIsbns(borrowedBooks);
        user.borrowedBooks.addAll(books.stream()
                .map(Book::getIsbn)  // Map Book objects to ISBNs
                .collect(Collectors.toList()));
        return user;


    }
}
