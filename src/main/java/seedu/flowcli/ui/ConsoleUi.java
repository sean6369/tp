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

    private String logo = "                                                                                                                                            \n" +
            "                                                                                                                                            \n" +
            "FFFFFFFFFFFFFFFFFFFFFFlllllll                                                                CCCCCCCCCCCCCLLLLLLLLLLL             IIIIIIIIII\n" +
            "F::::::::::::::::::::Fl:::::l                                                             CCC::::::::::::CL:::::::::L             I::::::::I\n" +
            "F::::::::::::::::::::Fl:::::l                                                           CC:::::::::::::::CL:::::::::L             I::::::::I\n" +
            "FF::::::FFFFFFFFF::::Fl:::::l                                                          C:::::CCCCCCCC::::CLL:::::::LL             II::::::II\n" +
            "  F:::::F       FFFFFF l::::l    ooooooooooo wwwwwww           wwwww           wwwwwwwC:::::C       CCCCCC  L:::::L                 I::::I  \n" +
            "  F:::::F              l::::l  oo:::::::::::oow:::::w         w:::::w         w:::::wC:::::C                L:::::L                 I::::I  \n" +
            "  F::::::FFFFFFFFFF    l::::l o:::::::::::::::ow:::::w       w:::::::w       w:::::w C:::::C                L:::::L                 I::::I  \n" +
            "  F:::::::::::::::F    l::::l o:::::ooooo:::::o w:::::w     w:::::::::w     w:::::w  C:::::C                L:::::L                 I::::I  \n" +
            "  F:::::::::::::::F    l::::l o::::o     o::::o  w:::::w   w:::::w:::::w   w:::::w   C:::::C                L:::::L                 I::::I  \n" +
            "  F::::::FFFFFFFFFF    l::::l o::::o     o::::o   w:::::w w:::::w w:::::w w:::::w    C:::::C                L:::::L                 I::::I  \n" +
            "  F:::::F              l::::l o::::o     o::::o    w:::::w:::::w   w:::::w:::::w     C:::::C                L:::::L                 I::::I  \n" +
            "  F:::::F              l::::l o::::o     o::::o     w:::::::::w     w:::::::::w       C:::::C       CCCCCC  L:::::L         LLLLLL  I::::I  \n" +
            "FF:::::::FF           l::::::lo:::::ooooo:::::o      w:::::::w       w:::::::w         C:::::CCCCCCCC::::CLL:::::::LLLLLLLLL:::::LII::::::II\n" +
            "F::::::::FF           l::::::lo:::::::::::::::o       w:::::w         w:::::w           CC:::::::::::::::CL::::::::::::::::::::::LI::::::::I\n" +
            "F::::::::FF           l::::::l oo:::::::::::oo         w:::w           w:::w              CCC::::::::::::CL::::::::::::::::::::::LI::::::::I\n" +
            "FFFFFFFFFFF           llllllll   ooooooooooo            www             www                  CCCCCCCCCCCCCLLLLLLLLLLLLLLLLLLLLLLLLIIIIIIIIII\n" +
            "                                                                                                                                            \n" +
            "                                                                                                                                            ";

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
        System.out.println(String.format("Now you have %d task in the %s.", targetProject.size(), targetProject.getProjectName()));
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

}
