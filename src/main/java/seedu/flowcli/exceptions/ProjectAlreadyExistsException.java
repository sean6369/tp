package seedu.flowcli.exceptions;

public class ProjectAlreadyExistsException extends FlowCLIException {
    public ProjectAlreadyExistsException(String projectName) {
        super("A project with the name '" + projectName + "' already exists.");
    }
}
