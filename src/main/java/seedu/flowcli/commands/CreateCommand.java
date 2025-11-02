package seedu.flowcli.commands;

import java.util.logging.Logger;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectAlreadyExistsException;
import seedu.flowcli.exceptions.ProjectNotFoundException;

public class CreateCommand extends Command {
    private static final Logger logger = Logger.getLogger(CreateCommand.class.getName());

    public CreateCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        assert context != null : "CommandContext must not be null";
        assert arguments != null : "CreateCommand arguments must not be null";
        logger.fine(() -> "CreateCommand.execute() called with args=\"" + arguments + "\"");

        String name = arguments.trim();
        if (name.isEmpty()) {
            logger.warning(() -> "Missing project name in arguments: \"" + arguments + "\"");
            throw new MissingArgumentException();
        }

        try {
            context.getProjects().getProject(name);
            logger.warning(() -> "Project already exists for input args: \"" + arguments + "\"");
            throw new ProjectAlreadyExistsException(name);
        } catch (ProjectNotFoundException e) {
            // Project doesn't exist, which is what we want - continue with creation
        }

        logger.fine(() -> "Creating project: \"" + name + "\"");
        context.getProjects().addProject(name);
        context.getUi().showAddedProject();
        logger.fine(() -> "Project created and UI notified for: \"" + name + "\"");

        return true;
    }
}
