package validators;

import model.Registration;

public class RegistrationValidator implements Validator<Registration> {
    @Override
    public void validate(Registration entity) throws ValidationException {
        StringBuffer msg = new StringBuffer();

        if (entity.getId() < 0)
            msg.append("The id cannot be negative!");

        if (entity.getIdParticipant() < 0)
            msg.append("The id of the participant cannot be negative!");

        if (entity.getIdEmployee() < 0)
            msg.append("The id of the employee cannot be negative!");

        if (entity.getIdAgeEvent() < 0)
            msg.append("The id of the event cannot be negative!");

        if (!msg.isEmpty())
            throw new ValidationException(msg.toString());
    }
}
