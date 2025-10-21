package seedu.flowcli.project;

import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskList;

import java.time.LocalDate;

public class Project {
    private String projectName;
    private String projectStatus;
    private TaskList projectTasks;

    public Project(String projectName) {
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

    public Task deleteTask(int index) {
        return projectTasks.delete(index);

    }

    public Task updateTask(int index, String newDescription, boolean updateDescription,
            LocalDate newDeadline, boolean updateDeadline, Integer newPriority, boolean updatePriority) {
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
        if(projectTasks==null){
            return projectName;
        }
        else{ 
            return projectName  + "\n" + projectTasks.render();
        }
    }

    public String showAllTasks() {
        return projectTasks.render();
    }
}
