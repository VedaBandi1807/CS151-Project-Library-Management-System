# Library Management System Using Java GUI

**Application name:** EloquentReads Library Management System

**Developed by:** Veda Sahithi Bandi – 017506801

## Introduction:

The **Library Management System (LMS)** is a Java-based application designed to manage library operations efficiently. The system provides a graphical user interface (GUI) for users to interact with the library's functionalities. It allows librarians to manage books and user accounts, while regular users can borrow and return books. User and book data are stored in a text file and the application also supports different types of users, including librarians and regular users each with specific roles and access levels.

## Key Features:

- *User Management*: Librarians can activate or deactivate user accounts.
* *Book Management*: Librarians can add and remove book information.
* *Borrow and Return Books*: Users and Librarians can borrow and return books with constraints on the number of books borrowed.
* *Search and Sort*: Search and sort functionality for books and users.
* *User Registration*: Users can sign up by providing their first name, last name, email, and password. There is an option for users to sign up either as a normal user or a librarian.
* *User Login*: Registered users can log in with their library card number and password.
* *User Persistence*: User and book data is stored in a text file (`libraryData.txt`) for persistence.

## Prerequisites: 
* Java Development Kit (JDK) 8 or higher
* An IDE or text editor for Java development (e.g., IntelliJ IDEA, Eclipse)
* A terminal or command prompt
* Ensure the image path is correct in the MyLibrary enum class and LibraryManagementSystemGUI class (I have provided the images in the src → resources folder.)

## Instructions to run the program:

 1.	Clone the Repository containing the project code to your local machine.
    <div align="center">
	<img src="https://github.com/user-attachments/assets/56820efd-060c-4cf7-af48-a23642e37e80">
    </div>
 2.	Ensure the image path is correct in the LibraryManagementSystemGUI class (I have provided the images in the src → resources folder.)
 3.	Compile the program:
 	* Open your terminal or command prompt.
 	* Compile the files using the following command:
  	 <div align="center">
	<img src="![image](https://github.com/user-attachments/assets/361c4285-f9b1-42bd-a5a9-2e97e2a0d01b)">
         </div>
   		- -d bin: Specifies the output directory for compiled classes.
  		- src/*.java: Indicates the source files to be compiled.
 4.	Run the program:
    * After successful compilation, navigate to the bin directory:
    * Run the main class using the following command:
  <div align="center">
	<img src="![image](https://github.com/user-attachments/assets/a059b108-64e6-4a1f-89cc-7c47de8d8b24)">
  </div>

## Project Structure

 The project is organized into the following structure:
 * src/: Contains all Java source files. Below are the few main java files
    - LibraryManagementSystemGUI.java: Main class to launch the GUI application.
    - BookManager.java: Manages book-related operations.
    - Librarian.java: Defines the Librarian user and their functionalities.
    - NormalUser.java: Defines the Normal user and their functionalities.
    - Book.java: Represents a book in the library.
    - UserManager.java: Manages user-related operations.
    - MyLibrary.java: Manages the database and library operations.
 * bin/: Directory for compiled classes.
 * README.md: This file.

## How the Program Works

### GUI Overview

The application uses Java Swing to create a user-friendly graphical interface. The main GUI consists of:
 1.	Welcome Screen: The entry point of the application, provides options to log in or sign up.
 2.	Login Screen: Allows users to log in using their library card number and password.
 3.	Signup Screen: Enables new users to register by providing their details.
 4.	Librarian Dashboard: For librarians to log in as a user or manage books and user accounts.
 5.	User Dashboard: For regular users to manage borrowed books and view library information.
Functionalities
 1. User Management:
    * Activate/Deactivate Accounts: Librarians can enable or disable user accounts.
    * View User Details: Librarians can view details of all users.
    * Search and Sort Users: Search for books by name and sort them by name, and library card number.
 2. Book Management:
    * Add/Remove Books: Librarians can add new books or remove existing ones.
 3. Borrowing and Returning Books:
    * Borrow Books: Users can borrow books, with a maximum limit of two books at a time.
    * Return Books: Users can return borrowed books to the library.
    * View Book Details: Users can view details of all the available books.
    * Search and Sort Books: Search for books by title or author or ISBN and sort them.


## Application Snapshots:

1. Home Page
   ![image](https://github.com/user-attachments/assets/754dc797-334e-46ea-b5f6-6987e356e513)
2. Signup Page
   * SignUp a Normal User
   ![image](https://github.com/user-attachments/assets/67c4e3fd-e19c-42d4-aee2-7aa7809010c9)
   ![image](https://github.com/user-attachments/assets/082d5cda-8b47-42fa-8b0d-e31c6c2e9221)
   * SignUp a Librarian
   ![image](https://github.com/user-attachments/assets/38c1699f-76e6-4af4-a9e5-7a6ba9944e83)
   ![image](https://github.com/user-attachments/assets/6f0565e7-9caf-4608-8ec6-659d9fff5f72)
3. Login Page
   ![image](https://github.com/user-attachments/assets/0e68a781-86d5-4197-aade-6dee76c58351)
   ![image](https://github.com/user-attachments/assets/b0fd72a1-e8d9-4bed-90bb-d8fea1977dbc)
4. Librarian Dashboard
   ![image](https://github.com/user-attachments/assets/2f37de4e-88eb-4e8b-a6b6-474b61a06ce5)
   * Manage Users
   * Manage Books
   * Login as a User
6. UserDashboard
7. 





