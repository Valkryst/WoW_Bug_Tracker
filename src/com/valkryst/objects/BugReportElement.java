package com.valkryst.objects;

public class BugReportElement {
    /** The name of each element, sorted by ID. */
    private final static String[] ELEMENT_NAMES = {
        "Feedback Type",
        "Client Version",
        "Client Build",
        "Client Build Date",
        "Realm",
        "Locale",
        "Name",
        "Level",
        "Race",
        "Sex",
        "Class",
        "Map",
        "Zone",
        "Area",
        "Position",
        "Facing",
        "Speed",
        "Chunk",
        "Coords",
        "Addons Loaded",
        "Addons Disabled",
        "Talents",
        "Equipment",
        "Who",
        "Where",
        "When",
        "Type",
        "Survey Name",
        "Survey ID",
        "Survey Objective",
        "Survey Type",
        "Survey Obtained",
        "Survey Submitted",
        "Clarity Rating",
        "Difficulty Rating",
        "Reward Rating",
        "Fun Rating",
        "Combats",
        "Deaths",
        "Average Length",
        "Video Options",
        "Text",
        "Sound Options",
        "Object Name"
    };

    /** The description of each element, sorted by ID. */
    private final static String[] ELEMENT_DESCRIPTIONS = {
        "Unknown",
        "The game version.\nEx: 3.3.5a",
        "The game build number.\nEx: 12340",
        "The date at which the game build was built.",
        "The name of the realm that the report was submitted on.",
        "The client's language.",
        "The name of the character who submitted the report.",
        "The level of the character who submitted the report.",
        "The race of the character who submitted the report.",
        "The gender of the character who submitted the report.",
        "The class of the character who submitted the report.",
        "The name of the map in which the submitting character was.",
        "The name of the zone in which the submitting character was.",
        "The name of the area in which the submitting character was.",
        "The (x, y, z) coordinates at which the submitting character was. (May be incorrect.)",
        "The facing at which the submitting character was.",
        "The (walking?) speed of the submitting character.",
        "Unknown.",
        "The (x, y, z) coordinates at which the submitting character was. (May be incorrect.)",
        "The addons enabled at the time of submission.",
        "The addons disabled at the time of submission.",
        "The talents of the submitting character.",
        "The equipment IDs of the submitting character.",
        "The NPC being reported on.",
        "The location being reported at.",
        "The time of the report.",
        "The type of the report.",
        "The name of the survey.",
        "The ID of the survey.",
        "Unknown.",
        "The type of the survey.",
        "The time at which the survey was obtained (by the player?).",
        "The time at which the survey was submitted.",
        "Unknown.",
        "Unknown.",
        "Unknown.",
        "Unknown.",
        "The amount of recorded combats. (May be incorrect.)",
        "The amount of recorded deaths. (May be incorrect.)",
        "The average length of combat. (May be incorrect.)",
        "The video options at the time of submission.",
        "The message entered by the user at the time of submission.",
        "The sound options at the time of submission.",
        "The object name of the bug report. (May be incorrect.)"
    };

    /** The id of the tags from which the element's data was parsed. */
    private final int id;
    /** The data. */
    private final String data;

    /**
     * Construct a new BugReportElement with the specified
     * ID and data.
     *
     * @param id
     *         The id of the tags from which the element's data was parsed.
     *
     * @param data
     *         The data.
     */
    public BugReportElement(final int id, final String data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public String toString() {
        return ELEMENT_NAMES[id] + ": " + data;
    }

    /** @return The name of the element. */
    public String getName() {
        if(ELEMENT_NAMES.length <= id) {
            return ELEMENT_NAMES[id];
        } else {
            return "Unknown element. ID #" + id;
        }
    }

    /** @return The description of the element. */
    public String getDescription() {
        if(ELEMENT_NAMES.length <= id) {
            return ELEMENT_DESCRIPTIONS[id];
        } else {
            return "Unknown element. ID #" + id;
        }
    }
}
