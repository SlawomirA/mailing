package thesis.backend.mailing.model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Custom object for response class.
 * message - custom message returned by endpoint
 */

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    private String type;
    private Map<String, String> parameters;
    private Map<String, String> attachments;
}
