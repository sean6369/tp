package seedu.flowcli.ui;

import java.util.List;

import seedu.flowcli.commands.utility.ProjectStatusAnalyzer;
import seedu.flowcli.commands.utility.ProjectStatusAnalyzer.ProjectStatus;
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
        System.out.println("Here is your list of projects:");

        if (projects.isEmpty()) {
            System.out.println("[No projects yet]");
            printLine();
            return;
        }

        for (int taskIdx = 0; taskIdx < projects.getProjectListSize(); taskIdx++) {
            Project project = projects.getProjectByIndex(taskIdx);
            System.out.println((taskIdx + 1) + ". " + project.getProjectName());

            String tasks = project.showAllTasks();
            if (tasks != null && !tasks.isEmpty()) {
                String[] taskLines = tasks.split("\\R");
                for (String taskLine : taskLines) {
                    if (!taskLine.isEmpty()) {
                        System.out.println("   " + taskLine);
                    }
                }
            }
        }

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
        System.out.println("Available Commands:\n");
        System.out.println("Tip: Projects are referenced by their index from `list --all`.\n");
        printHelpEntry("1. create-project <projectName>", "Creates a new project with the given name.");
        printHelpEntry("2. add-task <projectIndex> <taskDesc> [--priority low/medium/high] [--deadline YYYY-MM-DD]",
                "Adds a new task to the specified project with optional priority and deadline fields.");
        printHelpEntry("3. list --all", "Lists all existing projects.");
        printHelpEntry("4. list <projectIndex>", "Lists all task entries in the specified project.");
        printHelpEntry("5. mark <projectIndex> <taskIndex>", "Marks a task in a project as done.");
        printHelpEntry("6. unmark <projectIndex> <taskIndex>", "Marks a task in a project as not done.");
        printHelpEntry("7. delete-project <projectIndex> --confirm", "Deletes an entire project repository.");
        printHelpEntry("8. delete-task <projectIndex> <taskIndex>", "Deletes a task entry from the specified project.");
        printHelpEntry(
                "9. update-task <projectIndex> <taskIndex> [--description <newTaskDesc>] [--deadline YYYY-MM-DD]"
                        + "[--priority <low/medium/high>]",
                "Updates details of an existing task, such as description, deadline, or priority.");
        printHelpEntry("10. sort-tasks <--deadline/priority> <ascending/descending>",
                "Sorts existing tasks by deadline or priority.");
        printHelpEntry("11. filter-tasks --priority <low/medium/high>", "Filters existing tasks by priority.");
        printHelpEntry(
                "12. export-tasks <filename>.txt [projectIndex] [filter-tasks --priority <low/medium/high>]"
                        + "[sort-tasks <--deadline/priority> <ascending/descending>]",
                "Exports tasks to a TXT file. Defaults to all tasks if no project is specified.");
        printHelpEntry("13. status <projectIndex> / --all", "Shows project completion status.");
        printHelpEntry("14. help", "Shows this help message.");
        printHelpEntry("15. bye", "Exits the application.");
        System.out.println("Alternatively, you may provide the CLI with prompts to assist you with the following:\n");
        System.out.println("'create', 'add', 'list', 'mark', 'unmark', 'delete', 'update', 'sort', 'filter', 'export'");
        printLine();
    }

    private void printHelpEntry(String command, String description) {
        System.out.println(" " + command);
        System.out.println("  - " + description + "\n");
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
        System.out.println("Here are all your tasks across all projects:");
        for (Project project : projects.getProjectList()) {
            if (project.size() > 0) {
                System.out.println(project.getProjectName() + ":");
                System.out.print(project.showAllTasks());
            }
        }
        printLine();
    }

    /**
     * Displays the status of a single project with progress bar and
     * motivational message.
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
     * Displays the status of all projects with progress bars and motivational
     * messages.
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
        return status.getCompletedTasks() + "/" + status.getTotalTasks() + " tasks completed, "
                + String.format("%.0f%%", status.getPercentage());
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
