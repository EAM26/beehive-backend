package nl.novi.beehivebackend.exceptions;

public class IsNotEmptyException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public IsNotEmptyException() {
        super();
    }

    public IsNotEmptyException(String message) {
        super(message);
    }
}
