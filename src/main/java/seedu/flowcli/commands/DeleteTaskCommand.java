package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.task.Task;

public class DeleteTaskCommand extends Command {

    public DeleteTaskCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        if (targetProject == null) {
            Integer projectIndex = parsedArgument.getTargetProjectIndex();
            if (projectIndex != null) {
                throw new IndexOutOfRangeException(context.getProjects().getProjectListSize());
            }
            if (parsedArgument.hasNonNumericProjectToken()) {
                throw new InvalidArgumentException(String.format(ArgumentParser.INVALID_PROJECT_INDEX_MESSAGE,
                        parsedArgument.getParsedProjectName()));
            }
            throw new MissingArgumentException();
        }

        String remaining = parsedArgument.getRemainingArgument();
        if (remaining == null || remaining.trim().isEmpty()) {
            throw new MissingArgumentException();
        }

        String[] parts = remaining.trim().split("\\s+");
        Integer index = CommandParser.parseIndexOrNull(parts[0], targetProject.size());
        Task deletedTask = targetProject.deleteTask(index);
        context.getUi().showDeletedTask(targetProject, deletedTask);
        return true;
    }
}
