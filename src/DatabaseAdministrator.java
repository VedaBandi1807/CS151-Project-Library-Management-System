class DatabaseAdministrator extends NormalUser {
	
	public DatabaseAdministrator(String firstName, String lastName, String email, String libraryCardNumber, String password, boolean isActive) {
        super(firstName, lastName, email, libraryCardNumber, password, isActive);
    }

	@Override
    public String getUserType() {
        return "DatabaseAdministrator";
    }

    @Override
    public String toString() {
        return "DatabaseAdministrator [firstName=" + getFirstName() + ", lastName=" + getLastName() +
               ", email=" + getEmail() + ", libraryCardNumber=" + getLibraryCardNumber() + 
               ", isActive=" + isActive() + "]";
    }

}