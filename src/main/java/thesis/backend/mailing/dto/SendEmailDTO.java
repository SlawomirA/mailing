package thesis.backend.mailing.dto;

import thesis.backend.mailing.model.Request.SendEmailRequest;

import java.util.Map;

public record SendEmailDTO(String type, Map<String, String> parameters, Map<String, String> attachments) {
}
