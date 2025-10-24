package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectNotFoundException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

public class ListCommand extends Command {

    public ListCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ProjectList projectList = context.getProjects();
        String trimmedArguments = arguments.trim();

        if (trimmedArguments.isEmpty()) {
            throw new MissingArgumentException();
        }

        if ("--all".equalsIgnoreCase(trimmedArguments)) {
            context.getUi().showProjectList();
            context.getExportHandler().clearViewState();
            return true;
        }

        String[] parts = trimmedArguments.split("\\s+", 2);
        String firstToken = parts[0];

        try {
            int projectIndex = Integer.parseInt(firstToken);
            if (projectIndex < 1 || projectIndex > projectList.getProjectListSize()) {
                throw new IndexOutOfRangeException(projectList.getProjectListSize());
            }

            Project targetProject = projectList.getProjectByIndex(projectIndex - 1);
            context.getUi().showTaskList(targetProject);
            context.getExportHandler().clearViewState();
            return true;
        } catch (NumberFormatException ignored) {
        }

        ArgumentParser parsedArgument = new ArgumentParser(arguments, projectList);
        Project targetProject = parsedArgument.getTargetProject();
        if (targetProject != null) {
            context.getUi().showTaskList(targetProject);
            context.getExportHandler().clearViewState();
            return true;
        }

        if ("--all".equalsIgnoreCase(parsedArgument.getParsedProjectName())) {
            context.getUi().showProjectList();
            context.getExportHandler().clearViewState();
            return true;
        }

        throw new ProjectNotFoundException(parsedArgument.getParsedProjectName());
    }
}
