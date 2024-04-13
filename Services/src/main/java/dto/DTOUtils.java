package dto;

import model.AgeEvent;
import model.AgeGroup;
import model.Participant;
import model.SportsEvent;

public class DTOUtils {
    public static Participant getFromDTO(ParticipantDTO participantDTO) {
        Long id = participantDTO.getId();
        String firstName = participantDTO.getName().split(" ")[0];
        String lastName = participantDTO.getName().split(" ")[1];
        int age = participantDTO.getAge();
        Participant participant = new Participant(firstName, lastName, age);
        participant.setId(id);
        return participant;
    }

    public static ParticipantDTO getDTO(Participant participant) {
        Long id = participant.getId();
        String name = participant.getFirstName() + " " + participant.getLastName();
        int age = participant.getAge();
        return new ParticipantDTO(id, name, age);
    }

    public static AgeEvent getFromDTO(AgeEventDTO ageEventDTO) {
        Long id = ageEventDTO.getId();
        AgeGroup ageGroup = ageEventDTO.getAgeGroup();
        SportsEvent sportsEvent = ageEventDTO.getSportsEvent();
        AgeEvent ageEvent = new AgeEvent(ageGroup, sportsEvent);
        ageEvent.setId(id);
        return ageEvent;
    }

    public static AgeEventDTO getDTO(AgeEvent ageEvent) {
        Long id = ageEvent.getId();
        AgeGroup ageGroup = ageEvent.getAgeGroup();
        SportsEvent sportsEvent = ageEvent.getSportsEvent();
        return new AgeEventDTO(id, ageGroup, sportsEvent);
    }
}
