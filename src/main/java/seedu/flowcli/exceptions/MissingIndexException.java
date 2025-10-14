package seedu.flowcli.exceptions;

public class MissingIndexException extends FlowCLIException {
    public MissingIndexException() {
        super("Please provide an index, e.g., 'mark 2'.");
    }
}
