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
public class ViewNameValidator implements Validator<String> {

    public static final String VIEW_NAME_CANNOT_BE_NULL = "View Name cannot be null  ";
    public static final String VIEW_NAME_CANNOT_BE_EMPTY = "View Name cannot be empty  ";
    public static final String VIEW_NAME_HAS_TO_CONTAIN_ONLY_LETTERS_AND_UNDERSCORES = "View Name has to contain only letters and underscores  ";
    public static final String VIEW_NAME_HAS_TO_START_WITH_RAP_AND_END_WITH_V = "View Name has to start with 'rap_' and end with '_v'  ";
    public static final String VIEW_NAME_CAN_NOT_BE_TOO_LONG_60 = "View Name can not be too long (<60)  ";
    private final List<String> validationErrors = new ArrayList<>();


    private Optional<String> validateViewNameIsNotNull(String viewName) {
        return Objects.isNull(viewName) ? Optional.of(VIEW_NAME_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateViewNameIsNotEmpty(String viewName) {
        return viewName.isEmpty() ? Optional.of(VIEW_NAME_CANNOT_BE_EMPTY) : Optional.empty();
    }

    private Optional<String> validateViewNameContainsOnlyLettersAndUnderscores(String viewName) {
        return !viewName.matches("^[a-zA-Z_]+$") ? Optional.of(VIEW_NAME_HAS_TO_CONTAIN_ONLY_LETTERS_AND_UNDERSCORES) : Optional.empty();
    }

    private Optional<String> validateViewNameMatchesReportsPattern(String viewName) {
        return viewName.startsWith("rap_v") && viewName.endsWith("_v")  ? Optional.of(VIEW_NAME_HAS_TO_START_WITH_RAP_AND_END_WITH_V) : Optional.empty();
    }

    private Optional<String> validateViewNameIsNotTooLong(String viewName) {
        return viewName.replace("rap_v","").replace("_v","").length() > 60  ? Optional.of(VIEW_NAME_CAN_NOT_BE_TOO_LONG_60) : Optional.empty();
    }



    @Override
    public boolean validate(String viewName) {
        validationErrors.clear();

        validateViewNameIsNotNull(viewName).ifPresent(validationErrors::add);
        if(!validationErrors.isEmpty())
            return false;
        validateViewNameIsNotEmpty(viewName).ifPresent(validationErrors::add);
        validateViewNameContainsOnlyLettersAndUnderscores(viewName).ifPresent(validationErrors::add);
        validateViewNameMatchesReportsPattern(viewName).ifPresent(validationErrors::add);
        validateViewNameIsNotTooLong(viewName).ifPresent(validationErrors::add);

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
