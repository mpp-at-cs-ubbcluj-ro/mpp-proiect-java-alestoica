package repository;

import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validators.RegistrationValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class RegistrationDBRepository implements RegistrationRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private RegistrationValidator validator;
    private ParticipantRepository participantRepository;
    private AgeEventRepository ageEventRepository;
    private EmployeeRepository employeeRepository;

    public RegistrationDBRepository(RegistrationValidator validator, Properties props, ParticipantRepository participantRepository, AgeEventRepository ageEventRepository, EmployeeRepository employeeRepository) {
        logger.info("initializing RegistrationDBRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
        this.validator = validator;
        this.participantRepository = participantRepository;
        this.ageEventRepository =  ageEventRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Collection<Registration> findByAgeEvent(AgeEvent ageEvent) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from registrations where id_age_event = ?")) {

            preparedStatement.setLong(1, ageEvent.getId());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                Long id = result.getLong("id");
                Long idParticipant = result.getLong("id_participant");
                Long idEmployee = result.getLong("id_employee");

                Participant participant = participantRepository.findOne(idParticipant);
                Employee employee = employeeRepository.findOne(idEmployee);

                Registration registration = new Registration(participant, ageEvent, employee);
                registration.setId(id);

                registrations.add(registration);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return registrations;
    }

    @Override
    public Collection<Registration> findByParticipant(Participant participant) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from registrations where id_participant = ?")) {

            preparedStatement.setLong(1, participant.getId());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                Long id = result.getLong("id");
                Long idAgeEvent = result.getLong("id_age_event");
                Long idEmployee = result.getLong("id_employee");

                AgeEvent ageEvent = ageEventRepository.findOne(idAgeEvent);
                Employee employee = employeeRepository.findOne(idEmployee);

                Registration registration = new Registration(participant, ageEvent, employee);
                registration.setId(id);

                registrations.add(registration);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return registrations;
    }

    @Override
    public Registration findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from registrations where id = ?")) {

            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Long idParticipant = result.getLong("id_participant");
                Long idEmployee = result.getLong("id_employee");
                Long idAgeEvent = result.getLong("id_age_event");

                Participant participant = participantRepository.findOne(idParticipant);
                AgeEvent ageEvent = ageEventRepository.findOne(idAgeEvent);
                Employee employee = employeeRepository.findOne(idEmployee);

                Registration registration = new Registration(participant, ageEvent, employee);
                registration.setId(id);

                logger.traceExit();
                return registration;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from registrations");
             ResultSet result = preparedStatement.executeQuery();) {

            while (result.next()) {
                Long id = result.getLong("id");
                Long idParticipant = result.getLong("id_participant");
                Long idEmployee = result.getLong("id_employee");
                Long idAgeEvent = result.getLong("id_age_event");

                Participant participant = participantRepository.findOne(idParticipant);
                AgeEvent ageEvent = ageEventRepository.findOne(idAgeEvent);
                Employee employee = employeeRepository.findOne(idEmployee);

                Registration registration = new Registration(participant, ageEvent, employee);
                registration.setId(id);

                registrations.add(registration);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return registrations;
    }

    @Override
    public void add(Registration entity) {
        logger.traceEntry("saving registration {}", entity);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("insert into registrations (id, id_participant, id_age_event, id_employee) values (?, ?, ?, ?)")) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getParticipant().getId());
            preparedStatement.setLong(3, entity.getAgeEvent().getId());
            preparedStatement.setLong(4, entity.getEmployee().getId());

            validator.validate(entity);

            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting registration with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("delete from registrations where id = ?")) {

            preparedStatement.setLong(1, id);

            int result = preparedStatement.executeUpdate();
            logger.trace("deleted {} instances", result);

        } catch (SQLException e) {

            logger.error(e);
            System.err.println("db error " + e);

        }

        logger.traceExit();
    }

    @Override
    public void update(Long id, Registration entity) {
        logger.traceEntry("updating participant with id {}", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("update registrations set id_participant = ?, id_age_event = ?, id_employee = ? where id = ?")) {

            preparedStatement.setLong(1, entity.getParticipant().getId());
            preparedStatement.setLong(2, entity.getAgeEvent().getId());
            preparedStatement.setLong(3, entity.getEmployee().getId());
            preparedStatement.setLong(4, id);

            validator.validate(entity);

            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
    }

    @Override
    public Collection<Registration> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from registrations");
             ResultSet result = preparedStatement.executeQuery();) {

            while (result.next()) {
                Long id = result.getLong("id");
                Long idParticipant = result.getLong("id_participant");
                Long idEmployee = result.getLong("id_employee");
                Long idAgeEvent = result.getLong("id_age_event");

                Participant participant = participantRepository.findOne(idParticipant);
                AgeEvent ageEvent = ageEventRepository.findOne(idAgeEvent);
                Employee employee = employeeRepository.findOne(idEmployee);

                Registration registration = new Registration(participant, ageEvent, employee);
                registration.setId(id);

                registrations.add(registration);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return registrations;
    }
}
