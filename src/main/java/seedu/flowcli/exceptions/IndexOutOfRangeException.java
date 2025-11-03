package seedu.flowcli.exceptions;

public class IndexOutOfRangeException extends FlowCLIException {
    public IndexOutOfRangeException(int max) {
        super("Index out of range. Valid: 1.." + max + ".");
    }

    //Error message for project index validation errors.
    public IndexOutOfRangeException(int invalidIndex, int max) {
        super(buildIndexErrorMessage(invalidIndex, max, false));
    }

    //Error message for task index validation errors.
    public IndexOutOfRangeException(int invalidIndex, int max, boolean isTaskIndex) {
        super(buildIndexErrorMessage(invalidIndex, max, true));
    }

    private static String buildIndexErrorMessage(int invalidIndex, int max, boolean isTaskIndex) {
        if (max == 0) {
            String indexType = isTaskIndex ? "Task" : "Project";
            String itemType = isTaskIndex ? "tasks" : "projects";
            return String.format("%s Index %d is out of range. There are no %s available.", 
                    indexType, invalidIndex, itemType);
        } else {
            String indexType = isTaskIndex ? "Task" : "Project";
            return String.format("%s Index %d is out of range. Please use a number between 1 and %d.", 
                    indexType, invalidIndex, max);
        }
    }
}
