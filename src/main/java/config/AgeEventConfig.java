//package config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import repository.AgeEventDBRepository;
//import repository.AgeEventRepository;
//import service.AgeEventService;
//import validators.AgeEventValidator;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class AgeEventConfig {
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
//    public AgeEventValidator createValidator() {
//        return new AgeEventValidator();
//    }
//
//    @Bean
//    public AgeEventRepository createRepository(AgeEventValidator validator, Properties props) {
//        return new AgeEventDBRepository(validator, props);
//    }
//
//    @Bean(name = "ageEventService")
//    public AgeEventService ageEventService(AgeEventRepository repo) {
//        return new AgeEventService(repo);
//    }
//}
