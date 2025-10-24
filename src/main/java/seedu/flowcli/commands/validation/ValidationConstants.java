package seedu.flowcli.commands.validation;

/**
 * Contains constants for command validation throughout the application.
 */
public class ValidationConstants {
    // Priority constants
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_HIGH = "high";
    public static final String[] VALID_PRIORITIES = { PRIORITY_LOW, PRIORITY_MEDIUM, PRIORITY_HIGH };

    // Priority integer values
    public static final int PRIORITY_LOW_VALUE = 1;
    public static final int PRIORITY_MEDIUM_VALUE = 2;
    public static final int PRIORITY_HIGH_VALUE = 3;

    // Filter type constants
    public static final String FILTER_TYPE_PRIORITY = "priority";
    public static final String[] VALID_FILTER_TYPES = { FILTER_TYPE_PRIORITY };

    // Sort field constants
    public static final String SORT_FIELD_DEADLINE = "deadline";
    public static final String SORT_FIELD_PRIORITY = "priority";
    public static final String[] VALID_SORT_FIELDS = { SORT_FIELD_DEADLINE, SORT_FIELD_PRIORITY };

    // Sort order constants
    public static final String SORT_ORDER_ASCENDING = "ascending";
    public static final String SORT_ORDER_DESCENDING = "descending";
    public static final String[] VALID_SORT_ORDERS = { SORT_ORDER_ASCENDING, SORT_ORDER_DESCENDING };

    // Command keywords
    public static final String KEYWORD_BY = "by";
    public static final String KEYWORD_FILTER = "filter";
    public static final String KEYWORD_SORT = "sort";
}
