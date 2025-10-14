package seedu.flowcli.commands.core;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

/**
 * Provides shared resources required by commands during execution.
 */
public class CommandContext {
    private final ProjectList projects;
    private final ConsoleUi ui;
    private final ExportCommandHandler exportHandler;

    public CommandContext(ProjectList projects, ConsoleUi ui, ExportCommandHandler exportHandler) {
        this.projects = projects;
        this.ui = ui;
        this.exportHandler = exportHandler;
    }

    public ProjectList getProjects() {
        return projects;
    }

    public ConsoleUi getUi() {
        return ui;
    }

    public ExportCommandHandler getExportHandler() {
        return exportHandler;
    }
}
