package seedu.flowcli.commands.core;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.storage.Storage;
import seedu.flowcli.ui.ConsoleUi;

/**
 * Provides shared resources required by commands during execution.
 */
//@@author zeeeing
public class CommandContext {
    private final ProjectList projects;
    private final ConsoleUi ui;
    private final ExportCommandHandler exportHandler;
    private final Storage storage;

    public CommandContext(ProjectList projects, ConsoleUi ui, ExportCommandHandler exportHandler, Storage storage) {
        this.projects = projects;
        this.ui = ui;
        this.exportHandler = exportHandler;
        this.storage = storage;
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

    public Storage getStorage() {
        return storage;
    }
}
