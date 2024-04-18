package protobufprotocol;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProtoUtils {

    public static Protobufs.Employee getProtoEmployee(Employee employee) {
        return Protobufs.Employee.newBuilder()
                .setId(employee.getId())
                .setFirstName(employee.getFirstName())
                .setLastName(employee.getLastName())
                .setUsername(employee.getUsername())
                .setPassword(employee.getPassword())
                .build();
    }

    public static Protobufs.Participant getProtoParticipant(Participant participant) {
        if (participant != null)
            return Protobufs.Participant.newBuilder()
                    .setId(participant.getId())
                    .setFirstName(participant.getFirstName())
                    .setLastName(participant.getLastName())
                    .setAge(participant.getAge())
                    .build();
        else
            return Protobufs.Participant.newBuilder()
                    .build();
    }

    public static Protobufs.AgeEvent getProtoAgeEvent(AgeEvent ageEvent) {
        Protobufs.AgeEvent.AgeGroup protoAgeGroup =
                Protobufs.AgeEvent.AgeGroup.valueOf(ageEvent.getAgeGroup().toString());

        Protobufs.AgeEvent.SportsEvent protoSportsEvent =
                Protobufs.AgeEvent.SportsEvent.valueOf(ageEvent.getSportsEvent().toString());

        return Protobufs.AgeEvent.newBuilder()
                        .setId(ageEvent.getId())
                        .setAgeGroup(protoAgeGroup)
                        .setSportsEvent(protoSportsEvent)
                        .build();
    }

    public static Protobufs.Registration getProtoRegistration(Registration registration) {
        Protobufs.Participant protoParticipant = getProtoParticipant(registration.getParticipant());

        Protobufs.AgeEvent protoAgeEvent = getProtoAgeEvent(registration.getAgeEvent());

        Protobufs.Employee protoEmployee = getProtoEmployee(registration.getEmployee());

        return Protobufs.Registration.newBuilder()
                        .setId(registration.getId())
                        .setParticipant(protoParticipant)
                        .setAgeEvent(protoAgeEvent)
                        .setEmployee(protoEmployee)
                        .build();
    }

    public static List<Protobufs.Registration> getProtoRegistrations(Collection<Registration> registrations) {
        List<Protobufs.Registration> protoRegistrations = new ArrayList<>();

        for (Registration registration : registrations) {
            Protobufs.Registration protoRegistration = getProtoRegistration(registration);
            protoRegistrations.add(protoRegistration);
        }

        return protoRegistrations;
    }

    public static String getError(Protobufs.Response response){
        return response.getError();
    }

    public static Employee getEmployee(Protobufs.Employee protoEmployee) {
        Employee employee = new Employee(protoEmployee.getFirstName(), protoEmployee.getLastName(), protoEmployee.getUsername(), protoEmployee.getPassword());
        employee.setId(protoEmployee.getId());
        return employee;
    }

    public static Participant getParticipant(Protobufs.Participant protoParticipant) {
        Participant participant = new Participant(protoParticipant.getFirstName(), protoParticipant.getLastName(), protoParticipant.getAge());
        participant.setId(protoParticipant.getId());
        return participant;
    }

    public static AgeEvent getAgeEvent(Protobufs.AgeEvent protoAgeEvent) {
        System.out.println("getAgeEvent " + protoAgeEvent);
        AgeEvent ageEvent = new AgeEvent(AgeGroup.valueOf(protoAgeEvent.getAgeGroup().toString()), SportsEvent.valueOf(protoAgeEvent.getSportsEvent().toString()));
        ageEvent.setId(protoAgeEvent.getId());
        return ageEvent;
    }

    public static Registration getRegistration(Protobufs.Registration protoRegistration) {
        Participant participant = getParticipant(protoRegistration.getParticipant());

        AgeEvent ageEvent = getAgeEvent(protoRegistration.getAgeEvent());

        Employee employee = getEmployee(protoRegistration.getEmployee());

        Registration registration = new Registration(participant, ageEvent, employee);
        registration.setId(protoRegistration.getId());
        return registration;
    }

    public static Collection<AgeEvent> getAgeEvents(List<Protobufs.AgeEvent> protoAgeEvents) {
        Collection<AgeEvent> ageEvents = new ArrayList<>();

        for (Protobufs.AgeEvent protoAgeEvent : protoAgeEvents) {
            AgeEvent ageEvent = new AgeEvent(AgeGroup.valueOf(protoAgeEvent.getAgeGroup().toString()), SportsEvent.valueOf(protoAgeEvent.getSportsEvent().toString()));
            ageEvent.setId(protoAgeEvent.getId());
            ageEvents.add(ageEvent);
        }

        return ageEvents;
    }

    public static Collection<Participant> getParticipants(List<Protobufs.Participant> protoParticipants) {
        Collection<Participant> participants = new ArrayList<>();

        for (Protobufs.Participant protoParticipant : protoParticipants) {
            Participant participant = new Participant(protoParticipant.getFirstName(), protoParticipant.getLastName(), protoParticipant.getAge());
            participant.setId(protoParticipant.getId());
            participants.add(participant);
        }

        return participants;
    }

    public static Collection<Registration> getRegistrations(List<Protobufs.Registration> protoRegistrations) {
        Collection<Registration> registrations = new ArrayList<>();

        for (Protobufs.Registration protoRegistration : protoRegistrations) {
            Registration registration = getRegistration(protoRegistration);
            registrations.add(registration);
        }

        return registrations;
    }

    public static Protobufs.Response setCount(Protobufs.Response.Builder response, int count) {
        return response.setCount(count).build();
    }

    public static Protobufs.Response setEmployee(Protobufs.Response.Builder response, Employee employee) {
        return response.setEmployee(getProtoEmployee(employee)).build();
    }

    public static Protobufs.Response setParticipant(Protobufs.Response.Builder response, Participant participant) {
        return response.setParticipant(getProtoParticipant(participant)).build();
    }

    public static Protobufs.Response setAgeEvent(Protobufs.Response.Builder response, AgeEvent ageEvent) {
        return response.setAgeEvent(getProtoAgeEvent(ageEvent)).build();
    }

    public static Protobufs.Response setRegistration(Protobufs.Response.Builder response, Registration registration) {
        return response.setRegistration(getProtoRegistration(registration)).build();
    }

    public static Protobufs.Response setRegistrations(Protobufs.Response.Builder response, Collection<Registration> registrations) {
        return response.addAllRegistrations(getProtoRegistrations(registrations)).build();
    }


//    REQUESTS


    public static Protobufs.Request createLoginRequest(Employee employee) {
        System.out.println("createLoginRequest");
        Protobufs.Employee protoEmployee = getProtoEmployee(employee);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.LOGIN)
                .setEmployee(protoEmployee)
                .build();
    }

    public static Protobufs.Request createLogoutRequest(Long id) {
        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.LOGOUT)
                .setId(id)
                .build();
    }

    public static Protobufs.Request createAddRegistrationRequest(Registration registration) {
        Protobufs.Registration protoRegistration = getProtoRegistration(registration);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.ADD_REGISTRATION)
                .setRegistration(protoRegistration)
                .build();
    }

    public static Protobufs.Request createAddParticipantRequest(Participant participant) {
        Protobufs.Participant protoParticipant = getProtoParticipant(participant);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.ADD_PARTICIPANT)
                .setParticipant(protoParticipant)
                .build();
    }

    public static Protobufs.Request createGetEventsRequest() {
        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.GET_EVENTS)
                .build();
    }

    public static Protobufs.Request createGetParticipantsRequest() {
        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.GET_PARTICIPANTS)
                .build();
    }

    public static Protobufs.Request createGetParticipantRequest(Long id) {
        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.GET_PARTICIPANT)
                .setId(id)
                .build();
    }

    public static Protobufs.Request createGetEmployeeRequest() {
        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.GET_EMPLOYEE)
                .build();
    }

    public static Protobufs.Request createGetAgeEventRequest(AgeEvent ageEvent) {
        Protobufs.AgeEvent protoAgeEvent = getProtoAgeEvent(ageEvent);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.GET_AGE_EVENT)
                .setAgeEvent(protoAgeEvent)
                .build();
    }

    public static Protobufs.Request createGetRegistrationByAgeEventRequest(AgeEvent ageEvent) {
        Protobufs.AgeEvent protoAgeEvent = getProtoAgeEvent(ageEvent);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.REGISTRATION_BY_AGE_EVENT)
                .setAgeEvent(protoAgeEvent)
                .build();
    }

    public static Protobufs.Request createGetRegistrationByPartcicpantRequest(Participant participant) {
        Protobufs.Participant protoParticipant = getProtoParticipant(participant);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.REGISTRATION_BY_PARTICIPANT)
                .setParticipant(protoParticipant)
                .build();
    }

    public static Protobufs.Request createGetParticipantByNameAndAgeRequest(Participant participant) {
        Protobufs.Participant protoParticipant = getProtoParticipant(participant);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.PARTICIPANT_BY_NAME_AND_AGE)
                .setParticipant(protoParticipant)
                .build();
    }

    public static Protobufs.Request createCountParticipantsRequest(AgeEvent ageEvent) {
        Protobufs.AgeEvent protoAgeEvent = getProtoAgeEvent(ageEvent);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.COUNT_PARTICIPANTS)
                .setAgeEvent(protoAgeEvent)
                .build();
    }

    public static Protobufs.Request createCountRegistrationsRequest(Participant participant) {
        Protobufs.Participant protoParticipant = getProtoParticipant(participant);

        return Protobufs.Request.newBuilder()
                .setType(Protobufs.Request.RequestType.COUNT_REGISTRATIONS)
                .setParticipant(protoParticipant)
                .build();
    }


//    RESPONSES


    public static Protobufs.Response.Builder createOkResponse() {
        return Protobufs.Response.newBuilder()
                .setType(Protobufs.Response.ResponseType.OK);
    }

    public static Protobufs.Response createErrorResponse(String error) {
        return Protobufs.Response.newBuilder()
                .setType(Protobufs.Response.ResponseType.ERROR)
                .setError(error)
                .build();
    }

    public static Protobufs.Response createGetEventsResponse(Collection<AgeEvent> ageEvents) {
        Protobufs.Response.Builder response =
                Protobufs.Response.newBuilder()
                .setType(Protobufs.Response.ResponseType.GET_EVENTS);

        for(AgeEvent ageEvent : ageEvents) {
            Protobufs.AgeEvent protoAgeEvent = getProtoAgeEvent(ageEvent);

            response.addAgeEvents(protoAgeEvent);
        }

        return response.build();
    }

    public static Protobufs.Response createGetParticipantsResponse(Collection<Participant> participants) {
        Protobufs.Response.Builder response =
                Protobufs.Response.newBuilder()
                        .setType(Protobufs.Response.ResponseType.GET_PARTICIPANTS);

        for(Participant participant : participants) {
            Protobufs.Participant protoParticipant = getProtoParticipant(participant);

            response.addParticipants(protoParticipant);
        }

        return response.build();
    }

    public static Protobufs.Response createNewRegistrationResponse(Registration registration) {
        Protobufs.Registration protoRegistration = getProtoRegistration(registration);

        return Protobufs.Response.newBuilder()
                .setType(Protobufs.Response.ResponseType.NEW_REGISTRATION)
                .setRegistration(protoRegistration)
                .build();
    }
}

