package seedu.flowcli.commands.core;

import seedu.flowcli.commands.AddCommand;
import seedu.flowcli.commands.ByeCommand;
import seedu.flowcli.commands.Command;
import seedu.flowcli.commands.CreateCommand;
import seedu.flowcli.commands.DeleteProjectCommand;
import seedu.flowcli.commands.DeleteTaskCommand;
import seedu.flowcli.commands.ExportCommand;
import seedu.flowcli.commands.FilterCommand;
import seedu.flowcli.commands.HelpCommand;
import seedu.flowcli.commands.ListCommand;
import seedu.flowcli.commands.MarkCommand;
import seedu.flowcli.commands.SortCommand;
import seedu.flowcli.commands.StatusCommand;
import seedu.flowcli.commands.UnknownCommand;
import seedu.flowcli.commands.UnmarkCommand;
import seedu.flowcli.commands.UpdateCommand;
import seedu.flowcli.parsers.CommandParser;

public class CommandFactory {

    public Command create(CommandParser.CommandType type, String arguments) {
        switch (type) {
        case LIST:
            return new ListCommand(arguments);
        case MARK:
            return new MarkCommand(arguments);
        case UNMARK:
            return new UnmarkCommand(arguments);
        case BYE:
            return new ByeCommand(arguments);
        case ADD_TASK:
            return new AddCommand(arguments);
        case CREATE_PROJECT:
            return new CreateCommand(arguments);
        case DELETE:
            String trimmed = arguments == null ? "" : arguments.trim();
            if (trimmed.contains("--confirm") || trimmed.split("\\s+").length == 1) {
                return new DeleteProjectCommand(trimmed);
            }
            return new DeleteTaskCommand(trimmed);
        case DELETE_PROJECT:
            return new DeleteProjectCommand(arguments);
        case DELETE_TASK:
            return new DeleteTaskCommand(arguments);
        case UPDATE_TASK:
            return new UpdateCommand(arguments);
        case HELP:
            return new HelpCommand(arguments);
        case SORT_TASKS:
            return new SortCommand(arguments);
        case FILTER_TASKS:
            return new FilterCommand(arguments);
        case EXPORT_TASKS:
            return new ExportCommand(arguments);
        case STATUS:
            return new StatusCommand(arguments);
        case UNKNOWN:
        default:
            return new UnknownCommand(arguments);
        }
    }
}
