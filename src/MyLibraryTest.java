import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyLibraryTest {

    @BeforeEach
    void setUp() {
        // Configure MyLibrary to use in-memory mode for tests
        MyLibrary.DB.setUseInMemory(true);
        MyLibrary.DB.initializeLibraryData();
    }

    @Test
    void testGenerateLibraryCardNumber() {
        // Ensure that we start with an empty library (for test isolation)
        MyLibrary.DB.setUseInMemory(true); // Set to use in-memory mode to avoid file operations
        MyLibrary.DB.initializeLibraryData(); // Initialize in-memory data
        
        MyLibrary.DB.getUsers().clear();

        // Generate the first card number
        int firstCardNumber = MyLibrary.DB.generateLibraryCardNumber();
        
        // Add the first user with the generated card number
        NormalUser firstUser = new NormalUser("First", "User", "first.user@example.com", null, "password", true);
        firstUser.setLibraryCardNumber(String.valueOf(firstCardNumber));
        MyLibrary.DB.addUser(firstUser);

        // Generate the second card number
        int secondCardNumber = MyLibrary.DB.generateLibraryCardNumber();
        
        // Add the second user with the generated card number
        NormalUser secondUser = new NormalUser("Second", "User", "second.user@example.com", null, "password", true);
        secondUser.setLibraryCardNumber(String.valueOf(secondCardNumber));
        MyLibrary.DB.addUser(secondUser);

        // Assert that the numbers are sequential and correct
        assertEquals(12345, firstCardNumber, "The first card number should be 12345.");
        assertEquals(12346, secondCardNumber, "The second card number should be 12346.");
    }


    @Test
    void testAddUser() {
        NormalUser user = new NormalUser("Alice", "Wonderland", "alice.wonderland@example.com", null, "password", true);
        MyLibrary.DB.addUser(user);

        LibraryUser retrievedUser = MyLibrary.DB.findUserByLibraryCardNumber(user.getLibraryCardNumber());
        assertEquals(user, retrievedUser);
    }

    @Test
    void testRemoveUser() {
        NormalUser user = new NormalUser("Bob", "Builder", "bob.builder@example.com", null, "password", true);
        MyLibrary.DB.addUser(user);

        MyLibrary.DB.removeUser(user.getLibraryCardNumber());
        assertNull(MyLibrary.DB.findUserByLibraryCardNumber(user.getLibraryCardNumber()));
    }

    @Test
    void testAddBook() {
        Book book = new Book("The Mythical Man-Month", "Frederick P. Brooks Jr.", "9780201835953", false);
        MyLibrary.DB.addBook(book);

        Book retrievedBook = MyLibrary.DB.findBookByIsbn("9780201835953");
        assertEquals(book, retrievedBook);
    }

    @Test
    void testRemoveBook() {
        Book book = new Book("The Lean Startup", "Eric Ries", "9780307887894", false);
        MyLibrary.DB.addBook(book);

        MyLibrary.DB.removeBook("9780307887894");
        assertNull(MyLibrary.DB.findBookByIsbn("9780307887894"));
    }
}
