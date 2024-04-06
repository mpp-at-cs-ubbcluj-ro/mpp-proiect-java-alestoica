//package config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import repository.*;
//import service.RegistrationService;
//import validators.RegistrationValidator;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class RegistrationConfig {
//    @Bean
//    @Primary
//    public Properties getProps() {
//        Properties props = new Properties();
//
//        try {
//            props.load(new FileReader("bd.config"));
//            System.out.println("properties set. ");
//        } catch (IOException e) {
//            System.out.println("cannot find bd.config " + e);
//        }
//
//        return props;
//    }
//
//    @Bean
//    public RegistrationValidator createValidator() {
//        return new RegistrationValidator();
//    }
//
//    @Bean
//    public RegistrationRepository createRepository(RegistrationValidator validator, Properties props, ParticipantRepository participantRepository, AgeEventRepository ageEventRepository, EmployeeRepository employeeRepository) {
//        return new RegistrationDBRepository(validator, props, participantRepository, ageEventRepository, employeeRepository);
//    }
//
//    @Bean(name = "registrationService")
//    public RegistrationService registrationService(RegistrationRepository repo) {
//        return new RegistrationService(repo);
//    }
//}
