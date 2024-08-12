import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private UserManager userManager;
    private NormalUser normalUser;
    private Librarian librarian;

    @BeforeEach
    void setUp() {
        // Initialize UserManager to use in-memory mode
        userManager = new UserManager();
        MyLibrary.DB.setUseInMemory(true); // Set to use in-memory mode to avoid file operations
        MyLibrary.DB.initializeLibraryData(); // Initialize in-memory data
        
        MyLibrary.DB.getUsers().clear();
        
        normalUser = new NormalUser("John", "Doe", "john.doe@example.com", "12345", "password", true);
        librarian = new Librarian("Jane", "Smith", "jane.smith@example.com", "12346", "password", true);
    }

    @Test
    void testAddUser() {
        userManager.addUser(normalUser);
        userManager.addUser(librarian);

        assertEquals(normalUser, userManager.getUser("12345"));
        assertEquals(librarian, userManager.getUser("12346"));
    }

    @Test
    void testRemoveUser() {
        userManager.addUser(normalUser);
        userManager.addUser(librarian);

        userManager.removeUser("12345");
        userManager.removeUser("12346");

        assertNull(userManager.getUser("12345"));
        assertNull(userManager.getUser("12346"));
    }

    @Test
    void testEnableUser() {
        userManager.addUser(normalUser);
        System.out.println(MyLibrary.DB.getUsers());
        userManager.removeUser("12345");
        System.out.println(MyLibrary.DB.getUsers());
        userManager.enableUser("12345");
        System.out.println(MyLibrary.DB.getUsers());

        assertTrue(userManager.getUser("12345").isActive());
    }

    @Test
    void testDisableUser() {
        userManager.addUser(normalUser);
        userManager.disableUser("12345");

        assertFalse(userManager.getUser("12345").isActive());
    }

    @Test
    void testGetUserNotFound() {
        userManager.addUser(normalUser);

        assertNull(userManager.getUser("67890"));
    }

    @Test
    void testGetUsers() {
        userManager.addUser(normalUser);
        userManager.addUser(librarian);

        assertEquals(2, userManager.getUsers().size());
        assertTrue(userManager.getUsers().containsValue(normalUser));
        assertTrue(userManager.getUsers().containsValue(librarian));
    }

    @Test
    void testUserManagerHandlesMultipleUsers() {
        NormalUser anotherUser = new NormalUser("Alice", "Brown", "alice.brown@example.com", "12347", "password", true);
        Librarian anotherLibrarian = new Librarian("Bob", "White", "bob.white@example.com", "12348", "password", true);

        userManager.addUser(normalUser);
        userManager.addUser(librarian);
        userManager.addUser(anotherUser);
        userManager.addUser(anotherLibrarian);

        assertEquals(normalUser, userManager.getUser("12345"));
        assertEquals(librarian, userManager.getUser("12346"));
        assertEquals(anotherUser, userManager.getUser("12347"));
        assertEquals(anotherLibrarian, userManager.getUser("12348"));
    }
}
