import model.*;

public class Main {
    public static void main(String[] args) {
        Employee employee = new Employee("Gigel", "Marin", "gigelmarin_", "123456gigel");
        Long idEmployee = 23456L;
        employee.setId(idEmployee);
        System.out.println(employee);

        Participant participant = new Participant("Dorel", "Lupu", 10);
        Long idParticipant = 34567L;
        participant.setId(idParticipant);
        System.out.println(participant);

        AgeEvent ageEvent1 = null;
        AgeEvent ageEvent2 = null;
        if (participant.getAge() >= 6 && participant.getAge() <= 8) {
            ageEvent1 = new AgeEvent(AgeGroup.group_6_8_years, SportsEvent.meters_50);
            ageEvent2 = new AgeEvent(AgeGroup.group_6_8_years, SportsEvent.meters_100);
        } else if (participant.getAge() >= 9 && participant.getAge() <= 11) {
            ageEvent1 = new AgeEvent(AgeGroup.group_9_11_years, SportsEvent.meters_100);
            ageEvent2 = new AgeEvent(AgeGroup.group_9_11_years, SportsEvent.meters_1000);
        } else if (participant.getAge() >= 12 && participant.getAge() <= 15) {
            ageEvent1 = new AgeEvent(AgeGroup.group_12_15_years, SportsEvent.meters_1000);
            ageEvent2 = new AgeEvent(AgeGroup.group_12_15_years, SportsEvent.meters_1500);
        }
        Long idAgeEvent1 = 45678L;
        ageEvent1.setId(idAgeEvent1);
        Long idAgeEvent2 = 56789L;
        ageEvent2.setId(idAgeEvent2);
        System.out.println(ageEvent1);

        Registration registration = new Registration(participant.getId(), ageEvent1.getId(), employee.getId());
        Long idRegistration = 12345L;
        registration.setId(idRegistration);

        System.out.println(registration);
    }
}