package thesis.backend.mailing.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import thesis.backend.mailing.dto.CreateEmailTemplateDTO;
import thesis.backend.mailing.dto.SendEmailDTO;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.model.MySQL.Attachment;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.SendedEmailResponse;
import thesis.backend.mailing.repository.AttachmentRepository;
import thesis.backend.mailing.repository.EmailTemplateRepository;
import thesis.backend.mailing.service.EmailingService;
import thesis.backend.mailing.validator.CreateEmailTemplateValidator;
import thesis.backend.mailing.validator.SendEmailValidator;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of EmailingService interface providing methods for JWT token generation, extraction, and validation.
 */
@Service
public class EmailingServiceImpl implements EmailingService {
//    @Value("${email.keywords.from}")
//    private String FROM_KEYWORD;

    @Value("${email.keywords.to}")
    private String TO_KEYWORD;

    @Value("${email.keywords.subject}")
    private String SUBJECT_KEYWORD;

    @Value("${email.keywords.html}")
    private String HTML_KEYWORD;
    
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String FROM_ACCOUNT;


    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    CreateEmailTemplateValidator createEmailTemplateValidator;
    @Autowired
    SendEmailValidator sendEmailValidator;

    @Override
    public EmailTemplate addEmailTemplate(EmailTemplate emailTemplate) throws MalformedEmailTemplateException {

        //Create DTO object
        CreateEmailTemplateDTO createEmailTemplateDTO = !Objects.isNull(emailTemplate)
                ? new CreateEmailTemplateDTO(emailTemplate.getName(),
                emailTemplate.getType(),
                emailTemplate.getContent(),
                emailTemplate.getCreatedAt(),
                emailTemplate.getUpdatedAt())
                : null;

        //Validate its fields against rules
        if(!createEmailTemplateValidator.validate(createEmailTemplateDTO))
            throw new MalformedEmailTemplateException(createEmailTemplateValidator.describe());

        //Create object of EmailTemplate type
        EmailTemplate emailTemplateToSave = new EmailTemplate();
        emailTemplateToSave.setName(createEmailTemplateDTO.name());
        emailTemplateToSave.setType(createEmailTemplateDTO.type());
        emailTemplateToSave.setContent(createEmailTemplateDTO.content());
        emailTemplateToSave.setCreatedAt(createEmailTemplateDTO.createdAt());
        emailTemplateToSave.setUpdatedAt(createEmailTemplateDTO.updatedAt());

        //Return saved object
        return emailTemplateRepository.saveAndFlush(emailTemplateToSave);
    }

    public SendedEmailResponse sendMessage(SendEmailRequest sendEmailRequest) throws EmailTypeNotFoundException, MessagingException, SendEmailException {
        //Create DTO object
        SendEmailDTO sendEmailDTO = !Objects.isNull(sendEmailRequest)
                ? new SendEmailDTO(sendEmailRequest.getType(),
                sendEmailRequest.getParameters(),
                sendEmailRequest.getAttachments())
                : null;

        //Validate its fields against rules
        if(!sendEmailValidator.validate(sendEmailDTO))
            throw new SendEmailException(sendEmailValidator.describe());

        //Look for email template
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findByType(sendEmailDTO.type());
        if(emailTemplate.isEmpty())
            throw new EmailTypeNotFoundException("Email template not found: "+sendEmailDTO.type());

        //Send message
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom(sendEmailDTO.parameters().get( FROM_KEYWORD));

        Map<String, String> parameters = sendEmailDTO.parameters();
        String recipient = parameters.get(TO_KEYWORD);
        String subject = parameters.get(SUBJECT_KEYWORD);
        boolean html = Boolean.parseBoolean(parameters.get(HTML_KEYWORD));
        Map<String, String> attachments = sendEmailDTO.attachments();

        helper.setTo(recipient);
        helper.setSubject(subject);
        String content = processEmailContent(parameters, emailTemplate.get().getContent());
        helper.setText(content,
                html);

        addAttachments(attachments, helper);
        emailSender.send(message);
        return new SendedEmailResponse(FROM_ACCOUNT,
                recipient,
                subject,
                content,
                html,
                parameters,
                attachments);
    }


    private void addAttachments(Map<String, String> attachments, MimeMessageHelper helper) {
        if(!attachments.isEmpty()) {
            attachments.forEach((key, value) -> {
                Optional<Attachment> attachment = attachmentRepository.findByName(key);

                if (attachment.isPresent()) {
                    try {
                        helper.addAttachment(attachment.get().getName(), new ByteArrayResource(attachment.get().getContent()));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private static String processEmailContent(Map<String, String> parameters, String emailTemplateContent) {
        final String[] processedContent = {emailTemplateContent};
        parameters.forEach((key, value) -> {
            processedContent[0] = processedContent[0].replace("{{" + key + "}}", value);
        });
        return processedContent[0];
    }
}
