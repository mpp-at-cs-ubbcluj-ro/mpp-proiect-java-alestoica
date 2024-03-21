package validators;

import model.AgeEvent;
import model.AgeGroup;
import model.Participant;
import model.SportsEvent;

public class AgeEventValidator implements Validator<AgeEvent> {
    @Override
    public void validate(AgeEvent entity) throws ValidationException {
        StringBuffer msg = new StringBuffer();

        if (entity.getId() < 0)
            msg.append("The id cannot be negative!");

       if (entity.getAgeGroup() != AgeGroup.GROUP_6_8_YEARS && entity.getAgeGroup() != AgeGroup.GROUP_9_11_YEARS && entity.getAgeGroup() != AgeGroup.GROUP_12_15_YEARS)
           msg.append("The age group is invalid!");

        if (entity.getSportsEvent() != SportsEvent.METERS_50 && entity.getSportsEvent() != SportsEvent.METERS_100 && entity.getSportsEvent() != SportsEvent.METERS_1000 && entity.getSportsEvent() != SportsEvent.METERS_1500)
            msg.append("The sports event is invalid!");

        if (!msg.isEmpty())
            throw new ValidationException(msg.toString());
    }
}
