package com.valkryst.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BugReportType {
    /** The total number of feedback field tags. */
    private final static byte TOTAL_FEEDBACK_FIELDS = 43;
    /** The amount of elements with potentially useless data. */
    @Getter private int uselessElements;
    /** The parsed elements. */
    private final List<BugReportElement> elements = new ArrayList<>();

    /**
     * todo JavaDoc
     */
    public BugReportType(final String data) throws Exception {
        byte uselessElements = 0;

        for(int i = 1 ; i < TOTAL_FEEDBACK_FIELDS ; i++ ) {
            final String tagID = String.valueOf(i);
            String parsedData = parseTagString(tagID, data);

            // Handle Category #1 parsing:
            if (i == 33) {
                switch (parsedData) {
                    case "1": {
                        parsedData = "Extremely Vague";
                        break;
                    }
                    case "2": {
                        parsedData = "Somewhat Vague";
                        break;
                    }
                    case "3": {
                        parsedData = "Fairly Clear";
                        break;
                    }
                    case "4": {
                        parsedData = "Perfectly Clear";
                        break;
                    }
                    default: {
                        parsedData = "N/A";
                    }
                }
            }

            // Handle Category #2 parsing:
            if (i == 34) {
                switch (parsedData) {
                    case "1": {
                        parsedData = "Easy";
                        break;
                    }
                    case "2": {
                        parsedData = "Manageable";
                        break;
                    }
                    case "3": {
                        parsedData = "Challenging";
                        break;
                    }
                    case "4": {
                        parsedData = "Hard";
                        break;
                    }
                    default: {
                        parsedData = "N/A";
                    }
                }
            }

            // Handle Category #3 parsing:
            if (i == 35) {
                switch (parsedData) {
                    case "1": {
                        parsedData = "Awful";
                        break;
                    }
                    case "2": {
                        parsedData = "Bad";
                        break;
                    }
                    case "3": {
                        parsedData = "Good";
                        break;
                    }
                    case "4": {
                        parsedData = "Awesome";
                        break;
                    }
                    default: {
                        parsedData = "N/A";
                    }
                }
            }

            // Handle Category #4 parsing:
            if (i == 36) {
                switch (parsedData) {
                    case "1": {
                        parsedData = "Not at all.";
                        break;
                    }
                    case "2": {
                        parsedData = "Not very fun.";
                        break;
                    }
                    case "3": {
                        parsedData = "Pretty fun.";
                        break;
                    }
                    case "4": {
                        parsedData = "A lot of fun.";
                        break;
                    }
                    default: {
                        parsedData = "N/A";
                    }
                }
            }

            // Construct and add element to the list:
            final BugReportElement element = new BugReportElement(i, parsedData);
            elements.add(element);

            if (parsedData.equals("N/A")) {
                uselessElements++;
            }
        }

        this.uselessElements = uselessElements;
    }

    /**
     * Attempts to parse data for the first-found instance
     * of the specified tag.
     *
     * @param tag
     *         The tag "<tag></tag>" to be parsed.
     *
     * @param data
     *         The data containing the tags to be parsed.
     *
     * @return
     *         Either the text parsed from the specified
     *         tag, or "N/A" if no instance of the tag
     *         could be found.
     */
    private String parseTagString(final String tag, final String data) {
        try {
            final Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">");
            final Matcher matcher = pattern.matcher(data);

            if(matcher.find()) {
                return matcher.group(1);
            }
        } catch(final NullPointerException ignored) {}

        return "N/A";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final BugReportElement element : elements) {
            sb.append(element.toString()).append("\n");
        }

        return sb.toString();
    }
}
