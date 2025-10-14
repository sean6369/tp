package seedu.flowcli.exceptions;

public class ProjectAlreadyExistsException extends FlowCLIException{
    public ProjectAlreadyExistsException(){
        super("there already exists a project with this name");
    }
}
