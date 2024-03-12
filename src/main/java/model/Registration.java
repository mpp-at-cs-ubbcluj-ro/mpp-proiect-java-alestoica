package model;

public class Registration extends Entity<Long> {
    private Long idParticipant, idAgeEvent, idEmployee;

    public Registration(Long idParticipant, Long idAgeEvent, Long idEmployee) {
        this.idParticipant = idParticipant;
        this.idAgeEvent = idAgeEvent;
        this.idEmployee = idEmployee;
    }

    public Long getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(Long idParticipant) {
        this.idParticipant = idParticipant;
    }

    public Long getIdAgeEvent() {
        return idAgeEvent;
    }

    public void setIdAgeEvent(Long idAgeEvent) {
        this.idAgeEvent = idAgeEvent;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    @Override
    public String toString() {
        return "Registration { " +
                "id=" + id +
                ", idParticipant=" + idParticipant +
                ", idAgeEvent=" + idAgeEvent +
                ", idEmployee=" + idEmployee +
                " }";
    }
}
