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
        if (arguments.isEmpty() || !arguments.startsWith("tasks by")) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter tasks by priority <value> or project <name>");
        }

        String[] parts = arguments.split("\\s+");
        if (parts.length < 4) {
            throw new InvalidArgumentException(
                    "Invalid filter command. Use: filter tasks by priority <value> or project <name>");
        }

        String type = parts[2];
        String value = parts[3];

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
}
