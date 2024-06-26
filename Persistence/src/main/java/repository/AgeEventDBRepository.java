package repository;

import model.AgeEvent;
import model.AgeGroup;
import model.SportsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import validators.AgeEventValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Component
public class AgeEventDBRepository implements AgeEventRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private AgeEventValidator validator;

    @Autowired
    public AgeEventDBRepository(AgeEventValidator validator, Properties props) {
        logger.info("initializing AgeEventDBRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
        this.validator = validator;
    }

    @Override
    public int countParticipants(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("""
                select count(*) as count from participants p
                inner join registrations r on p.id == r.id_participant
                inner join age_events ae on ae.id = r.id_age_event
                where ae.id == ?""")){

            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            return result.getInt("count");
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return 0;
    }

    @Override
    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from age_events where age_group = ? and sports_event = ?")){

            preparedStatement.setString(1, ageGroup);
            preparedStatement.setString(2, sportsEvent);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Long id = result.getLong("id");
                AgeGroup ageGroup1 = AgeGroup.valueOf(ageGroup);
                SportsEvent sportsEvent1 = SportsEvent.valueOf(sportsEvent);

                AgeEvent ageEvent = new AgeEvent(ageGroup1, sportsEvent1);
                ageEvent.setId(id);

                logger.traceExit();
                return ageEvent;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public AgeEvent findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from age_events where id = ?")){

            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                AgeGroup ageGroup = AgeGroup.valueOf(result.getString("age_group"));
                SportsEvent sportsEvent = SportsEvent.valueOf(result.getString("sports_event"));

                AgeEvent ageEvent = new AgeEvent(ageGroup, sportsEvent);
                ageEvent.setId(id);

                logger.traceExit();
                return ageEvent;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Iterable<AgeEvent> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<AgeEvent> ageEvents = new ArrayList<>();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from age_events");
            ResultSet result = preparedStatement.executeQuery();){

            while (result.next()) {
                Long id = result.getLong("id");
                AgeGroup ageGroup = AgeGroup.valueOf(result.getString("age_group"));
                SportsEvent sportsEvent = SportsEvent.valueOf(result.getString("sports_event"));

                AgeEvent ageEvent = new AgeEvent(ageGroup, sportsEvent);
                ageEvent.setId(id);

                ageEvents.add(ageEvent);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return ageEvents;
    }

    @Override
    public void add(AgeEvent entity) {
        logger.traceEntry("saving age event {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("insert into age_events (age_group, sports_event) values (?, ?)")){

//            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(1, entity.getAgeGroup().toString());
            preparedStatement.setString(2, entity.getSportsEvent().toString());

//            validator.validate(entity);

            int result = preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }

            logger.trace("saved {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting age event with id {} ", id);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("delete from age_events where id = ?")){

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
    public void update(Long id, AgeEvent entity) {
        logger.traceEntry("updating age event with id {}", id);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("update age_events set age_group = ?, sports_event = ? where id = ?")){

            preparedStatement.setString(1, entity.getAgeGroup().toString());
            preparedStatement.setString(2, entity.getSportsEvent().toString());
            preparedStatement.setLong(3, id);

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
    public Collection<AgeEvent> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<AgeEvent> ageEvents = new ArrayList<>();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from age_events");
            ResultSet result = preparedStatement.executeQuery();){

            while (result.next()) {
                Long id = result.getLong("id");
                AgeGroup ageGroup = AgeGroup.valueOf(result.getString("age_group"));
                SportsEvent sportsEvent = SportsEvent.valueOf(result.getString("sports_event"));

                AgeEvent ageEvent = new AgeEvent(ageGroup, sportsEvent);
                ageEvent.setId(id);

                ageEvents.add(ageEvent);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return ageEvents;
    }
}
