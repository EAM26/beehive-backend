package nl.novi.beehivebackend.exceptions;

public class IsNotUniqueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IsNotUniqueException() {
        super();
    }

    public IsNotUniqueException(String message) {
        super(message);
    }
}

