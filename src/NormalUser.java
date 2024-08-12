import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.stream.Collectors;

class NormalUser implements LibraryUser, Serializable {
	private static final long serialVersionUID = 1L; 
	private String firstName;
    private String lastName;
    private String email;
    private String libraryCardNumber;
    private String password;
    private boolean isActive;
    private List<String> borrowedBooks;

    public NormalUser(String firstName, String lastName, String email, String libraryCardNumber, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.libraryCardNumber = libraryCardNumber;
        this.password = password;
        this.isActive = isActive;
        this.borrowedBooks = new ArrayList<>();
    }
    
    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public void borrowBook(Book book) throws CustomExceptions.BookNotAvailableException {
        if (borrowedBooks.size() >= 2) {
            throw new IllegalStateException("Cannot borrow more than 2 books at a time.");
        }
        if (book.isCheckedOut()) {
            throw new CustomExceptions.BookNotAvailableException("Book is already checked out.");
        }
        borrowedBooks.add(book.getIsbn());
        book.setCheckedOut(true);
        MyLibrary.DB.saveLibraryData();
    }

    @Override
    public void returnBook(Book book) {
        borrowedBooks.remove(book.getIsbn());
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
    public int compareTo(LibraryUser other) {
        return this.firstName.compareTo(other.getFirstName());
    }

    @Override
    public String getUserType() {
        return "NormalUser";
    }

    @Override
    public boolean canBorrow() {
        return borrowedBooks.size() < 2;
    }
    
    @Override
    public String getDetails() {
        return String.format("Name: %s %s, Email: %s, Library Card Number: %s, Active: %s, Borrowed Books: %s",
                             firstName, lastName, email, libraryCardNumber, isActive, String.join(", ", borrowedBooks));
    }

    @Override
    public String toString() {
        return "NormalUser [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
               ", libraryCardNumber=" + libraryCardNumber + ", isActive=" + isActive + "]";
    }
    
    // Method to get the list of books checked out by all users
    public List<Book> getCheckedOutBooks() {
        List<Book> checkedOutBooks = new ArrayList<>();
        for (LibraryUser user : MyLibrary.DB.getUsers().values()) {
            if (user instanceof NormalUser) {
            	NormalUser normalUser = (NormalUser) user;
                List<Book> books = MyLibrary.DB.getBooksByIsbns(normalUser.getBorrowedBooks());
                checkedOutBooks.addAll(books.stream()
                    .filter(Book::isCheckedOut)
                    .collect(Collectors.toList()));
            }
        }
        return checkedOutBooks;
    }
    
    // Method to convert object to data string
    public String toDataString() {
    	String booksData = String.join("|", borrowedBooks);
        return "NormalUser;" + firstName + ";" + lastName + ";" + email + ";" + libraryCardNumber + ";" +
               password + ";" + isActive + ";" + booksData;
    }
    
    // Static method to convert data string back to object
    public static NormalUser fromDataString(String data) {
        String[] parts = data.split(";");
        String firstName = parts[0];
        String lastName = parts[1];
        String email = parts[2];
        String libraryCardNumber = parts[3];
        String password = parts[4];
        boolean isActive = Boolean.parseBoolean(parts[5]);

        List<String> borrowedIsbns = new ArrayList<>();
        if (parts.length > 6 && !parts[6].isEmpty()) {
        	borrowedIsbns.addAll(List.of(parts[6].split("\\|")));
        }

        NormalUser user = new NormalUser(firstName, lastName, email, libraryCardNumber, password, isActive);
        // Convert ISBNs to Book objects
        List<Book> books = MyLibrary.DB.getBooksByIsbns(borrowedIsbns);

        // Add ISBNs of the borrowed books to the user's borrowedBooks list
        user.borrowedBooks.addAll(books.stream()
                                       .map(Book::getIsbn)  // Map Book objects to ISBNs
                                       .collect(Collectors.toList()));

        return user;
    }
}
