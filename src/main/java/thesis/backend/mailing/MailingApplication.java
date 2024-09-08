package thesis.backend.mailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class MailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailingApplication.class, args);
	}

}
