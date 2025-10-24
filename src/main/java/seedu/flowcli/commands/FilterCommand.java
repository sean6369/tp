package seedu.flowcli.commands;

import java.util.List;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.commands.utility.TaskFilter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.InvalidArgumentException;
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
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high>");
        }

        String type;
        String value;

        if (trimmed.startsWith("tasks by")) {
            String[] parts = trimmed.split("\\s+", 4);
            if (parts.length < 4) {
                throw new InvalidArgumentException(
                        "Invalid filter command. Use: filter tasks by priority <value> or project <name>");
            }
            type = parts[2].toLowerCase();
            value = normalizeValue(parts[3]);
        } else {
            if (!trimmed.startsWith("--")) {
                throw new InvalidArgumentException(
                        "Invalid filter command. Use: filter-tasks --priority <low/medium/high>");
            }

            int spaceIndex = trimmed.indexOf(' ');
            if (spaceIndex == -1) {
                throw new InvalidArgumentException(
                        "Invalid filter command. Use: filter-tasks --priority <low/medium/high>");
            }

            String option = trimmed.substring(0, spaceIndex);
            String valuePart = trimmed.substring(spaceIndex + 1).trim();
            if (valuePart.isEmpty()) {
                throw new InvalidArgumentException(
                        "Invalid filter command. Use: filter-tasks --priority <low/medium/high>");
            }

            type = option.substring(2).toLowerCase();
            value = normalizeValue(valuePart);
        }

        if (ValidationConstants.FILTER_TYPE_PRIORITY.equals(type)) {
            CommandValidator.validatePriority(value);

            TaskFilter filter = new TaskFilter(context.getProjects(), value, null);
            List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
            context.getUi().showGlobalFilteredTasks(filteredTasks, type, value);

            context.getExportHandler().updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                    "filtered by " + type + " " + value);
        } else if (ValidationConstants.FILTER_TYPE_PROJECT.equals(type)) {
            TaskFilter filter = new TaskFilter(context.getProjects(), null, value);
            List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
            context.getUi().showGlobalFilteredTasks(filteredTasks, type, value);

            context.getExportHandler().updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                    "filtered by " + type + " " + value);
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
