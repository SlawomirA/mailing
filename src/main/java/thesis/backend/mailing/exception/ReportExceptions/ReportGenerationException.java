package thesis.backend.mailing.exception.ReportExceptions;


import java.io.IOException;

public class ReportGenerationException extends IOException {
    public ReportGenerationException(String message) {
        super(message);
    }
}
