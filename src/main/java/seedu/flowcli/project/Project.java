package seedu.flowcli.project;

import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskList;
import seedu.flowcli.exceptions.IndexOutOfRangeException;

import java.time.LocalDate;

public class Project {
    private String projectName;
    private String projectStatus;
    private TaskList projectTasks;

    public Project(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        this.projectName = projectName;
        projectTasks = new TaskList();
    }

    public int size() {
        return projectTasks.size();
    }

    public String getProjectName() {
        return projectName;
    }

    public TaskList getProjectTasks() {
        return projectTasks;
    }

    public boolean isEmpty() {
        return projectTasks.isEmpty();
    }

    public void addTask(String description) {
        projectTasks.addTask(description);
    }

    public void addTask(String description, LocalDate deadline, int priority) {
        projectTasks.addTask(description, deadline, priority);
    }

    public Task deleteTask(int index) throws IndexOutOfRangeException {
        return projectTasks.delete(index);

    }

    public Task updateTask(int index, String newDescription, boolean updateDescription,
            LocalDate newDeadline, boolean updateDeadline, Integer newPriority, boolean updatePriority)
            throws IndexOutOfRangeException {
        Task task = projectTasks.get(index);
        if (updateDescription) {
            task.setDescription(newDescription);
        }
        if (updateDeadline) {
            task.setDeadline(newDeadline);
        }
        if (updatePriority) {
            task.setPriority(newPriority);
        }
        return task;
    }

    public String toString() {
        return projectName + "\n" + projectTasks.render();
    }

    public String showAllTasks() {
        return projectTasks.render();
    }
}
