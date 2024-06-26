package rpcprotocol;

public enum RequestType {
    LOGIN,
    LOGOUT,
    ADD_REGISTRATION,
    ADD_PARTICIPANT,
    GET_EVENTS,
    GET_PARTICIPANTS,
    GET_PARTICIPANT,
    GET_EMPLOYEE,
    GET_AGE_EVENT,
    REGISTRATION_BY_AGE_EVENT,
    REGISTRATION_BY_PARTICIPANT,
    PARTICIPANT_BY_NAME_AND_AGE,
    COUNT_PARTICIPANTS,
    COUNT_REGISTRATIONS
}
