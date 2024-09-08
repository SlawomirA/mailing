package thesis.backend.mailing.validator;

import lombok.extern.slf4j.Slf4j;

public interface Validator<T> {
    public boolean validate(T object);
    public String describe();
}
