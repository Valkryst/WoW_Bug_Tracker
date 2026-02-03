package com.valkryst.objects;

import com.valkryst.model.MainScreenModel;
import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BugReport {
    /** The id. */
    @Getter private IntegerProperty id;
    /** The status type id. */
    @Getter @Setter private IntegerProperty status;
    /** The amount of elements, within the report, with potentially useless data. */
    @Getter private IntegerProperty uselessElements;
    /** The OS & user specifications. */
    @Getter private String type;
    /** The main content of the report. */
    @Getter private String content;

    /**
     * Constructs a new BugReport with the specified data.
     *
     * @param id
     *         The id.
     *
     * @param status
     *         The status type id.
     *
     * @param type
     *         The OS & user specifications.
     *
     * @param content
     *         The main content of the report.
     */
    public BugReport(final int id, final byte status, final String type, final String content) {
        this.id = new SimpleIntegerProperty(id);
        this.status = new SimpleIntegerProperty(status);
        this.content = content;

        try {
            final BugReportType bugReportType = new BugReportType(type);
            this.type = bugReportType.toString();
            uselessElements = new SimpleIntegerProperty(bugReportType.getUselessElements());
        } catch(final Exception e) {
            final Logger logger = LogManager.getLogger();
            logger.error(e);
        }
    }

    /** @return The id. */
    public Integer getId() {
        return id.get();
    }

    /** @return The name of the status or "ERROR" if no name was found. */
    public String getStatus(final MainScreenModel model) {
        final Optional<StatusType> optional = model.getStatusTypes()
                                                   .parallelStream()
                                                   .filter(statusType -> statusType.getId() == status.get())
                                                   .findFirst();

        if(optional.isPresent()) {
            return optional.get().getName();
        } else {
            return "ERROR";
        }
    }

    /** @return The amount of elements, within the report, with potentially useless data. */
    public Integer getUselessElements() {
        return uselessElements.get();
    }
}
