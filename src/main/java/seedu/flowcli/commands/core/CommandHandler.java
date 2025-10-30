package seedu.flowcli.commands.core;

import java.util.Scanner;
import java.util.logging.Logger;

import seedu.flowcli.commands.Command;
import seedu.flowcli.exceptions.FlowCLIException;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

public class CommandHandler {
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());
    private final ConsoleUi ui;
    private final CommandParser parser;
    private final CommandFactory factory;
    private final CommandContext context;
    private InteractivePromptHandler interactiveHandler;

    //@@author Zhenzha0
    public CommandHandler(ProjectList projects, ConsoleUi ui) {
        this.ui = ui;
        ExportCommandHandler exportHandler = new ExportCommandHandler(projects, ui);
        this.parser = new CommandParser();
        this.factory = new CommandFactory();
        this.context = new CommandContext(projects, ui, exportHandler);
        this.interactiveHandler = null; // Will be set in handleCommands
    }

    public void handleCommands() {
        Scanner scanner = new Scanner(System.in);
        // Initialize interactive handler with scanner after it's created
        this.interactiveHandler = new InteractivePromptHandler(context.getProjects(), scanner);

        try {
            boolean shouldContinue = true;
            while (shouldContinue && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) {
                    ui.printLine();
                    continue;
                }
                //@@author
                Command command = resolveCommand(line, scanner);
                if (command == null) {
                    // Interactive mode was cancelled, continue to next input
                    continue;
                }
                try {
                    shouldContinue = command.execute(context);
                } catch (FlowCLIException e) {
                    // Expected application errors - show user-friendly message
                    ui.showError(e.getMessage());
                } catch (Exception e) {
                    // Unexpected errors - log for debugging and show generic message
                    logger.log(java.util.logging.Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
                    ui.showUnexpectedError();
                }
            }
        } finally {
            scanner.close();
        }
    }

    private Command resolveCommand(String input, Scanner scanner) {
        CommandParser.ParsedCommand parsed = parser.parse(input);

        // Check if interactive mode should be triggered
        if (shouldUseInteractiveMode(parsed)) {
            String interactiveArgs = handleInteractiveMode(parsed.getType(), scanner);
            if (interactiveArgs == null) {
                // Interactive mode was cancelled, continue to next input
                return null;
            }
            // Create command with interactively collected arguments
            return factory.create(parsed.getType(), interactiveArgs);
        }

        // Normal command parsing
        return factory.create(parsed.getType(), parsed.getArguments());
    }

    /**
     * Determines if interactive mode should be used for the given parsed
     * command.
     *
     * @param parsed The parsed command
     * @return true if interactive mode should be triggered
     */
    //@@author Yxiang-828 zeeeing
     private boolean shouldUseInteractiveMode(CommandParser.ParsedCommand parsed) {
        // Trigger interactive mode for main commands with minimal/no arguments
        switch (parsed.getType()) {
        case ADD_TASK:
            return parsed.getArguments().trim().isEmpty();
        case CREATE_PROJECT:
            return parsed.getArguments().trim().isEmpty();
        case LIST:
            return parsed.getArguments().trim().isEmpty();
        case MARK:
        case UNMARK:
            return parsed.getArguments().trim().isEmpty();
        case DELETE:
            return parsed.getArguments().trim().isEmpty();
        case DELETE_PROJECT:
        case DELETE_TASK:
            // Don't trigger interactive mode for delete subcommands
            return false;
        case UPDATE_TASK:
            return parsed.getArguments().trim().isEmpty();
        case SORT_TASKS:
            return parsed.getArguments().trim().isEmpty();
        case FILTER_TASKS:
            return parsed.getArguments().trim().isEmpty();
        case EXPORT_TASKS:
            return parsed.getArguments().trim().isEmpty();
        case STATUS:
            return parsed.getArguments().trim().isEmpty();
        default:
            return false;
        }
    }

    /**
     * Handles interactive mode for the given command type.
     *
     * @param type The command type
     * @param scanner The input scanner
     * @return The constructed command arguments, or null if cancelled
     */
    private String handleInteractiveMode(CommandParser.CommandType type, Scanner scanner) {
        switch (type) {
        case ADD_TASK:
            return interactiveHandler.handleAddCommand();
        case CREATE_PROJECT:
            return interactiveHandler.handleCreateCommand();
        case LIST:
            return interactiveHandler.handleListCommand();
        case MARK:
            return interactiveHandler.handleMarkCommand();
        case UNMARK:
            return interactiveHandler.handleUnmarkCommand();
        case DELETE:
            return interactiveHandler.handleDeleteCommand();
        case UPDATE_TASK:
            return interactiveHandler.handleUpdateCommand();
        case SORT_TASKS:
            return interactiveHandler.handleSortCommand();
        case FILTER_TASKS:
            return interactiveHandler.handleFilterCommand();
        case EXPORT_TASKS:
            return interactiveHandler.handleExportCommand();
        case STATUS:
            return interactiveHandler.handleStatusCommand();
        default:
            return null;
        }
    }
}
