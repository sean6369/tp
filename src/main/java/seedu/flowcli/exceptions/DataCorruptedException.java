package seedu.flowcli.exceptions;

/**
 * Represents an error when the storage file contains corrupted or invalid data.
 */
public class DataCorruptedException extends Exception {
    public DataCorruptedException(String message) {
        super(message);
    }

    public DataCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}

