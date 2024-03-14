package validators;

import model.Participant;

public class ParticipantValidator implements Validator<Participant> {
    @Override
    public void validate(Participant entity) throws ValidationException {
        StringBuffer msg = new StringBuffer();

        if (entity.getId() < 0)
            msg.append("The id cannot be negative!");

        if (entity.getFirstName().isEmpty() || entity.getLastName().isEmpty())
            msg.append("The name of the participant cannot be null!");

        if (entity.getAge() < 0)
            msg.append("The age of the participant cannot be negative!");

        if (!msg.isEmpty())
            throw new ValidationException(msg.toString());
    }
}
