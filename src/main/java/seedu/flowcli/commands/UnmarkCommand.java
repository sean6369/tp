package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;

import java.util.logging.Logger;

public class UnmarkCommand extends Command {
    private static final Logger logger = Logger.getLogger(UnmarkCommand.class.getName());

    public UnmarkCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        logger.fine("Executing UnmarkCommand with arguments: " + arguments);

        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        logger.fine("Unmarking task in project: " + targetProject.getProjectName());

        Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

        targetProject.getProjectTasks().unmark(idx);

        logger.fine("Task unmarked successfully at index " + (idx + 1));
        context.getUi().showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), false);
        return true;
    }
}
