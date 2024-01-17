package nl.novi.beehivebackend.exceptions;

public class DisabledException  extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DisabledException() {
        super();
    }

    public DisabledException(String message) {
        super(message);
    }
}
