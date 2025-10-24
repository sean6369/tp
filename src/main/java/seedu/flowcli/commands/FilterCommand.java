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
        String value = trimmed.substring(spaceIndex + 1).trim();
        if (value.isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter-tasks --priority <low/medium/high>");
        }

        String type = option.substring(2).toLowerCase();
        if (!ValidationConstants.FILTER_TYPE_PRIORITY.equals(type)) {
            CommandValidator.validateFilterType(type);
            throw new InvalidArgumentException("Invalid filter type. Use: priority");
        }

        String normalizedPriority = CommandValidator.validatePriority(value);

        TaskFilter filter = new TaskFilter(context.getProjects(), normalizedPriority, null);
        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
        context.getUi().showGlobalFilteredTasks(filteredTasks, type, normalizedPriority);

        context.getExportHandler().updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                "filtered by " + type + " " + normalizedPriority);
        return true;
    }
}
