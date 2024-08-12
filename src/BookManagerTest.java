import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BookManagerTest {

    private BookManager bookManager;

    @BeforeEach
    void setUp() {
        bookManager = new BookManager();
        MyLibrary.DB.setUseInMemory(true);  // Switch to in-memory mode
        MyLibrary.DB.initializeLibraryData();
    }

    @Test
    void testAddBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", "9780134685991", false);
        bookManager.addBook(book);

        assertEquals(book, bookManager.getBook("9780134685991"));
    }

    @Test
    void testAddDuplicateBook() {
        Book book1 = new Book("Clean Code", "Robert C. Martin", "9780132350884", false);
        Book book2 = new Book("Clean Code", "Robert C. Martin", "9780132350884", false);
        bookManager.addBook(book1);

        assertThrows(IllegalArgumentException.class, () -> {
            bookManager.addBook(book2);
        });
    }

    @Test
    void testRemoveBook() {
        Book book = new Book("Test-Driven Development", "Kent Beck", "9780321146533", false);
        bookManager.addBook(book);
        bookManager.removeBook("9780321146533");

        assertNull(bookManager.getBook("9780321146533"));
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Refactoring", "Martin Fowler", "9780201485677", false);
        Book book2 = new Book("Design Patterns", "Erich Gamma", "9780201633610", false);
        bookManager.addBook(book1);
        bookManager.addBook(book2);

        List<Book> books = bookManager.getAllBooks();
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
    }
}
