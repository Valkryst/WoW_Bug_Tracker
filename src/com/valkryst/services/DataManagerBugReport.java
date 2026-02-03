package com.valkryst.services;

import com.valkryst.objects.BugReport;
import eu.hansolo.enzo.notification.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acts as a middle-man between the rest of the program and
 * the database for any matters related to the bugreports.
 *
 * @author Valkryst
 */
public class DataManagerBugReport extends DataManager {
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
    public DataManagerBugReport(final String ipAddress, final String port, final String databaseName, final String username, final String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        super(ipAddress, port, databaseName, username, password);
    }

    /**
     * Retrieves all reports from the DB.
     *
     * @return
     *         The list of retrieved reports, else
     *         null if an error occurs.
     */
    public List<BugReport> getBugReports() {
        try {
            // Prepare and execute statement to retrieve all bug reports.
            final CallableStatement statement = dbConnection.prepareCall("CALL getAllRawBugReports();");
            final ResultSet resultSet = super.executeQueryWithResults(statement);

            return compileBugReports(resultSet);
        } catch(final SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error retrieving the reports.");
        }

        return null;
    }

    /**
     * Constructs all of the reports from the specified
     * result set and returns them.
     *
     * @param resultSet
     *         The result set containing the reports.
     *
     * @return
     *         The list of constructed reports, else
     *         null if an error occurs.
     */
    private List<BugReport> compileBugReports(final ResultSet resultSet) {
        try {
            // Setup list & point the result set to the first result:
            final List<BugReport> list_reports = new ArrayList<>();
            resultSet.first();

            // Construct reports & add them to the list:
            do {
                final BugReport report  = new BugReport(resultSet.getInt("id"), resultSet.getByte("status"), resultSet.getString("type"), resultSet.getString("content"));
                list_reports.add(report);
            } while(resultSet.next());

            return list_reports;
        } catch(final SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error compiling the reports.");
            return new ArrayList<>();
        }
    }

    /**
     * Sets the status of the specified report in the DB.
     *
     * @param bugReport
     *         The report whose status is to be altered.
     *
     * @param status
     *         The new status update to.
     */
    public void setBugReportStatus(final BugReport bugReport, final int status) {
        try {
            final CallableStatement statement = dbConnection.prepareCall("CALL setButReportStatus(?, ?);");
            statement.setInt(1, bugReport.getId());
            statement.setInt(2, status);

            super.executeQueryWithoutResults(statement);
        } catch(final SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error setting bug report status.");
        }
    }

    /**
     * Deletes the specified report from the DB.
     *
     * @param bugReport
     *         The report to be deleted.
     */
    public void deleteBugReport(final BugReport bugReport) {
        try {
            final CallableStatement statement = dbConnection.prepareCall("CALL deleteBugReport(?);");
            statement.setInt(1, bugReport.getId());

            super.executeQueryWithoutResults(statement);
        } catch(final SQLException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error deleting bug report");
        }
    }
}
