package hibernate;

import jakarta.persistence.*;
import model.Participant;
import hibernate.AgeEvent;
import hibernate.Employee;

@Entity
@Table(name = "registrations")
public class Registration extends model.Entity<Long> {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participant", nullable = false)
    private Participant participant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_age_event", nullable = false)
    private AgeEvent ageEvent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    public Registration() {}

    public Registration(Participant participant, AgeEvent ageEvent, Employee employee) {
        this.participant = participant;
        this.ageEvent = ageEvent;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                "id=" + getId() +
                ", participant=" + participant +
                ", ageEvent=" + ageEvent +
                ", employee=" + employee +
                " }";
    }
}
