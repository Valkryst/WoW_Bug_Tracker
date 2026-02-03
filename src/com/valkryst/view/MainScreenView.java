package com.valkryst.view;

import com.valkryst.controller.MainScreenController;
import com.valkryst.model.MainScreenModel;
import com.valkryst.objects.BugReport;
import com.valkryst.objects.StatusType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;

public class MainScreenView extends HBox {
    /** The Button to delete the currently selected record of the TableView. */
    @Getter private Button button_delete = new Button("Delete");
    /**
     * The Button to update the currently selected record of the TableView
     * with the currently selected StatusType of the ComboBox.
     */
    @Getter private Button button_setStatus = new Button("Set Status");
    /** The ComboBox of StatusTypes. */
    @Getter private ComboBox<String> comboBox_statusTypes = new ComboBox<>();
    /** The TableView containing all of the BugReports. */
    @Getter private TableView<BugReport> tableView = new TableView();
    /**
     * The TextArea displaying the information of the currently selected
     * BugReport record of the TableView.
     */
    @Getter private final TextArea textArea = new TextArea();

    /** todo JavaDoc */
    public MainScreenView(final MainScreenController controller, final MainScreenModel model) {
        setDefaultComponentTooltips();
        setDefaultComponentChangeListeners(controller);
        setDefaultComponentEventHandlers(controller);

        setupComboBox(model);
        setupTableView(model);

        HBox.setHgrow(textArea, Priority.ALWAYS);

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(tableView, setupRightView());
    }

    /**
     * Populates the combobox with all loaded status types.
     *
     * @param model
     *         The model to retrieve the status type data from.
     */
    private void setupComboBox(final MainScreenModel model) {
        for(final StatusType statusType : model.getStatusTypes()) {
            comboBox_statusTypes.getItems().add(statusType.getName());
        }

        comboBox_statusTypes.getSelectionModel().selectFirst();
    }

    /**
     * Sets up the table view.
     *
     * @param model
     *         The model to retrieve the list data from.
     */
    private void setupTableView(final MainScreenModel model) {
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Create Columns:
        final TableColumn col_id = new TableColumn("ID");
        final TableColumn col_status = new TableColumn("Status");
        final TableColumn col_uselessnessScore = new TableColumn("Uselessness Score");

        // Set the variable that each column contains:
        col_id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BugReport, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<BugReport, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getId());
            }
        });

        col_status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BugReport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BugReport, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getStatus(model));
            }
        });


        col_uselessnessScore.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BugReport, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<BugReport, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getUselessElements());
            }
        });


        tableView.getColumns().addAll(col_id, col_status, col_uselessnessScore);
        tableView.getItems().addAll(model.getBugReports());
    }

    /** Sets all Components to have their default tooltips. */
    public void setDefaultComponentTooltips() {

    }


    /**
     * Sets specific Components to use the specified EventHandler as
     * their EventHandler.
     *
     * @param eventHandler
     *         The EventHandler to use.
     */
    private void setDefaultComponentEventHandlers(final EventHandler eventHandler) {
        button_delete.setOnAction(eventHandler);
        button_setStatus.setOnAction(eventHandler);
    }

    /**
     * Sets specific Components to use the specified ChangeListener as
     * their ChangeListener.
     *
     * @param changeListener
     *         The ChangeListener to use.
     */
    private void setDefaultComponentChangeListeners(final ChangeListener changeListener) {
        tableView.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    /** todo JavaDoc */
    private VBox setupRightView() {
        final HBox panel_buttons = new HBox(10);
        panel_buttons.getChildren().addAll(button_delete, comboBox_statusTypes, button_setStatus);

        final VBox panel = new VBox(4);
        panel.getChildren().addAll(panel_buttons, textArea);

        HBox.setHgrow(panel, Priority.ALWAYS);
        VBox.setVgrow(panel, Priority.ALWAYS);

        VBox.setVgrow(textArea, Priority.ALWAYS);

        return panel;
    }
}
