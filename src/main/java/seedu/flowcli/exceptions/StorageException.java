package seedu.flowcli.exceptions;

/**
 * Represents an error during storage operations (save/load).
 */
//@@author Zhenzha0
public class StorageException extends Exception {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
//@@author

