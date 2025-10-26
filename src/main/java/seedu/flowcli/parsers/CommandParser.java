package seedu.flowcli.parsers;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingIndexException;

public class CommandParser {

    public static final String INVALID_TASK_INDEX_MESSAGE = "Invalid task index: %s. Use the numeric task "
            + "index shown in 'list <projectIndex>'.";

    public enum CommandType {
        LIST, MARK, UNMARK, BYE, ADD_TASK, CREATE_PROJECT, DELETE, DELETE_PROJECT, DELETE_TASK, UPDATE_TASK, HELP,
        SORT_TASKS, FILTER_TASKS, EXPORT_TASKS, STATUS, UNKNOWN
    }

    public static class ParsedCommand {
        private final CommandType type;
        private final String arguments;

        public ParsedCommand(CommandType type, String arguments) {
            this.type = type;
            this.arguments = arguments;
        }

        public CommandType getType() {
            return type;
        }

        public String getArguments() {
            return arguments;
        }
    }

    public ParsedCommand parse(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return new ParsedCommand(CommandType.UNKNOWN, "");
        }

        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
        case "list":
            return new ParsedCommand(CommandType.LIST, arguments);
        case "mark":
            return new ParsedCommand(CommandType.MARK, arguments);
        case "unmark":
            return new ParsedCommand(CommandType.UNMARK, arguments);
        case "bye":
            return new ParsedCommand(CommandType.BYE, arguments);
        case "add-task":
        case "add":
            return new ParsedCommand(CommandType.ADD_TASK, arguments);
        case "create-project":
        case "create":
            return new ParsedCommand(CommandType.CREATE_PROJECT, arguments);
        case "delete":
            return new ParsedCommand(CommandType.DELETE, arguments);
        case "delete-task":
            return new ParsedCommand(CommandType.DELETE_TASK, arguments);
        case "delete-project":
            return new ParsedCommand(CommandType.DELETE_PROJECT, arguments);
        case "update":
        case "update-task":
            return new ParsedCommand(CommandType.UPDATE_TASK, arguments);
        case "help":
            return new ParsedCommand(CommandType.HELP, arguments);
        case "sort":
        case "sort-tasks":
            return new ParsedCommand(CommandType.SORT_TASKS, arguments);
        case "filter":
        case "filter-tasks":
            return new ParsedCommand(CommandType.FILTER_TASKS, arguments);
        case "export":
        case "export-tasks":
            return new ParsedCommand(CommandType.EXPORT_TASKS, arguments);
        case "status":
            return new ParsedCommand(CommandType.STATUS, arguments);
        default:
            return new ParsedCommand(CommandType.UNKNOWN, arguments);
        }
    }

    public static Integer parseIndexOrNull(String indexText, int maxIndex)
            throws IndexOutOfRangeException, MissingIndexException, InvalidArgumentException {
        if (indexText == null) {
            throw new MissingIndexException();
        }

        int idx1;
        try {
            idx1 = Integer.parseInt(indexText);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException(String.format(INVALID_TASK_INDEX_MESSAGE, indexText));
        }
        
        if (idx1 < 1 || idx1 > maxIndex) {
            throw new IndexOutOfRangeException(maxIndex);
        }
        return idx1 - 1;
    }
}
