package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcUtils {
    private Properties jdbcProps;
    private static final Logger logger = LogManager.getLogger();
    public JdbcUtils(Properties props){
        jdbcProps = props;
    }
    private Connection instance=null;

    private Connection getNewConnection(){
        logger.traceEntry();

        String url=jdbcProps.getProperty("jdbc.url");
        String user=jdbcProps.getProperty("tasks.jdbc.user");
        String pass=jdbcProps.getProperty("tasks.jdbc.pass");

        logger.info("trying to connect to database {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);

        Connection con = null;
        try {
            if (user != null && pass != null)
                con = DriverManager.getConnection(url, user, pass);
            else
                con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("error getting connection " + e);
        }

        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();

        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("db error " + e);
        }

        logger.traceExit(instance);
        return instance;
    }
}

