package com.valkryst.controller;

import com.valkryst.services.DataManagerBugReport;
import com.valkryst.view.ConnectionScreenView;
import eu.hansolo.enzo.notification.Notification;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class ConnectionScreenController implements EventHandler {
    /** The stage on which the current scene resides. */
    private final Stage primaryStage;

    /** The view. */
    @Getter private final ConnectionScreenView view;

    /**
     * Constructs a new ConnectionScreenController
     * using the specified data.
     *
     * @param primaryStage
     *         The stage on which the current scene resides.
     */
    public ConnectionScreenController(final Stage primaryStage) {
        this.primaryStage = primaryStage;

        view = new ConnectionScreenView(this);
        loadLoginData();

        // Set Stage Size:
        primaryStage.setMinWidth(512);
        primaryStage.setMinHeight(240);

        primaryStage.setMaxWidth(512);
        primaryStage.setMaxHeight(240);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();

        if(source.equals(view.getButton_login())) {
            if(isConnectionDataValid()) {
                if(attemptLogin()) {
                    saveLoginData();
                }
            }
        }
    }

    /**
     * Determines whether or not the connection data
     * appears to be valid.
     *
     * @return
     *         Whether or not the connection data
     *         appears to be valid.
     */
    private boolean isConnectionDataValid() {
        final TextField field_serverAddress = view.getField_serverAddress();
        final TextField field_mysqlPort = view.getField_mysqlPort();
        final TextField field_databaseName = view.getField_databaseName();
        final TextField field_username = view.getField_username();
        final PasswordField field_password = view.getField_password();

        boolean attemptLogin = true;

        // Reset error states:
        view.resetComponentStyles();

        // Reset tooltips:
        view.setDefaultComponentTooltips();

        // Check if server address, MySQL port, database name, username, or password fields are empty:
        if(field_serverAddress.getText().length() == 0) {
            field_serverAddress.getStylesheets().remove("global.css");
            field_serverAddress.getStylesheets().add("field_error.css");
            attemptLogin = false;
        }

        if(field_mysqlPort.getText().length() == 0) {
            field_mysqlPort.getStylesheets().remove("global.css");
            field_mysqlPort.getStylesheets().add("field_error.css");
            attemptLogin = false;
        }

        if(field_databaseName.getText().length() == 0) {
            field_databaseName.getStylesheets().remove("global.css");
            field_databaseName.getStylesheets().add("field_error.css");
            attemptLogin = false;
        }

        if(field_username.getText().length() == 0) {
            field_username.getStylesheets().remove("global.css");
            field_username.getStylesheets().add("field_error.css");
            attemptLogin = false;
        }

        if(field_password.getText().length() == 0) {
            field_password.getStylesheets().remove("global.css");
            field_password.getStylesheets().add("field_error.css");
            attemptLogin = false;
        }

        return attemptLogin;
    }

    /**
     * Attempt to login. If successful, switch to the next
     * view.
     *
     * @return
     *         Whether or not the login was successful.
     */
    private boolean attemptLogin() {
        final String serverAddress = view.getField_serverAddress().getText();
        final String mysqlPort = view.getField_mysqlPort().getText();
        final String databaseName = view.getField_databaseName().getText();
        final String username = view.getField_username().getText();
        final String password = view.getField_password().getText();

        try {
            final DataManagerBugReport dataManagerBugReport = new DataManagerBugReport(serverAddress, mysqlPort, databaseName, username, password);

            final Scene scene = new Scene(new MainScreenController(primaryStage, dataManagerBugReport).getView());
            scene.getStylesheets().add("global.css");
            scene.getRoot().getStyleClass().add("main-root");
            primaryStage.setScene(scene);

            return true;
        } catch(final ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "Connection information may be incorrect.");
        }

        return false;
    }

    /**
     * Attempts to load the login data and fill the
     * text fields of the view with it.
     */
    private void loadLoginData() {
        try {
            final List<String> lines = Files.readAllLines(Paths.get("login_information.txt"), Charset.defaultCharset());

            for(int i = 0 ; i < lines.size() ; i++) {
                final String line = lines.get(i);

                if(! line.isEmpty()) {
                    switch (i) {
                        case 0: {
                            view.getField_serverAddress().setText(line);
                            break;
                        }
                        case 1: {
                            view.getField_mysqlPort().setText(line);
                            break;
                        }
                        case 2: {
                            view.getField_databaseName().setText(line);
                            break;
                        }
                        case 3: {
                            view.getField_username().setText(line);
                            break;
                        }
                    }
                }
            }
        } catch(final IOException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error when loading the login data.");
        }
    }

    /** Attempts to save the login data. */
    private void saveLoginData() {
        // Get the data to be written & the file to write to.
        final String serverAddress = view.getField_serverAddress().getText();
        final String mysqlPort = view.getField_mysqlPort().getText();
        final String databaseName = view.getField_databaseName().getText();
        final String username = view.getField_username().getText();

        final File file = new File("login_information.txt");

        try (
                final FileWriter fileWriter = new FileWriter(file);
                final PrintWriter printWriter = new PrintWriter(fileWriter);
        ) {
            /*
             * Write the first line. This both writes the
             * first line and erases all of the old data.
             *
             * Append the remaining lines.
             */
            printWriter.write(serverAddress + "\n");
            printWriter.append(mysqlPort).append("\n");
            printWriter.append(databaseName).append("\n");
            printWriter.append(username).append("\n");
            printWriter.flush();
        } catch (final IOException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error when saving the login data.");
        }
    }
}
