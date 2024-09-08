package thesis.backend.mailing.exception.AuthenticationExceptions;


import java.io.IOException;

public class IncorrectEmailPasswordException extends IOException {
    public IncorrectEmailPasswordException(String message) {
        super(message);
    }
}
