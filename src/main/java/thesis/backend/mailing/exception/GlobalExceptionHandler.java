package thesis.backend.mailing.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.IncorrectEmailPasswordException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.model.Response.Response;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response<Void>> handleNullPointerException(NullPointerException e) {
        log.error("Null Pointer Exception occurred: ", e);
        Response<Void> response = new Response<>("Null Pointer Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response<Void>> handleNoSuchElementException(NoSuchElementException e) {
        log.error("No such element Exception occurred: ", e);
        Response<Void> response = new Response<>("No such element exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<Response<Void>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        log.error("Array Index Out Of Bounds Exception occurred: ", e);
        Response<Void> response = new Response<>("Array Index Out Of Bounds Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal Argument Exception occurred: ", e);
        Response<Void> response = new Response<>("Illegal Argument Exception occurred", HttpStatus.BAD_REQUEST.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailTypeNotFoundException.class)
    public ResponseEntity<Response<Void>> handleEmailTypeNotFoundException(EmailTypeNotFoundException e) {
        log.warn("Email Type Not Found Exception occurred: ", e);
        Response<Void> response = new Response<>("Email Type Not Found Exception occurred", HttpStatus.BAD_REQUEST.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedEmailTemplateException.class)
    public ResponseEntity<Response<Void>> handleMalformedEmailTemplateException(MalformedEmailTemplateException e) {
        log.warn("Malformed Email Template Exception occurred: ", e);
        Response<Void> response = new Response<>("Malformed Email Template Exception occurred", HttpStatus.BAD_REQUEST.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<Response<Void>> handleSendEmailException(SendEmailException e) {
        log.warn("Send Email Exception occurred: ", e);
        Response<Void> response = new Response<>("Send Email Exception occurred", HttpStatus.BAD_REQUEST.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGenericException(Exception e) {
        log.error("An exception occurred: ", e);
        Response<Void> response = new Response<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}