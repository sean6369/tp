package seedu.flowcli.exceptions;

public class IndexOutOfRangeException extends FlowCLIException {
    public IndexOutOfRangeException(int max) {
        super("Index out of range. Valid: 1.." + max + ".");
    }
}
