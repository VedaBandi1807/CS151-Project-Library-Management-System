import java.util.List;

public class BookManager {

    public BookManager() {
    }

    // Add a new book
    public void addBook(Book book) {
    	if (getBook(book.getIsbn()) != null) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }
    	// Add the new book to the library database
        MyLibrary.DB.addBook(book);
        MyLibrary.DB.saveLibraryData(); // to persist changes
    }

    // Method to get a book by ISBN
    public Book getBook(String isbn) {
    	// Search for a book with the given ISBN in the database
    	return MyLibrary.DB.getBooks().stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public void removeBook(String title) {
    	// Remove the book with the given title from the database
        MyLibrary.DB.removeBook(title);
        MyLibrary.DB.saveLibraryData(); // to persist changes
    }

    public List<Book> getAllBooks() {
    	// Return the list of all books from the database
        return MyLibrary.DB.getBooks();
    }
}
