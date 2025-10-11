package seedu.flowcli.exception;

public class FlowCLIExceptions {
    public static class MissingArgumentException extends Exception {
        public MissingArgumentException() {
            super("Couldn't find any project with such name");
        }
    }

    public static class MissingIndexException extends Exception {
        public MissingIndexException() {
            super("Please provide an index, e.g., 'mark 2'.");
        }
    }

    public static class IndexOutOfRangeException extends Exception {
        public IndexOutOfRangeException(int max) {
            super("Index out of range. Valid: 1.." + max + ".");
        }
    }

    public static class EmptyProjectListException extends Exception {
        public EmptyProjectListException() {
            super("Your Project List is empty. Add a task first.");
        }
    }

    public static class EmptyTaskListException extends Exception {
        public EmptyTaskListException() {
            super("Your Task list is empty. Add a task first.");
        }
    }

    public static class MissingDescriptionException extends Exception {
        public MissingDescriptionException() {
            super("Bro stop trolling, you only entered the command and target project...");
        }
    }

    public static class UnknownInputException extends Exception {
        public UnknownInputException() {
            super("Unknown command. Type 'help' to see all available commands.");
        }
    }

    public static class InvalidArgumentException extends Exception {
        public InvalidArgumentException(String message) {
            super(message);
        }
    }

    public static class FileWriteException extends Exception {
        public FileWriteException(String message) {
            super("Failed to write to file: " + message);
        }
    }

    public static class NoViewToExportException extends Exception {
        public NoViewToExportException() {
            super("No view to export. Please filter, sort, or list tasks first, or specify export parameters.");
        }
    }
}
