package thesis.backend.mailing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.Response;
import thesis.backend.mailing.model.Response.SendedEmailResponse;
import thesis.backend.mailing.service.EmailingService;
import thesis.backend.mailing.utils.Consts;

import java.util.ArrayList;


@Slf4j
@RestController
@RequestMapping("authenticationController")
@Scope("request")
public class EmailController {

    @Autowired
    EmailingService emailingService;


    @PostMapping(value = "/sendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send email.",
            description = "Send email with chosen template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<SendedEmailResponse>> sendEmail(@RequestBody SendEmailRequest sendEmailRequest) throws EmailTypeNotFoundException, MessagingException, SendEmailException {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        emailingService.sendMessage(sendEmailRequest)));
    }


    @PostMapping(value = "/addEmailTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add email template.",
            description = "Add email template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<?>> addEmailTemplate(@RequestBody EmailTemplate emailTemplate) throws MalformedEmailTemplateException {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        emailingService.addEmailTemplate(emailTemplate)));
    }


}
