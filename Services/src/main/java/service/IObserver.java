package service;

import model.Participant;
import model.Registration;

public interface IObserver {
    void notifyAddRegistration(Registration registration) throws Exception;
    void notifyAddParticipant(Participant participant) throws Exception;
}
