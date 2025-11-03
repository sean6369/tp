package seedu.flowcli.exceptions;

public class TaskAlreadyMarkedException extends FlowCLIException{
    public TaskAlreadyMarkedException() {
        super("Task is already marked");
    }
}
