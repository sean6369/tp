package seedu.flowcli.exceptions;

public class EmptyTaskListException extends FlowCLIException {
    public EmptyTaskListException() {
        super("Your Task List is empty. Add a task first.");
    }
}
