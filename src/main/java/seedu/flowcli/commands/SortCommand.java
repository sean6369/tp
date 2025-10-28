package seedu.flowcli.commands;

import java.util.List;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.commands.utility.TaskSorter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.EmptyTaskListException;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
import seedu.flowcli.task.TaskWithProject;

public class SortCommand extends Command {

    public SortCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        String trimmed = arguments.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidCommandSyntaxException(
                    "Invalid sort command. Use: sort-tasks <--deadline/priority> <ascending/descending>");
        }

        if (context.getProjects().isEmpty()) {
            throw new EmptyProjectListException();
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length != 2) {
            throw new InvalidCommandSyntaxException(
                    "Invalid sort command. Use: sort-tasks <--deadline/priority> <ascending/descending>");
        }

        String fieldToken = parts[0];
        if (!fieldToken.startsWith("--")) {
            throw new InvalidCommandSyntaxException(
                    "Invalid sort command. Use: sort-tasks <--deadline/priority> <ascending/descending>");
        }

        String field = fieldToken.substring(2).toLowerCase();
        String order = parts[1].toLowerCase();

        boolean ascending = ValidationConstants.SORT_ORDER_ASCENDING.equals(order);

        CommandValidator.validateSortField(field);
        CommandValidator.validateSortOrder(order);

        TaskSorter sorter = new TaskSorter(context.getProjects(), field, ascending);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();
        
        if (sortedTasks.isEmpty()) {
            throw new EmptyTaskListException();
        }
        
        context.getUi().showGlobalSortedTasks(sortedTasks, field, order);

        context.getExportHandler().updateViewState(sortedTasks, ExportCommandHandler.ViewType.SORTED,
                "sorted by " + field + " " + order);
        return true;
    }
}
