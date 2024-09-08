package thesis.backend.mailing.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import thesis.backend.mailing.dto.CreateEmailTemplateDTO;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class CreateEmailTemplateValidator implements Validator<CreateEmailTemplateDTO> {
     public static final String EMAIL_TEMPLATE_CONTENT_CANNOT_BE_NULL = "Email template content cannot be null  ";
    public static final String EMAIL_TEMPLATE_NAME_MUST_BE_LONGER_THAN_4_LETTERS = "Email template name must be longer than 4 letters  ";
    public static final String EMAIL_TEMPLATE_TYPE_CANNOT_BE_NULL = "Email template type cannot be null  ";
    public static final String EMAIL_TEMPLATE_CANNOT_BE_NULL = "Email template cannot be null  ";

    private final List<String> validationErrors = new ArrayList<>();


    private Optional<String> validateEmailTemplateIsNotNull(CreateEmailTemplateDTO emailTemplate) {
        return Objects.isNull(emailTemplate) ? Optional.of(EMAIL_TEMPLATE_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateEmailTemplateTypeIsNotNull(CreateEmailTemplateDTO emailTemplate) {
        return Objects.isNull(emailTemplate.type()) ? Optional.of(EMAIL_TEMPLATE_TYPE_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateEmailTemplateNameIsLongerThan4Letters(CreateEmailTemplateDTO emailTemplate) {
        return emailTemplate.name().length() <= 4 ? Optional.of(EMAIL_TEMPLATE_NAME_MUST_BE_LONGER_THAN_4_LETTERS) : Optional.empty();
    }

    private Optional<String> validateEmailTemplateContentIsNotNull(CreateEmailTemplateDTO emailTemplate) {
        return Objects.isNull(emailTemplate.content()) ? Optional.of(EMAIL_TEMPLATE_CONTENT_CANNOT_BE_NULL) : Optional.empty();
    }


    @Override
    public boolean validate(CreateEmailTemplateDTO emailTemplate) {
        validationErrors.clear();

        validateEmailTemplateIsNotNull(emailTemplate).ifPresent(validationErrors::add);
        if(!validationErrors.isEmpty())
            return false;
        validateEmailTemplateTypeIsNotNull(emailTemplate).ifPresent(validationErrors::add);
        validateEmailTemplateNameIsLongerThan4Letters(emailTemplate).ifPresent(validationErrors::add);
        validateEmailTemplateContentIsNotNull(emailTemplate).ifPresent(validationErrors::add);

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
