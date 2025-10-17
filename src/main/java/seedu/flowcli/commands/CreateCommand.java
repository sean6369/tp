package seedu.flowcli.commands;

import java.util.logging.Logger;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectAlreadyExistsException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;

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

        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();

        if (targetProject != null) {
            logger.warning(() -> "Project already exists for input args: \"" + arguments + "\"");
            throw new ProjectAlreadyExistsException();
        }

        String name = parsedArgument.getRemainingArgument();
        if (name == null) {
            logger.warning(() -> "Missing project name in arguments: \"" + arguments + "\"");
            throw new MissingArgumentException();
        }

        logger.info(() -> "Creating project: \"" + name + "\"");
        context.getProjects().addProject(name);
        context.getUi().showAddedProject();
        logger.fine(() -> "Project created and UI notified for: \"" + name + "\"");

        return true;
    }
}
