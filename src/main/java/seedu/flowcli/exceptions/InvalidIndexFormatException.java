package seedu.flowcli.exceptions;

public class InvalidIndexFormatException extends FlowCLIException {
    public InvalidIndexFormatException(String index, String indexType) {
        super("Invalid " + indexType + " index: " + index + 
              ". Use the numeric index shown in the list.");
    }
}
