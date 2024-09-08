package thesis.backend.mailing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesis.backend.mailing.model.MySQL.EmailTemplate;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {
    Optional<EmailTemplate> findByType(String type);
}

