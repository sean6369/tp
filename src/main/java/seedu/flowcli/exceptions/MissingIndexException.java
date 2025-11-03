package seedu.flowcli.exceptions;

public class MissingIndexException extends FlowCLIException {
    public MissingIndexException() {
        super("Please provide a task index, e.g., 'mark 2 1'.");
    }
}
