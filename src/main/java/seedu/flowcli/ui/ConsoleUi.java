package seedu.flowcli.ui;

import java.util.List;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.commands.utility.ProjectStatusAnalyzer;
import seedu.flowcli.commands.utility.ProjectStatusAnalyzer.ProjectStatus;

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

    public void showUpdatedTask(Project targetProject, Task updatedTask) {
        printLine();
        System.out.println("Got it. I've updated this task in " + targetProject.getProjectName() + " : ");
        System.out.println(updatedTask);
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
        System.out.println("I see, another forgetful human who needs help remembering commands.");
        System.out.println();
        System.out.println("Available Commands:");
        System.out.println("Tip: Wrap multi-word project names in double quotes (e.g. \"Birthday Bash\").");
        System.out.println(" 1. create-project <project>               - Add a new project");
        System.out.println(" 2. add-task <project> <desc> [--priority low/medium/high] [--deadline YYYY-MM-DD]");
        System.out.println(" 3. list-all                        - List all projects");
        System.out.println(" 4. list <project>              - List tasks in a project");
        System.out.println(" 5. mark <project> <index>      - Mark task as done");
        System.out.println(" 6. unmark <project> <index>    - Mark task as not done");
        System.out.println(" 7. delete-project <project>            - Delete a project");
        System.out.println(" 8. delete-task <project> <index>    - Delete a task");
        System.out.println(" 9. update <project> <index> [--description <desc>] [--deadline YYYY-MM-DD|none]"
                + " [--priority low/medium/high] - Update a task");
        System.out.println("10. sort tasks by deadline/priority ascending/descending - Sort all tasks");
        System.out.println("11. filter tasks by priority <value> - Filter tasks by priority");
        System.out.println("12. filter tasks by project <name> - Filter tasks by project name");
        System.out.println("13. export tasks to <filename>.txt [<project>] [filter by <type> <value>] "
                + "[sort by <field> <order>] - Export tasks to TXT file");
        System.out.println("14. export tasks to <filename>.txt --all - Force export all tasks");
        System.out.println("15. status <project> / --all    - Show project completion status");
        System.out.println("16. help                        - Show this help message");
        System.out.println("17. bye                         - Exit the application");
        System.out.println();
        System.out.println("Hmph, if you're too lazy or forgetful to remember these commands...");
        System.out.println("just type the main command like 'add', 'create', 'list', 'mark', 'unmark', 'delete',");
        System.out.println("'update', 'sort', 'filter', 'export', or 'help'.");
        System.out.println("It's not like I'll guide you through everything step by step or anything!");
        System.out.println("Don't get the wrong idea - I'm only doing this because I have to...");
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

    public void showAllTasksAcrossProjects() {
        printLine();
        System.out.println("Here are all tasks across all projects:");
        for (Project project : projects.getProjectList()) {
            if (project.size() > 0) {
                System.out.println(project.getProjectName() + ":");
                System.out.print(project.showAllTasks());
            }
        }
        printLine();
    }

    /**
     * Displays the status of a single project with progress bar and motivational message.
     *
     * @param project The project to display status for
     */
    public void showProjectStatus(Project project) {
        printLine();

        ProjectStatus status = ProjectStatusAnalyzer.analyzeProject(project);

        if (status.hasNoTasks()) {
            System.out.println(project.getProjectName() + " - No tasks yet!");
            System.out.println("You haven't added any tasks to this project. What are you waiting for?");
            printLine();
            return;
        }

        System.out.println(project.getProjectName() + " - Project Status");
        System.out.println(formatStatusSummary(status));
        System.out.println(generateProgressBar(status.getPercentage()));
        System.out.println(getMotivationalMessage(status.getPercentage()));

        printLine();
    }

    /**
     * Displays the status of all projects with progress bars and motivational messages.
     *
     * @param projectList The project list containing all projects
     */
    public void showAllProjectsStatus(ProjectList projectList) {
        printLine();

        if (projectList.isEmpty()) {
            System.out.println("No projects found! Create one with 'create-project' command.");
            printLine();
            return;
        }

        System.out.println("All Projects Status:");
        System.out.println();

        for (Project project : projectList.getProjectList()) {
            ProjectStatus status = ProjectStatusAnalyzer.analyzeProject(project);

            if (status.hasNoTasks()) {
                System.out.println(project.getProjectName() + " - No tasks yet!");
                System.out.println("  You haven't added any tasks to this project.");
                System.out.println();
                continue;
            }

            System.out.println(project.getProjectName() + " - " + formatStatusSummary(status));
            System.out.println(generateProgressBar(status.getPercentage()));
            System.out.println(getMotivationalMessage(status.getPercentage()));
            System.out.println();
        }

        printLine();
    }

    /**
     * Formats the status summary line for a project.
     *
     * @param status The project status data
     * @return Formatted status string
     */
    private String formatStatusSummary(ProjectStatus status) {
        return status.getCompletedTasks() + "/" + status.getTotalTasks() +
                " tasks completed, " + String.format("%.0f%%", status.getPercentage());
    }

    /**
     * Generates a visual progress bar based on percentage.
     *
     * @param percentage The completion percentage (0-100)
     * @return A string representation of the progress bar
     */
    private String generateProgressBar(double percentage) {
        int barLength = 40;
        int filledLength = (int) (barLength * percentage / 100);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append("=");
            } else if (i == filledLength && filledLength < barLength) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("] ").append(String.format("%.0f%%", percentage));

        return bar.toString();
    }

    /**
     * Returns a motivational message based on completion percentage.
     *
     * @param percentage The completion percentage (0-100)
     * @return A motivational message
     */
    private String getMotivationalMessage(double percentage) {
        if (percentage <= 25) {
            return "You are kinda cooked, start doing your tasks!";
        } else if (percentage <= 50) {
            return "You gotta lock in and finish all tasks!";
        } else if (percentage <= 75) {
            return "We are on the right track, keep completing your tasks!";
        } else {
            return "We are finishing all tasks!! Upzzz!";
        }
    }
}
