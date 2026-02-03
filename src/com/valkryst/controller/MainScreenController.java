package com.valkryst.controller;

import com.valkryst.model.MainScreenModel;
import com.valkryst.objects.BugReport;
import com.valkryst.objects.StatusType;
import com.valkryst.services.DataManagerBugReport;
import com.valkryst.view.MainScreenView;
import eu.hansolo.enzo.notification.Notification;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MainScreenController implements ChangeListener, EventHandler {
    /** The stage on which the current scene resides. */
    private final Stage primaryStage;

    /** The data manager to use when working with the DB. */
    private final DataManagerBugReport dataManagerBugReport;

    /** The model. */
    private final MainScreenModel model;

    /** The view. */
    @Getter private final MainScreenView view;

    /**
     * Constructs a new MainScreenController using the
     * specified data.
     *
     * @param primaryStage
     *         The stage on which the current scene resides.
     *
     * @param dataManagerBugReport
     *         The data manager to use when working with the DB.
     */
    public MainScreenController(final Stage primaryStage, final DataManagerBugReport dataManagerBugReport) {
        this.primaryStage = primaryStage;
        this.dataManagerBugReport = dataManagerBugReport;

        model = new MainScreenModel(dataManagerBugReport);
        view = new MainScreenView(this, model);


        primaryStage.setMinWidth(512);
        primaryStage.setMinHeight(512);

        primaryStage.setMaxWidth(Integer.MAX_VALUE);
        primaryStage.setMaxHeight(Integer.MAX_VALUE);

        primaryStage.setResizable(true);
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {
            final int selectedId = view.getTableView()
                                       .getSelectionModel()
                                       .getSelectedItem()
                                       .getId();

            final Optional<BugReport> doesReportExist = dataManagerBugReport.getBugReports()
                                                                            .parallelStream()
                                                                            .filter(report -> report.getId() == selectedId)
                                                                            .findFirst();

            if (doesReportExist.isPresent()) {
                final String data_type = doesReportExist.get().getType();
                final String data_content = doesReportExist.get().getContent();

                view.getTextArea().clear();
                view.getTextArea().setText(data_type + "\n\n" + data_content);
            }
        }
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();

        if(source.equals(view.getButton_delete())) {
            // Get the report and remove it from the DB:
            final BugReport selectedReport = view.getTableView().getSelectionModel().getSelectedItem();
            dataManagerBugReport.deleteBugReport(selectedReport);

            // Remove the report from the TableView:
            view.getTableView().getItems().remove(selectedReport);
            model.getBugReports().remove(selectedReport);

            // Notify Success:
            Notification.Notifier.INSTANCE.notifySuccess("Success", "Report #" + selectedReport.getId() + " deleted.");
        }

        if(source.equals(view.getButton_setStatus())) {
            // Get the new status ID:
            final String newStatusName = view.getComboBox_statusTypes()
                                             .getSelectionModel()
                                             .getSelectedItem();

            final Optional<StatusType> newStatusType = model.getStatusTypes()
                                                            .parallelStream()
                                                            .filter(statusType -> statusType.getName().equals(newStatusName))
                                                            .findFirst();

            if(newStatusType.isPresent()) {
                // Get the report and update it's status in the DB:
                final BugReport selectedReport = view.getTableView().getSelectionModel().getSelectedItem();
                dataManagerBugReport.setBugReportStatus(selectedReport, newStatusType.get().getId());

                // Update the report itself:
                selectedReport.setStatus(new SimpleIntegerProperty(newStatusType.get().getId()));

                // Hacky workaround to update record data in TableView:
                final int selectedTableIndex = view.getTableView().getSelectionModel().getSelectedIndex();
                view.getTableView().getColumns().get(selectedTableIndex).setVisible(false);
                view.getTableView().getColumns().get(selectedTableIndex).setVisible(true);

                // Notify Success:
                Notification.Notifier.INSTANCE.notifySuccess("Success", "Report #" + selectedReport.getId() + " updated.");
            } else {
                final Logger logger = LogManager.getLogger();
                logger.error("No StatusType matching the name of " + newStatusName + " could be found within the " +
                             "list of loaded StatusTypes. Check the \"Report Status Data.json\" file to ensure the " +
                             "JSON is correctly formatted and to see if the StatusType actually exists.");

                Notification.Notifier.INSTANCE.notifyError("Error", "No StatusType matching the name of " + newStatusName + " exists.");
            }
        }
    }
}
