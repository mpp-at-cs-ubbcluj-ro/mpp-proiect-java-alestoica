package repository;

import model.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validators.ParticipantValidator;
import validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepository implements ParticipantRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private ParticipantValidator validator;

    public ParticipantDBRepository(ParticipantValidator validator, Properties props) {
        logger.info("initializing ParticipantDBRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
        this.validator = validator;
    }

    @Override
    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from participants where fist_name = ? and last_name = ? and age = ?")) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Long id = result.getLong("id");

                Participant participant = new Participant(firstName, lastName, age);
                participant.setId(id);

                logger.traceExit();
                return participant;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Participant findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from participants where id = ?")) {

            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                int age = result.getInt("age");

                Participant participant = new Participant(firstName, lastName, age);
                participant.setId(id);

                logger.traceExit();
                return participant;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from participants");
             ResultSet result = preparedStatement.executeQuery();) {

            while (result.next()) {
                Long id = result.getLong("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                int age = result.getInt("age");

                Participant participant = new Participant(firstName, lastName, age);
                participant.setId(id);

                participants.add(participant);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return participants;
    }

    @Override
    public void add(Participant entity) {
        logger.traceEntry("saving participant {}", entity);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("insert into participants (id, first_name, last_name, age) values (?, ?, ?, ?)")) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setInt(4, entity.getAge());

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
        logger.traceEntry("deleting participant with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("delete from participants where id = ?")) {

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
    public void update(Long id, Participant entity) {
        logger.traceEntry("updating participant with id {}", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("update participants set first_name = ?, last_name = ?, age = ? where id = ?")) {

            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getAge());
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
    public Collection<Participant> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from participants");
             ResultSet result = preparedStatement.executeQuery();) {

            while (result.next()) {
                Long id = result.getLong("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                int age = result.getInt("age");

                Participant participant = new Participant(firstName, lastName, age);
                participant.setId(id);

                participants.add(participant);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return participants;
    }
}
