package thesis.backend.mailing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesis.backend.mailing.model.MySQL.Attachment;
import thesis.backend.mailing.model.MySQL.EmailTemplate;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Optional<Attachment> findByName(String filename);
}

