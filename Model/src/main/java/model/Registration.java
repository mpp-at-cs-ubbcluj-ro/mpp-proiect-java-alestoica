package model;

public class Registration extends Entity<Long> {
    private Participant participant;
    private AgeEvent ageEvent;
    private Employee employee;

    public Registration(Participant participant, AgeEvent ageEvent, Employee employee) {
        this.participant = participant;
        this.ageEvent = ageEvent;
        this.employee = employee;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public AgeEvent getAgeEvent() {
        return ageEvent;
    }

    public void setAgeEvent(AgeEvent ageEvent) {
        this.ageEvent = ageEvent;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Registration { " +
                "id=" + id +
                ", idParticipant=" + participant +
                ", idAgeEvent=" + ageEvent +
                ", idEmployee=" + employee +
                " }";
    }
}
