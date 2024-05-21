//package rest.services;
//
//import model.AgeEvent;
//import model.Participant;
//import model.Registration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import repository.RegistrationRepository;
//
//import java.util.Collection;
//
//@RestController
//@RequestMapping("/competition/registrations")
//public class RegistrationController {
//    @Autowired
//    private RegistrationRepository registrationRepository;
//
//    @RequestMapping(value = "/by-age-event/{id}", method = RequestMethod.GET)
//    public Collection<Registration> findByAgeEvent(@RequestBody AgeEvent ageEvent) {
//        return registrationRepository.findByAgeEvent(ageEvent);
//    }
//
//    @RequestMapping(value = "/by-participant/{id}", method = RequestMethod.GET)
//    public Collection<Registration> findByParticipant(@RequestBody Participant participant) {
//        return registrationRepository.findByParticipant(participant);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<?> findOne(@PathVariable Long id) {
//        Registration registration = registrationRepository.findOne(id);
//
//        if (registration == null)
//            return new ResponseEntity<String>("Registration not found", HttpStatus.NOT_FOUND);
//        else
//            return new ResponseEntity<Registration>(registration, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public Collection<Registration> getAll(){
//        return registrationRepository.getAll();
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public Registration add(@RequestBody Registration registration){
//        registrationRepository.add(registration);
//        return registration;
//    }
//
//    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> delete(@PathVariable Long id){
//        try {
//            registrationRepository.delete(id);
//            return new ResponseEntity<AgeEvent>(HttpStatus.OK);
//        }catch (Exception ex){
//            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//    public Registration update(@RequestBody Registration registration) {
//        registrationRepository.update(registration.getId(), registration);
//        return registration;
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String eventError(Exception e) {
//        return e.getMessage();
//    }
//}
