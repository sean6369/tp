package seedu.flowcli.task;

/**
 * Represents a task with its associated project information.
 * Used for operations that work across multiple projects (filtering, sorting, exporting).
 */
public class TaskWithProject {
    private final String projectName;
    private final Task task;

    public TaskWithProject(String projectName, Task task) {
        this.projectName = projectName;
        this.task = task;
    }

    public String getProjectName() {
        return projectName;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return projectName + ": " + task.toString();
    }
}
