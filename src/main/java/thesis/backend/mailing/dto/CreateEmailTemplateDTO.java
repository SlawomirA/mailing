package thesis.backend.mailing.dto;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Map;

public record CreateEmailTemplateDTO(String name, String type, String content, Date createdAt, Date updatedAt) {
}
