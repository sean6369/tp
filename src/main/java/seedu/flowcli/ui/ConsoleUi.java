package seedu.flowcli.ui;

import seedu.flowcli.task.Task;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.project.Project;

/**
 * Handles all user interface interactions for the FlowCLI application.
 * This class provides methods to display messages, task lists, and errors to the user.
 */
public class ConsoleUi {


    private ProjectList projects;
    private String line = "____________________________________________________________";

    /*
    private String logo = " _______  _        _______           _______  _       _________\n" +
            "(  ____ \\( \\      (  ___  )|\\     /|(  ____ \\( \\      \\__   __/\n" +
            "| (    \\/| (      | (   ) || )   ( || (    \\/| (         ) (   \n" +
            "| (__    | |      | |   | || | _ | || |      | |         | |   \n" +
            "|  __)   | |      | |   | || |( )| || |      | |         | |   \n" +
            "| (      | |      | |   | || || || || |      | |         | |   \n" +
            "| )      | (____/\\| (___) || () () || (____/\\| (____/\\___) (___\n" +
            "|/       (_______/(_______)(_______)(_______/(_______/\\_______/\n" + "\n";

     */
    private String logo = "FLOWCLI";

    private String helloMessage = "Hello! I'm FlowCLI, a fast minimal CLI task manager";
    private String byeMessage = "Bye. Hope to see you again soon!";

    public ConsoleUi(ProjectList projects) {
        this.projects = projects;
    }






    public void printHelloMessage() {
        System.out.println(helloMessage);
    }


    public void printLine() {
        System.out.println(line);
    }

    public void printLogo() {
        System.out.println(logo);
    }


    public void welcome() {
        printLogo();
        printLine();
        printHelloMessage();
    }

    public void bye() {
        printLine();
        System.out.println(byeMessage);
        printLine();
    }

    public void showMarked(String projectName, Task t, boolean nowDone) {
        printLine();
        System.out.println(nowDone
                ? "Nice! I've marked this task under " + projectName + " as done:"
                : "OK, I've marked this task under " + projectName + " as not done yet:");
        System.out.println("  " + t);
        printLine();
    }

    public void showCurrentProjectListSize() {
        System.out.println(String.format("Now you have %d projects.", projects.getProjectListSize()));
    }

    public void showCurrentTaskListSize(Project targetProject) {
        System.out.println(String.format("Now you have %d task in the %s.", targetProject.size(),
                targetProject.getProjectName()));
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

    public void showHelp() {
        printLine();
        System.out.println("Available Commands:");
        System.out.println("  add project <name>          - Add a new project");
        System.out.println("  add task <project> <desc>   - Add a task to a project");
        System.out.println("  list                        - List all projects");
        System.out.println("  list <project>              - List tasks in a project");
        System.out.println("  mark <project> <index>      - Mark task as done");
        System.out.println("  unmark <project> <index>    - Mark task as not done");
        System.out.println("  delete project <name>       - Delete a project");
        System.out.println("  delete task <project> <idx> - Delete a task");
        System.out.println("  help                        - Show this help message");
        System.out.println("  bye                         - Exit the application");
        printLine();
    }

}
