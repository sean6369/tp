package seedu.flowcli;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;
import seedu.flowcli.command.CommandHandler;

public class FlowCLI {
    private ProjectList projects;
    private ConsoleUi ui;

    public FlowCLI() {
        projects = new ProjectList();
        ui = new ConsoleUi(projects);

        ui.welcome();

    }

    public void run() {
        new CommandHandler(projects, ui).handleCommands();
    }

    /**
     * Main entry-point for the java.FlowCLI.FlowCLI application.
     */
    public static void main(String[] args) {
        new FlowCLI().run();
    }
}
