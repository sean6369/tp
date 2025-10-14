package seedu.flowcli.commands.core;

import seedu.flowcli.commands.AddCommand;
import seedu.flowcli.commands.ByeCommand;
import seedu.flowcli.commands.Command;
import seedu.flowcli.commands.DeleteCommand;
import seedu.flowcli.commands.ExportCommand;
import seedu.flowcli.commands.FilterCommand;
import seedu.flowcli.commands.HelpCommand;
import seedu.flowcli.commands.ListCommand;
import seedu.flowcli.commands.MarkCommand;
import seedu.flowcli.commands.UpdateCommand;
import seedu.flowcli.commands.SortCommand;
import seedu.flowcli.commands.UnmarkCommand;
import seedu.flowcli.commands.UnknownCommand;
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
        case ADD:
            return new AddCommand(arguments);
        case DELETE:
            return new DeleteCommand(arguments);
        case UPDATE:
            return new UpdateCommand(arguments);
        case HELP:
            return new HelpCommand(arguments);
        case SORT:
            return new SortCommand(arguments);
        case FILTER:
            return new FilterCommand(arguments);
        case EXPORT:
            return new ExportCommand(arguments);
        case UNKNOWN:
        default:
            return new UnknownCommand(arguments);
        }
    }
}
