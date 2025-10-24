package seedu.flowcli.commands;

import java.util.List;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.commands.utility.TaskFilter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.task.TaskWithProject;

public class FilterCommand extends Command {

    public FilterCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        String trimmed = arguments.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high> or filter-tasks --project <projectName>");
        }

        if (!trimmed.startsWith("--")) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high> or filter-tasks --project <projectName>");
        }

        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex == -1) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high> or filter-tasks --project <projectName>");
        }

        String option = trimmed.substring(0, spaceIndex);
        String valuePart = trimmed.substring(spaceIndex + 1).trim();
        if (valuePart.isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high> or filter-tasks --project <projectName>");
        }

        String type = option.substring(2).toLowerCase();
        String value = normalizeValue(valuePart);

        if (ValidationConstants.FILTER_TYPE_PRIORITY.equals(type)) {
            CommandValidator.validatePriority(value);

            TaskFilter filter = new TaskFilter(context.getProjects(), value, null);
            List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
            context.getUi().showGlobalFilteredTasks(filteredTasks, type, value);

            context.getExportHandler().updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                    "filtered by " + type + " " + value);
        } else if (ValidationConstants.FILTER_TYPE_PROJECT.equals(type)) {
            Integer zeroBasedIndex;
            try {
                zeroBasedIndex = CommandParser.parseIndexOrNull(value, context.getProjects().getProjectListSize());
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException(String.format(ArgumentParser.INVALID_PROJECT_INDEX_MESSAGE, value));
            }

            Project project = context.getProjects().getProjectByIndex(zeroBasedIndex);
            String projectName = project.getProjectName();

            TaskFilter filter = new TaskFilter(context.getProjects(), null, projectName);
            List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
            context.getUi().showGlobalFilteredTasks(filteredTasks, type, projectName);

            context.getExportHandler().updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                    "filtered by " + type + " " + projectName);
        } else {
            CommandValidator.validateFilterType(type);
            throw new InvalidArgumentException("Invalid filter type. Use: priority or project");
        }
        return true;
    }

    private String normalizeValue(String rawValue) {
        String value = rawValue.trim();
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\\\"", "\"");
    }
}
