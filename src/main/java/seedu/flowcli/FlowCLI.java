package seedu.flowcli;

import seedu.flowcli.commands.core.CommandHandler;
import seedu.flowcli.exceptions.DataCorruptedException;
import seedu.flowcli.exceptions.StorageException;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.storage.Storage;
import seedu.flowcli.ui.ConsoleUi;

//@@author Zhenzha0
public class FlowCLI {
    private final ProjectList projects;
    private final ConsoleUi ui;
    private final CommandHandler commandHandler;
    private final Storage storage;

    public FlowCLI() {
        this.storage = new Storage();
        this.projects = loadData();
        this.ui = new ConsoleUi(projects);
        this.commandHandler = new CommandHandler(projects, ui, storage);
    }

    /**
     * Loads project data from storage on startup.
     * Handles corrupted data and I/O errors gracefully.
     * 
     * @return ProjectList loaded from storage, or empty ProjectList if loading fails
     */
    private ProjectList loadData() {
        try {
            ProjectList loadedProjects = storage.load();
            System.out.println("Data loaded successfully from " + storage.getDataFilePath());
            return loadedProjects;
        } catch (DataCorruptedException e) {
            // Corrupted data - file has been backed up, start with empty list
            System.err.println("============================================================");
            System.err.println("WARNING: Data file is corrupted!");
            System.err.println("Details: " + e.getMessage());
            System.err.println("The corrupted file has been backed up.");
            System.err.println("Starting with an empty project list.");
            System.err.println("============================================================");
            return new ProjectList();
        } catch (StorageException e) {
            // I/O error during loading - show warning and continue
            System.err.println("============================================================");
            System.err.println("WARNING: Could not load data from storage.");
            System.err.println("Details: " + e.getMessage());
            System.err.println("Starting with an empty project list.");
            System.err.println("============================================================");
            return new ProjectList();
        }
    }

    public void run() {
        ui.printWelcomeMessage();
        commandHandler.handleCommands();
    }

    public static void main(String[] args) {
        new FlowCLI().run();
    }
}
//@@author
