package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidIndexFormatException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;

//@@author Zhenzha0
public class StatusCommand extends Command {

    public StatusCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        String trimmedArguments = arguments.trim();

        if (trimmedArguments.isEmpty()) {
            throw new MissingArgumentException();
        }

        if (context.getProjects().isEmpty()) {
            throw new EmptyProjectListException();
        }

        if ("--all".equalsIgnoreCase(trimmedArguments)) {
            context.getUi().showAllProjectsStatus(context.getProjects());
            return true;
        }

        // Check if a specific project is targeted
        if (parsedArgument.getTargetProject() != null) {
            Project project = parsedArgument.getTargetProject();
            context.getUi().showProjectStatus(project);
            return true;
        }

        Integer projectIndex = parsedArgument.getTargetProjectIndex();
        if (projectIndex != null) {
            throw new IndexOutOfRangeException(context.getProjects().getProjectListSize());
        }

        if (parsedArgument.hasNonNumericProjectToken()) {
            throw new InvalidIndexFormatException(parsedArgument.getParsedProjectName(), "project");
        }

        // No arguments provided - should not happen as interactive mode handles
        // it
        context.getUi().showAllProjectsStatus(context.getProjects());
        return true;
    }
}
//@@author
