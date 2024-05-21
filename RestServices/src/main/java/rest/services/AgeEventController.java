package rest.services;

import model.AgeEvent;
import model.AgeGroup;
import model.SportsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.AgeEventRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/competition/events")
public class AgeEventController {
    @Autowired
    private AgeEventRepository ageEventRepository;

    @RequestMapping("/{id}/number-of-participants")
    public int countParticipants(@PathVariable Long id) {
        return ageEventRepository.countParticipants(id);
    }

    @RequestMapping("/{ageGroup}/{sportsEvent}")
    public ResponseEntity<?> findByAgeGroupAndSportsEvent(@PathVariable String ageGroup, @PathVariable String sportsEvent) {
        AgeEvent ageEvent = ageEventRepository.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);

        if (ageEvent == null)
            return new ResponseEntity<String>("Event not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<AgeEvent>(ageEvent, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        AgeEvent ageEvent = ageEventRepository.findOne(id);

        if (ageEvent == null)
            return new ResponseEntity<String>("Event not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<AgeEvent>(ageEvent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public AgeEvent[] getAll() {
        System.out.println("Get all participants ...");
        List<AgeEvent> ageEventList = new ArrayList<>(ageEventRepository.getAll());
        return ageEventList.toArray(new AgeEvent[ageEventList.size()]);
//        return ageEventRepository.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public AgeEvent add(@RequestBody AgeEvent ageEvent) {
        ageEventRepository.add(ageEvent);
        return ageEvent;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            ageEventRepository.delete(id);
            return new ResponseEntity<AgeEvent>(HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AgeEvent update(@RequestBody AgeEvent ageEvent) {
        ageEventRepository.update(ageEvent.getId(), ageEvent);
        return ageEvent;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String eventError(Exception e) {
        return e.getMessage();
    }
}
