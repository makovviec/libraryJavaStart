package library.Exception;

public class NoSuchFileTypeException extends RuntimeException {
    public NoSuchFileTypeException(String message) {
        super(message);
    }
}
