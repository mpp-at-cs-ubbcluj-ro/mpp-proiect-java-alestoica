package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import repository.*;
import server.Service;
import service.*;
import validators.AgeEventValidator;
import validators.EmployeeValidator;
import validators.ParticipantValidator;
import validators.RegistrationValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class Config {
    @Bean
    @Primary
    public Properties getProps() {
        Properties props = new Properties();

        try {
            props.load(Config.class.getResourceAsStream("/server.properties"));
            System.out.println("DB properties set.");
        } catch (IOException e) {
            System.out.println("Cannot find server.properties " + e);
        }

        return props;
    }

    @Bean
    public ParticipantValidator createParticipantValidator() {
        return new ParticipantValidator();
    }

    @Bean
    public ParticipantRepository createParticipantRepository(ParticipantValidator participantValidator, Properties props) {
        return new ParticipantDBRepository(participantValidator, props);
    }

    @Bean(name="participantService")
    public ParticipantService participantService(ParticipantRepository repo) {
        return new ParticipantService(repo);
    }

    @Bean
    public EmployeeValidator createEmployeeValidator() {
        return new EmployeeValidator();
    }

    @Bean
    public EmployeeRepository createEmployeeRepository(EmployeeValidator employeeValidator, Properties props) {
        return new EmployeeDBRepository(employeeValidator, props);
    }

    @Bean(name = "employeeService")
    public EmployeeService employeeService(EmployeeRepository repo) {
        return new EmployeeService(repo);
    }

    @Bean
    public AgeEventValidator createAgeEventValidator() {
        return new AgeEventValidator();
    }

    @Bean
    public AgeEventRepository createAgeEventRepository(AgeEventValidator ageEventValidator, Properties props) {
        return new AgeEventDBRepository(ageEventValidator, props);
    }

    @Bean(name = "ageEventService")
    public AgeEventService ageEventService(AgeEventRepository repo) {
        return new AgeEventService(repo);
    }

    @Bean
    public RegistrationValidator createRegistrationValidator() {
        return new RegistrationValidator();
    }

    @Bean
    public RegistrationRepository createRegistrationRepository(RegistrationValidator registrationValidator, Properties props, ParticipantRepository participantRepository, AgeEventRepository ageEventRepository, EmployeeRepository employeeRepository) {
        return new RegistrationDBRepository(registrationValidator, props, participantRepository, ageEventRepository, employeeRepository);
    }

    @Bean(name = "registrationService")
    public RegistrationService registrationService(RegistrationRepository repo) {
        return new RegistrationService(repo);
    }

    @Bean(name = "service")
    public Service service(ParticipantService participantService, EmployeeService employeeService, AgeEventService ageEventService, RegistrationService registrationService) {
        return new Service(participantService, employeeService, ageEventService, registrationService);
    }
}
