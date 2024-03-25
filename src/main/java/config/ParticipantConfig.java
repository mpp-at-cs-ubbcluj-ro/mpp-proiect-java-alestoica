package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import repository.ParticipantDBRepository;
import repository.ParticipantRepository;
import service.ParticipantService;
import validators.ParticipantValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class ParticipantConfig {
    @Bean
    @Primary
    public Properties getProps() {
        Properties props = new Properties();

        try {
            props.load(new FileReader("bd.config"));
            System.out.println("properties set. ");
        } catch (IOException e) {
            System.out.println("cannot find bd.config " + e);
        }

        return props;
    }

    @Bean
    public ParticipantValidator createValidator() {
        return new ParticipantValidator();
    }

    @Bean
    public ParticipantRepository createRepository(ParticipantValidator validator, Properties props) {
        return new ParticipantDBRepository(validator, props);
    }

    @Bean(name="participantService")
    public ParticipantService participantService(ParticipantRepository repo) {
        return new ParticipantService(repo);
    }
}
