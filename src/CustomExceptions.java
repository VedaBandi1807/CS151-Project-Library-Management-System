public class CustomExceptions {
    public static class BookNotAvailableException extends Exception {
        public BookNotAvailableException(String message) {
            super(message);
        }
    }

    public static class OverdueBooksException extends Exception {
        public OverdueBooksException(String message) {
            super(message);
        }
    }
}
