package exceptions;

public class IncorrectPlayerNameException extends Exception {
    public IncorrectPlayerNameException(String violation) {
        super(violation);
    }
}
