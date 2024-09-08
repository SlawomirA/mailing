package thesis.backend.mailing.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import thesis.backend.mailing.dto.CreateEmailTemplateDTO;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.SendedEmailResponse;

import java.util.Map;

public interface EmailingService {

    SendedEmailResponse sendMessage(SendEmailRequest sendEmailRequest) throws EmailTypeNotFoundException, MessagingException, SendEmailException;
    EmailTemplate addEmailTemplate(EmailTemplate emailTemplate) throws MalformedEmailTemplateException;
}
