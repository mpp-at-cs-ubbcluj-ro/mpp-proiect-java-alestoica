package dto;

import model.AgeGroup;
import model.SportsEvent;

public class AgeEventDTO {
    private Long id;
    private AgeGroup ageGroup;
    private SportsEvent sportsEvent;
    private int noParticipants;

    public AgeEventDTO(Long id, AgeGroup ageGroup, SportsEvent sportsEvent) {
        this.id = id;
        this.ageGroup = ageGroup;
        this.sportsEvent = sportsEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public SportsEvent getSportsEvent() {
        return sportsEvent;
    }

    public void setSportsEvent(SportsEvent sportsEvent) {
        this.sportsEvent = sportsEvent;
    }

    public int getNoParticipants() {
        return noParticipants;
    }

    public void setNoParticipants(int noParticipants) {
        this.noParticipants = noParticipants;
    }
}
