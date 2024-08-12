import java.io.Serializable;

//The Book class implements Serializable to allow it to be saved and loaded from a file.

class Book implements Comparable<Book>, Serializable {
	// Private fields to store the book's details
    private String title;
    private String author;
    private String isbn;
    private boolean isCheckedOut; // Indicates whether the book is checked out or not
    
    // Constructor to initialize a Book object with all the necessary details
    public Book(String title, String author, String isbn, boolean isCheckedOut) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isCheckedOut = isCheckedOut;
    }
    
    // Getter and Setter methods for the fields
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.isCheckedOut = checkedOut;
    }

    // Compare this book with another book based on ISBN for sorting purposes
    @Override
    public int compareTo(Book other) {
        return this.isbn.compareTo(other.isbn);
    }
    
    // Override toString() method to provide a string representation of the book
    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + ", isbn=" + isbn + ", isCheckedOut=" + isCheckedOut + "]";
    }
    
    // Convert the book's fields to a single string for saving to a file or database
    public String toDataString() {
        return title + ";" + author + ";" + isbn + ";" + isCheckedOut; // convert fields to a single line
    }
    
    // Create a Book object from a data string (used for loading from a file or database)
    public static Book fromDataString(String data) {
        String[] parts = data.split(";");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Data string is not properly formatted.");
        }
        String title = parts[0];
        String author = parts[1];
        String isbn = parts[2];
        boolean isCheckedOut = Boolean.parseBoolean(parts[3]);
        return new Book(title, author, isbn, isCheckedOut);// reconstruct from line
    }
}