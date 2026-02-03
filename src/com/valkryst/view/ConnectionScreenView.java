package com.valkryst.view;

import com.valkryst.controller.ConnectionScreenController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class ConnectionScreenView extends VBox {
    /** The maximum length, in characters, of an IP address. */
    private final static byte MAX_TEXTUAL_IP_ADDRESS_LENGTH = 39;
    /** The maximum length, in characters, of a port. */
    private final static byte MAX_TEXTUAL_PORT_LENGTH = 5;
    /** The maximum length, in characters, of a MySQL database name. */
    private final static byte MAX_TEXTUAL_DATABASE_NAME_LENGTH = 64;
    /** The maximum length of a username in MySQL. */
    private final static byte MAX_USERNAME_LENGTH = 16;
    /** The maximum length of a password in MySQL. */
    private final static byte MAX_PASSWORD_LENGTH = 41;

    /** The TextField for the IP address of the server to connect to. */
    @Getter private final TextField field_serverAddress = new TextField();
    /** The TextField for the port that MySQL runs on. */
    @Getter private final TextField field_mysqlPort = new TextField();
    /** The TextField for the name of the database to connect to. */
    @Getter private final TextField field_databaseName = new TextField();
    /** The TextField for the username to login with. */
    @Getter private final TextField field_username = new TextField();
    /** The PasswordField for the password to login with. */
    @Getter private final PasswordField field_password = new PasswordField();

    /** The Button to attempt a login. */
    @Getter private final Button button_login = new Button("Login");

    public ConnectionScreenView(final ConnectionScreenController controller) {
        setDefaultComponentTooltips();
        setDefaultComponentEventHandlers(controller);

        setFieldPromptText();
        setFieldColumnCounts();

        this.setPadding(new Insets(10));
        this.setSpacing(4);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(field_serverAddress, field_mysqlPort, field_databaseName, field_username,
                                  field_password, button_login);
    }

    /** Sets all Components to have their default tooltips. */
    public void setDefaultComponentTooltips() {
        field_serverAddress.setTooltip(new Tooltip("Enter the server IP address or domain name here."));
        field_mysqlPort.setTooltip(new Tooltip("Enter the port that MySQL runs on.\nThis is usually 3306."));
        field_databaseName.setTooltip(new Tooltip("Enter the name of the database which holds your character " +
                                                  "information.\nThis is generally called \"characters\"."));
        field_username.setTooltip(new Tooltip("Enter your MySQL username here."));
        field_password.setTooltip(new Tooltip("Enter your MySQL password here."));

        button_login.setTooltip(new Tooltip("Log in."));
    }

    /**
     * Sets all Components to use the specified EventHandler as their EventHandler.
     *
     * @param eventHandler
     *         The EventHandler to use.
     */
    private void setDefaultComponentEventHandlers(final EventHandler eventHandler) {
        button_login.setOnAction(eventHandler);
    }

    /** todo JavaDoc */
    private void setFieldPromptText() {
        field_serverAddress.setPromptText("Server Address (IP)");
        field_mysqlPort.setPromptText("MySQL Port");
        field_databaseName.setPromptText("Database Name");
        field_username.setPromptText("MySQL Username");
        field_password.setPromptText("MySQL Password");
    }

    /** Sets the preferred column count for all TextFields. */
    private void setFieldColumnCounts() {
        field_serverAddress.setPrefColumnCount(MAX_TEXTUAL_IP_ADDRESS_LENGTH);
        field_mysqlPort.setPrefColumnCount(MAX_TEXTUAL_PORT_LENGTH);
        field_databaseName.setPrefColumnCount(MAX_TEXTUAL_DATABASE_NAME_LENGTH);
        field_username.setPrefColumnCount(MAX_USERNAME_LENGTH);
        field_password.setPrefColumnCount(MAX_PASSWORD_LENGTH);
    }

    /** todo JavaDoc */
    public void resetComponentStyles() {
        for(final Node child : this.getChildren()) {
            if(child instanceof TextField) {
                ((TextField) child).getStylesheets().remove("field_error.css");
                ((TextField) child).getStylesheets().add("global.css");
            }

            if(child instanceof PasswordField) {
                ((PasswordField) child).getStylesheets().remove("field_error.css");
                ((PasswordField) child).getStylesheets().add("global.css");
            }
        }
    }
}
