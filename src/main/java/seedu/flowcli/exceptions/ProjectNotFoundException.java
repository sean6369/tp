package seedu.flowcli.exceptions;

//@@author Zhenzha0
public class ProjectNotFoundException extends FlowCLIException {
    public ProjectNotFoundException(String projectName) {
        super("Project '" + projectName + "' not found. Use 'list --all' to see available projects.");
    }
}
//@@author
