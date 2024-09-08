package thesis.backend.mailing.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendedEmailResponse {
  @JsonProperty("from")
  private String from;
  @JsonProperty("to")
  private String to;
  @JsonProperty("subject")
  private String subject;
  @JsonProperty("content")
  private String content;
  @JsonProperty("html")
  private Boolean html;
  @JsonProperty("parameters")
  private Map<String, String> parameters;
  @JsonProperty("attachments")
  private Map<String, String> attachments;
}
