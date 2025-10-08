package project;
import task.Task;
import task.TaskList;

public class Project {
    private String projectName;
    private String projectDescription;
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

    public void addTask(String Description) {
        projectTasks.addTask(Description);
    }

    public Task deleteTask(int index) {
        return projectTasks.delete(index);

    }

    public String toString() {
        return projectName + "\n" + projectTasks.render();
    }

}
