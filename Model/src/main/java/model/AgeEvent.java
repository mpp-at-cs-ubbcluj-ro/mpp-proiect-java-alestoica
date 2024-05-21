package model;

public class AgeEvent extends Entity<Long> {
    private AgeGroup ageGroup;
    private SportsEvent sportsEvent;

    public AgeEvent() {
    }

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
