package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;

public class UnmarkCommand extends Command {

    public UnmarkCommand(String arguments) {
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

        Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

        targetProject.getProjectTasks().unmark(idx);
        context.getUi().showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), false);
        return true;
    }
}
