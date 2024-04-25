package hibernate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import model.AgeGroup;
import model.SportsEvent;

@Entity
@Table(name = "age_events")
public class AgeEvent extends model.Entity<Long> {
    @Id
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    private AgeGroup ageGroup;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sports_event")
    private SportsEvent sportsEvent;

    public AgeEvent() {}

    public AgeEvent(AgeGroup ageGroup, SportsEvent sportsEvent) {
        this.ageGroup = ageGroup;
        this.sportsEvent = sportsEvent;
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

    @Override
    public String toString() {
        return "AgeEvent { " +
                "id=" + id +
                ", ageGroup=" + ageGroup +
                ", sportsEvent=" + sportsEvent +
                " }";
    }
}
