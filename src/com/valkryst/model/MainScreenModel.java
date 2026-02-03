package com.valkryst.model;

import com.valkryst.objects.BugReport;
import com.valkryst.objects.StatusType;
import com.valkryst.services.DataManagerBugReport;
import eu.hansolo.enzo.notification.Notification;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainScreenModel {
    /** The list of BugReports loaded from the DB. */
    @Getter private final List<BugReport> bugReports;
    /** The list of StatusTypes. */
    @Getter private List<StatusType> statusTypes = new ArrayList<>();

    /**
     * Constructs a new MainScreenModel using the
     * specified data.
     *
     * @param dataManagerBugReport
     *         The data manager to use when working with the DB.
     */
    public MainScreenModel(final DataManagerBugReport dataManagerBugReport) {
        bugReports = dataManagerBugReport.getBugReports();
        loadStatusTypes();
    }

    /** Loads all status types. */
    private void loadStatusTypes() {
        try(
                final InputStream is = this.getClass().getResourceAsStream("/Report Status Data.json");
                final JsonReader reader = Json.createReader(is);
        ) {
            // Open the JSON file for reading:
            final JsonObject object = reader.readObject();
            final JsonArray statuses = object.getJsonArray("statuses");

            for(final JsonValue statusValue : statuses) {
                final JsonObject statusObject = (JsonObject) statusValue;

                final int id = statusObject.getInt("id");
                final String name = statusObject.getString("name");

                final StatusType statusType = new StatusType(id, name);
                statusTypes.add(statusType);
            }
        } catch(final IOException e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);

            Notification.Notifier.INSTANCE.notifyError("Error", "There was an error loading the status types.");
        }
    }
}
