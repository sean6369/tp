package seedu.FlowCLI;

import seedu.FlowCLI.project.ProjectList;
import seedu.FlowCLI.ui.ConsoleUi;
import seedu.FlowCLI.command.CommandHandler;

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
