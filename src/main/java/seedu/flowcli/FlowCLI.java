package seedu.flowcli;

import seedu.flowcli.commands.core.CommandHandler;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

public class FlowCLI {
    private final ProjectList projects;
    private final ConsoleUi ui;
    private final CommandHandler commandHandler;

    public FlowCLI() {
        this.projects = new ProjectList();
        this.ui = new ConsoleUi(projects);
        this.commandHandler = new CommandHandler(projects, ui);
    }

    public void run() {
        ui.printWelcomeMessage();
        commandHandler.handleCommands();
    }

    public static void main(String[] args) {
        new FlowCLI().run();
    }
}
