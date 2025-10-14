package seedu.flowcli.exceptions;

public class EmptyTaskListException extends FlowCLIException {
    public EmptyTaskListException() {
        super("Your Task list is empty. Add a task first.");
    }
}
