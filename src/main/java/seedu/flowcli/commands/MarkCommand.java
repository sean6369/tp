package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;

import java.util.logging.Logger;

public class MarkCommand extends Command {
    private static final Logger logger = Logger.getLogger(MarkCommand.class.getName());

    public MarkCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        assert context != null : "CommandContext cannot be null";
        logger.info("Executing MarkCommand with arguments: " + arguments);

        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        assert targetProject.getProjectTasks() != null : "Project task list cannot be null";
        logger.fine("Marking task in project: " + targetProject.getProjectName());

        Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

        assert idx >= 0 && idx < targetProject.size() : "Task index out of bounds";

        targetProject.getProjectTasks().mark(idx);

        assert targetProject.getProjectTasks().get(idx).isDone() : "Task should be marked as done";
        logger.info("Task marked successfully at index " + (idx + 1));

        context.getUi().showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), true);
        return true;
    }
}
