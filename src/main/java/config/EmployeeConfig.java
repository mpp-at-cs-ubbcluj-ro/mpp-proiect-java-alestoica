//package config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import repository.EmployeeDBRepository;
//import repository.EmployeeRepository;
//import service.EmployeeService;
//import validators.EmployeeValidator;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class EmployeeConfig {
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
//    public EmployeeValidator createValidator() {
//        return new EmployeeValidator();
//    }
//
//    @Bean
//    public EmployeeRepository createRepository(EmployeeValidator validator, Properties props) {
//        return new EmployeeDBRepository(validator, props);
//    }
//
//    @Bean(name = "employeeService")
//    public EmployeeService employeeService(EmployeeRepository repo) {
//        return new EmployeeService(repo);
//    }
//}
