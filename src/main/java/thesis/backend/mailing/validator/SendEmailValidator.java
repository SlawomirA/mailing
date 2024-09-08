package thesis.backend.mailing.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thesis.backend.mailing.dto.SendEmailDTO;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.SendEmailRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class SendEmailValidator implements Validator<SendEmailDTO> {
    public static final String SEND_EMAIL_REQUEST_MUST_CONTAIN_HTML_KEYWORD = "Send Email Request parameters MUST contain HTML keyword  ";
    public static final String SEND_EMAIL_REQUEST_MUST_CONTAIN_SUBJECT_KEYWORD = "Send Email Request parameters MUST contain SUBJECT keyword  ";
    public static final String SEND_EMAIL_REQUEST_MUST_CONTAIN_TO_KEYWORD = "Send Email Request parameters MUST contain TO keyword  ";
    public static final String SEND_EMAIL_REQUEST_MUST_CONTAIN_FROM_KEYWORD = "Send Email Request parameters MUST contain FROM keyword  ";

   public static final String SEND_EMAIL_REQUEST_PARAMETERS_MUST_BE_UPPERCASE = "Send Email Request parameters must be UPPERCASE ";
   public static final String SEND_EMAIL_REQUEST_TYPE_CANNOT_BE_NULL = "Send Email Request type cannot be null  ";
    public static final String SEND_EMAIL_REQUEST_PARAMETERS_CANNOT_BE_NULL = "Send Email Request parameters cannot be null  ";
    public static final String SEND_EMAIL_REQUEST_CANNOT_BE_NULL = "Send Email Request cannot be null  ";

    private final List<String> validationErrors = new ArrayList<>();

    @Value("${email.keywords.from}")
    private String FROM_KEYWORD;

    @Value("${email.keywords.to}")
    private String TO_KEYWORD;

    @Value("${email.keywords.subject}")
    private String SUBJECT_KEYWORD;

    @Value("${email.keywords.html}")
    private String HTML_KEYWORD;


    private Optional<String> validateSendEmailIsNull(SendEmailDTO sendEmailDto) {
        return Objects.isNull(sendEmailDto) ? Optional.of(SEND_EMAIL_REQUEST_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateSendEmailParametersAreNull(SendEmailDTO sendEmailDto) {
        return Objects.isNull(sendEmailDto.parameters()) ? Optional.of(SEND_EMAIL_REQUEST_PARAMETERS_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateSendEmailTypeIsNull(SendEmailDTO sendEmailDto) {
        return Objects.isNull(sendEmailDto.type()) ? Optional.of(SEND_EMAIL_REQUEST_TYPE_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateSendEmailTypeIsEmpty(SendEmailDTO sendEmailDto) {
        return sendEmailDto.type().isEmpty() ? Optional.of(SEND_EMAIL_REQUEST_TYPE_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateSendEmailParametersAreAllUppercase(SendEmailDTO sendEmailDto) {
        return sendEmailDto.parameters().keySet().stream()
                .filter(key -> !key.equals(key.toUpperCase()))
                .findFirst()
                .map(key -> SEND_EMAIL_REQUEST_PARAMETERS_MUST_BE_UPPERCASE);
    }


    private Optional<String> validateSendEmailTOKeyword(SendEmailDTO sendEmailDto) {
        return !sendEmailDto.parameters().containsKey(TO_KEYWORD) ? Optional.of(SEND_EMAIL_REQUEST_MUST_CONTAIN_TO_KEYWORD) : Optional.empty();
    }

    private Optional<String> validateSendEmailSUBJECTKeyword(SendEmailDTO sendEmailDto) {
        return !sendEmailDto.parameters().containsKey(SUBJECT_KEYWORD) ? Optional.of(SEND_EMAIL_REQUEST_MUST_CONTAIN_SUBJECT_KEYWORD) : Optional.empty();
    }

    private Optional<String> validateSendEmailHTMLKeyword(SendEmailDTO sendEmailDto) {
        return !sendEmailDto.parameters().containsKey(HTML_KEYWORD) ? Optional.of(SEND_EMAIL_REQUEST_MUST_CONTAIN_HTML_KEYWORD) : Optional.empty();
    }

    @Override
    public boolean validate(SendEmailDTO sendEmailDTO) {
        validationErrors.clear();

        validateSendEmailIsNull(sendEmailDTO).ifPresent(validationErrors::add);
        if(!validationErrors.isEmpty())
            return false;

        validateSendEmailParametersAreNull(sendEmailDTO).ifPresent(validationErrors::add);
        validateSendEmailTypeIsNull(sendEmailDTO).ifPresent(validationErrors::add);
        if(!validationErrors.isEmpty())
            return false;

        validateSendEmailTypeIsEmpty(sendEmailDTO).ifPresent(validationErrors::add);
        validateSendEmailParametersAreAllUppercase(sendEmailDTO).ifPresent(validationErrors::add);
//        validateSendEmailFROMKeyword(sendEmailDTO).ifPresent(validationErrors::add);
        validateSendEmailTOKeyword(sendEmailDTO).ifPresent(validationErrors::add);
        validateSendEmailSUBJECTKeyword(sendEmailDTO).ifPresent(validationErrors::add);
        validateSendEmailHTMLKeyword(sendEmailDTO).ifPresent(validationErrors::add);

        return validationErrors.isEmpty();
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(sb::append);
            return sb.toString();
        }
        else
            return "No validation errors found";

    }
}
