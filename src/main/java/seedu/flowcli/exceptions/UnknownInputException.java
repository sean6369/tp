package seedu.flowcli.exceptions;

public class UnknownInputException extends FlowCLIException {
    public UnknownInputException() {
        super("Unknown command. Type 'help' to see all available commands.");
    }
}
