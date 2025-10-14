package seedu.flowcli.exceptions;

public class FileWriteException extends FlowCLIException {
    public FileWriteException(String message) {
        super("Failed to write to file: " + message);
    }
}
