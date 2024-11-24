package thesis.backend.mailing.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XlsxReportResponse {
  @JsonProperty("headers")
  private List<String> headers;
  @JsonProperty("rows")
  private List<List<String>> rows;
}
