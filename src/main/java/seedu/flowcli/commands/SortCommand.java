package seedu.flowcli.commands;

import java.util.List;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.commands.utility.TaskSorter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.task.TaskWithProject;

public class SortCommand extends Command {

    public SortCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        if (arguments.isEmpty() || !arguments.startsWith("tasks by")) {
            throw new InvalidArgumentException(
                                            "Invalid sort command. Use: sort tasks by deadline/priority ascending/descending");
        }

        String[] parts = arguments.split("\\s+");
        if (parts.length < 4) {
            throw new InvalidArgumentException(
                                            "Invalid sort command. Use: sort tasks by deadline/priority ascending/descending");
        }

        String field = parts[2];
        String order = parts[3];
        boolean ascending = ValidationConstants.SORT_ORDER_ASCENDING.equals(order);

        CommandValidator.validateSortField(field);
        CommandValidator.validateSortOrder(order);

        TaskSorter sorter = new TaskSorter(context.getProjects(), field, ascending);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();
        context.getUi().showGlobalSortedTasks(sortedTasks, field, order);

        context.getExportHandler().updateViewState(sortedTasks, ExportCommandHandler.ViewType.SORTED,
                                        "sorted by " + field + " " + order);
        return true;
    }
}
