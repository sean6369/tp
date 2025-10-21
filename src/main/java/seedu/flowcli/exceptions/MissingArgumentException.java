package seedu.flowcli.exceptions;

public class MissingArgumentException extends FlowCLIException {
    public MissingArgumentException() {
        super("Couldn't find any project with such name");
    }
}
