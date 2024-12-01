package exception;

public class DuplicateStudentIDException extends Exception {
    public DuplicateStudentIDException(String message) {
        super(message);
    }
}
