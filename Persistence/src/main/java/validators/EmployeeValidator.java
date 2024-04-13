package validators;

import model.Employee;

public class EmployeeValidator implements Validator<Employee> {
    @Override
    public void validate(Employee entity) throws ValidationException {
        StringBuffer msg = new StringBuffer();

        if (entity.getId() < 0)
            msg.append("The id cannot be negative!");

        if (entity.getFirstName().isEmpty() || entity.getLastName().isEmpty())
            msg.append("The name of the participant cannot be null!");

        if (entity.getUsername().isEmpty())
            msg.append("The username of the participant cannot be null!");

        if (entity.getPassword().isEmpty())
            msg.append("The password of the participant cannot be null!");

        if (!msg.isEmpty())
            throw new ValidationException(msg.toString());
    }
}
