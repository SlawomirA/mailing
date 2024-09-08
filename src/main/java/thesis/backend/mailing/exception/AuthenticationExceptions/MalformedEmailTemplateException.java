package thesis.backend.mailing.exception.AuthenticationExceptions;


import java.io.IOException;

public class MalformedEmailTemplateException extends IOException {
    public MalformedEmailTemplateException(String message) {
        super(message);
    }
}
