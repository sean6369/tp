package seedu.flowcli.exceptions;

public class MissingDescriptionException extends FlowCLIException {
    public MissingDescriptionException() {
        super("Bro stop trolling, you only entered the command and target project...");
    }
}
