package seedu.flowcli.exceptions;

public class InvalidDateException extends FlowCLIException {
    public InvalidDateException(String dateString) {
        super("Invalid date format: " + dateString + ". Use YYYY-MM-DD format.");
    }
}
