import java.io.Serializable;
import java.util.List;

public interface LibraryUser extends Comparable<LibraryUser>, Serializable {
	String getFirstName();
    String getLastName();
    String getEmail();
    String getLibraryCardNumber();
    String getPassword();
    boolean isActive();
    void setActive(boolean active);
    void borrowBook(Book book) throws CustomExceptions.BookNotAvailableException;
    void setBorrowedBooks(List<String> borrowedBooks);
    void setLibraryCardNumber(String libraryCardNumber);
    void returnBook(Book book);
    List<String> getBorrowedBooks();
    String getUserType();
    boolean canBorrow();
    String getDetails();
    
    default boolean isLibrarian() {
        return "Librarian".equals(getUserType());
    }
    
    String toDataString(); // Convert the object to a string for saving to a file
    static LibraryUser fromDataString(String data) {
        // Implement a basic structure, each subclass will have its own logic
        throw new UnsupportedOperationException("fromDataString() must be implemented in the subclass.");
    }

}
