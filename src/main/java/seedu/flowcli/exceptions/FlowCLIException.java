package seedu.flowcli.exceptions;

/**
 * Base exception for all FlowCLI-specific errors.
 */
public class FlowCLIException extends Exception {
    public FlowCLIException(String message) {
        super(message);
    }

    public FlowCLIException(String message, Throwable cause) {
        super(message, cause);
    }
}
