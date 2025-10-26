package seedu.flowcli.exceptions;

public class InvalidIndexFormatException extends FlowCLIException {
    public InvalidIndexFormatException(String index, String indexType) {
        super("Invalid " + indexType + " index: " + index + 
              ". Use the numeric " + indexType + " index shown in 'list --all'.");
    }
}
