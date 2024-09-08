package thesis.backend.mailing.exception.AuthenticationExceptions;


import java.io.IOException;

public class SendEmailException extends IOException {
    public SendEmailException(String message) {
        super(message);
    }
}
