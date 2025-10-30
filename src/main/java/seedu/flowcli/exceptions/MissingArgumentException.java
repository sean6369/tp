package seedu.flowcli.exceptions;

//@@author zeeeing
public class MissingArgumentException extends FlowCLIException {
    private static final String DEFAULT_MESSAGE = "Missing argument for the command.";

    public MissingArgumentException() {
        this(DEFAULT_MESSAGE);
    }

    public MissingArgumentException(String message) {
        super(message);
    }
}
