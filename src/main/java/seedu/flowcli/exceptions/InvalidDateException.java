package seedu.flowcli.exceptions;

//@@author zeeeing
public class InvalidDateException extends FlowCLIException {

    public enum Reason {
        FORMAT, VALUE
    }

    private final Reason reason;

    private InvalidDateException(String message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    public InvalidDateException(String dateString) {
        this(buildFormatMessage(dateString), Reason.FORMAT);
    }

    public static InvalidDateException invalidFormat(String dateString) {
        return new InvalidDateException(dateString);
    }

    public static InvalidDateException invalidDate(String dateString) {
        return new InvalidDateException(buildValueMessage(dateString), Reason.VALUE);
    }

    public Reason getReason() {
        return reason;
    }

    private static String buildFormatMessage(String dateString) {
        return "Invalid date format: " + normalize(dateString) + ". Use YYYY-MM-DD format.";
    }

    private static String buildValueMessage(String dateString) {
        return "Invalid date: " + normalize(dateString) + ". Provide a valid calendar date.";
    }

    private static String normalize(String dateString) {
        return dateString == null ? "null" : dateString;
    }
}
