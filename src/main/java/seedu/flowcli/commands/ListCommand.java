package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.EmptyTaskListException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;

public class ListCommand extends Command {

    public ListCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        String trimmedArguments = arguments.trim();

        if (trimmedArguments.isEmpty()) {
            throw new MissingArgumentException();
        }

        if ("--all".equalsIgnoreCase(trimmedArguments)) {
            if (context.getProjects().isEmpty()) {
                throw new EmptyProjectListException();
            }
            context.getUi().showProjectList();
            context.getExportHandler().clearViewState();
            return true;
        }

        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        if (targetProject.isEmpty()) {
            throw new EmptyTaskListException();
        }

        context.getUi().showTaskList(targetProject);
        context.getExportHandler().clearViewState();
        return true;
    }
}
