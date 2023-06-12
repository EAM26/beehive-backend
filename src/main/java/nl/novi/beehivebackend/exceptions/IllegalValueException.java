package nl.novi.beehivebackend.exceptions;

public class IllegalValueException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public IllegalValueException() {
        super();
    }

    public IllegalValueException(String message) {
        super(message);
    }


}
