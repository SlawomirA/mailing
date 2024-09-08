package thesis.backend.mailing.exception.AuthenticationExceptions;


import java.io.IOException;

public class EmailTypeNotFoundException extends IOException {
    public EmailTypeNotFoundException(String message) {
        super(message);
    }
}
