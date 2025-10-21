package seedu.flowcli.exceptions;

public class EmptyProjectListException extends FlowCLIException {
    public EmptyProjectListException() {
        super("Your Project List is empty. Add a task first.");
    }
}
