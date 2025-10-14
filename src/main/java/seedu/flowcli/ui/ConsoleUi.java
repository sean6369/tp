package seedu.flowcli.ui;

import java.util.List;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

/**
 * Handles all user interface interactions for the FlowCLI application. This
 * class provides methods to display messages, task lists, and errors to the
 * user.
 */
public class ConsoleUi {

    private static final String CHATBOT_NAME = "FlowCLI";
    private ProjectList projects;

    public ConsoleUi(ProjectList projects) {
        this.projects = projects;
    }

    public void printLine() {
        System.out.println("____________________________________________________________");
    }

    public void printWelcomeMessage() {
        System.out.println(CHATBOT_NAME);
        printLine();
        System.out.println("Hello! I'm " + CHATBOT_NAME + ", a fast, minimal CLI project task manager.");
        System.out.println("What can I do for you today?");
        printLine();
    }

    public void printByeMessage() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    public void showMarked(String projectName, Task t, boolean nowDone) {
        printLine();
        System.out.println(nowDone ? "Nice! I've marked this task under " + projectName + " as done:"
                : "OK, I've marked this task under " + projectName + " as not done yet:");
        System.out.println("  " + t);
        printLine();
    }

    public void showCurrentProjectListSize() {
        System.out.println(String.format("Now you have %d projects.", projects.getProjectListSize()));
    }
    
    public void showAddedProjectDescription(Project targetProject ){
        System.out.println("added the description to the project: " + "'" + targetProject.getProjectName() +"'");
    }

    public void showCurrentTaskListSize(Project targetProject) {
        System.out.println(
                String.format("Now you have %d task in the %s.", targetProject.size(), targetProject.getProjectName()));
    }

    public void showAddedProject() {
        printLine();
        System.out.println("Got it. I've added this project: ");
        System.out.println(projects.getProjectList().get(projects.getProjectListSize() - 1));
        showCurrentProjectListSize();
        printLine();
    }

    public void showAddedTask(Project targetProject) {
        printLine();
        System.out.println("Got it. I've added this task in " + targetProject.getProjectName() + " : ");
        System.out.println(targetProject.getProjectTasks().get(targetProject.getProjectTasks().size() - 1));
        showCurrentTaskListSize(targetProject);
        printLine();
    }

    public void showDeletedProject(Project deletedProject) {
        printLine();
        System.out.println("Got it. I've deleted this project: ");
        System.out.println(deletedProject);
        showCurrentProjectListSize();
        printLine();
    }

    public void showDeletedTask(Project targetProject, Task deletedTask) {
        printLine();
        System.out.println("Got it. I've deleted this task in " + targetProject.getProjectName() + " : ");
        System.out.println(deletedTask);
        showCurrentTaskListSize(targetProject);
        printLine();
    }

    public void showProjectList() {
        printLine();
        System.out.println("Here is your whole project list:");
        System.out.print(projects.render());
        printLine();
    }

    public void showTaskList(Project targetProject) {
        printLine();
        System.out.println("Here are the tasks in " + targetProject.getProjectName() + ":");
        System.out.print(targetProject.showAllTasks());
        printLine();
    }

    public void showSortedTaskList(Project targetProject, String sortBy, String order) {
        printLine();
        System.out.println("Tasks in " + targetProject.getProjectName() + " sorted by " + sortBy + " (" + order + "):");
        System.out.print(targetProject.showAllTasks());
        printLine();
    }

    public void showHelp() {
        printLine();
        System.out.println("Available Commands:");
        System.out.println(" 1. add <project>               - Add a new project");
        System.out.println(" 2. add <project> <desc> [--priority low/medium/high] [--deadline YYYY-MM-DD]");
        System.out.println(" 3. list                        - List all projects");
        System.out.println(" 4. list <project>              - List tasks in a project");
        System.out.println(" 5. mark <project> <index>      - Mark task as done");
        System.out.println(" 6. unmark <project> <index>    - Mark task as not done");
        System.out.println(" 7. delete <project>            - Delete a project");
        System.out.println(" 8. delete <project> <index>    - Delete a task");
        System.out.println(" 9. sort tasks by deadline/priority ascending/descending - Sort all tasks");
        System.out.println("10. filter tasks by priority <value> - Filter tasks by priority");
        System.out.println("11. filter tasks by project <name> - Filter tasks by project name");
        System.out.println("12. export tasks to <filename>.txt [<project>] [filter by <type> <value>] "
                + "[sort by <field> <order>] - Export tasks to TXT file");
        System.out.println("13. export tasks to <filename>.txt --all - Force export all tasks");
        System.out.println("14. help                        - Show this help message");
        System.out.println("15. bye                         - Exit the application");
        printLine();
    }

    public void showGlobalSortedTasks(List<TaskWithProject> tasks, String field, String order) {
        printLine();
        System.out.println("Sorted all tasks by " + field + " " + order + ":");
        for (TaskWithProject task : tasks) {
            System.out.println(task.toString());
        }
        printLine();
    }

    public void showGlobalFilteredTasks(List<TaskWithProject> tasks, String type, String value) {
        printLine();
        System.out.println("Filtered tasks by " + type + " " + value + ":");
        for (TaskWithProject task : tasks) {
            System.out.println(task.toString());
        }
        printLine();
    }

    public void showExportSuccess(String filename, int taskCount) {
        printLine();
        System.out.println("Successfully exported " + taskCount + " tasks to " + filename);
        printLine();
    }

}
