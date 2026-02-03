package com.valkryst.services;

import eu.hansolo.enzo.notification.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;

/**
 * Controls access to the database for all classes
 * which extend this class. Allows for two types of
 * queries, one with and one without results.
 *
 * @author Valkryst
 */
public class DataManager {
    /** The connection to use when connecting the the database. */
    protected Connection dbConnection;

    /**
     * Construct a new DataManager object with it's own
     * connection to the database.
     *
     * @param ipAddress The IP address to the server on which MySQL is running.
     * @param port The port to use when connecting to MySQL.
     * @param databaseName The database name of the database to connect to.
     * @param username The username for a MySQL account with access to the specified database.
     * @param password The password for the MySQL account specified by the specified username.
     */
    public DataManager(final String ipAddress, final String port, final String databaseName, final String username, final String password) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?noAccessToProcedureBodies=true"; //?noAccessToProcedureBodies=true is required for using the stored procedures.
        dbConnection = DriverManager.getConnection(url, username, password);

        // Attempt to overwrite the password using reflection:
        try {
            final Field value = String.class.getDeclaredField("value");
            value.setAccessible(true);
            final char[] chars = (char[]) value.get(password);
            Arrays.fill(chars, '*');
        } catch (final NoSuchFieldException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error creating the DataManager.");
        }
    }

    /**
     * Execute the specified query and return no result.a
     * This is best used when the query doesn't result in results.
     * @param preparedStatement The query to execute.
     */
    protected void executeQueryWithoutResults(final PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeQuery();
        } catch(SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error executing a query without results.");
        }
    }

    /**
     * Execute the specified query and return all results.
     * This is best used when the query results in results.
     * @param preparedStatement The query to execute.
     * @return The result set of the executed query.
     */
    protected ResultSet executeQueryWithResults(final PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch(SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error executing a query with results.");
        }
        return null;
    }
}
