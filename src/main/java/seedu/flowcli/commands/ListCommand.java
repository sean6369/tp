package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.EmptyTaskListException;
import seedu.flowcli.exceptions.ExtraArgumentException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

public class ListCommand extends Command {

    public ListCommand(String arguments) {
        super(arguments);
    }

    @Override
    //@@author zeeeing
    public boolean execute(CommandContext context) throws Exception {
        String trimmedArguments = arguments.trim();

        if (trimmedArguments.isEmpty()) {
            throw new MissingArgumentException();
        }

        ProjectList projects = context.getProjects();

        if ("--all".equalsIgnoreCase(trimmedArguments)) {
            if (projects.isEmpty()) {
                throw new EmptyProjectListException();
            }
            context.getUi().showProjectList();
            context.getExportHandler().clearViewState();
            return true;
        }

        ArgumentParser parsedArgument = new ArgumentParser(arguments, projects);
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        // Validate no extra parameters after project index
        String remaining = parsedArgument.getRemainingArgument();
        if (remaining != null && !remaining.trim().isEmpty()) {
            throw new ExtraArgumentException("Unexpected extra parameters: " + remaining);
        }

        if (targetProject.isEmpty()) {
            throw new EmptyTaskListException();
        }

        context.getUi().showTaskList(targetProject);
        context.getExportHandler().clearViewState();
        return true;
    }
}
