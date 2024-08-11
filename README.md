# CS151-Project-Library-Management-System
Library Management System Using Java GUI

Application name: EloquentReads Library Management System
Developed by: Veda Sahithi Bandi – 017506801
Introduction:
The Library Management System (LMS) is a Java-based application designed to manage library operations efficiently. The system provides a graphical user interface (GUI) for users to interact with the library's functionalities. It allows librarians to manage books and user accounts, while regular users can borrow and return books. User and book data are stored in a text file and the application also supports different types of users, including librarians and regular users each with specific roles and access levels.
Key Features:
•	User Management: Librarians can activate or deactivate user accounts.
•	Book Management: Librarians can add and remove book information.
•	Borrow and Return Books: Users and Librarians can borrow and return books with constraints on the number of books borrowed.
•	Search and Sort: Search and sort functionality for books and users.
•	User Registration: Users can sign up by providing their first name, last name, email, password. There is an option for users to sign up either as a normal user or a librarian.
•	User Login: Registered users can log in with their library card number and password. 
•	User Persistence: User and book data is stored in a text file (`libraryData.txt`) for persistence.
Prerequisites: 
•	Java Development Kit (JDK) 8 or higher  
•	An IDE or text editor for Java development (e.g., IntelliJ IDEA, Eclipse) 
•	A terminal or command prompt
•	Ensure that the image path is correct in the MyLibrary enum class and LibraryManagementSystemGUI class (I have provided the images in the src → resources folder.)
Instructions to run the program:
1.	Clone the Repository containing the project code to your local machine.

 

2.	Ensure that the image path is correct in the LibraryManagementSystemGUI class (I have provided the images in the src → resources folder.)
3.	Compile the program:
a.	Open your terminal or command prompt.
b.	Compile the files using the following command:
 
o	-d bin: Specifies the output directory for compiled classes.
o	src/*.java: Indicates the source files to be compiled.
4.	Run the program:
a.	After successful compilation, navigate to the bin directory:
b.	Run the main class using the following command:
 
Project Structure
The project is organized into the following structure:
•	src/: Contains all Java source files. Below are the few main java files
o	LibraryManagementSystemGUI.java: Main class to launch the GUI application.
o	BookManager.java: Manages book-related operations.
o	Librarian.java: Defines the Librarian user and their functionalities.
o	NormalUser.java: Defines the Normal user and their functionalities.
o	Book.java: Represents a book in the library.
o	UserManager.java: Manages user-related operations.
o	MyLibrary.java: Manages the database and library operations.
•	bin/: Directory for compiled classes.
•	README.md: This file.
How the Program Works
GUI Overview
The application uses Java Swing to create a user-friendly graphical interface. The main GUI consists of:
1.	Welcome Screen: The entry point of the application, providing options to log in or sign up.
2.	Login Screen: Allows users to log in using their library card number and password.
3.	Signup Screen: Enables new users to register by providing their details.
4.	Librarian Dashboard: For librarians to login as a user or manage books and user accounts.
5.	User Dashboard: For regular users to manage borrowed books and view library information.
Functionalities
•	User Management:
o	Activate/Deactivate Accounts: Librarians can enable or disable user accounts.
o	View User Details: Librarians can view details of all users.
o	Search and Sort Users: Search for books by name and sort them by name, library card number.
•	Book Management:
o	Add/Remove Books: Librarians can add new books or remove existing ones.
•	Borrowing and Returning Books:
o	Borrow Books: Users can borrow books, with a maximum limit of two books at a time.
o	Return Books: Users can return borrowed books to the library.
o	View Book Details: Users can view details of all the available books.
o	Search and Sort Books: Search for books by title or author or ISBN and sort them.



