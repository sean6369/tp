package seedu.flowcli.parsers;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.MissingIndexException;

public class CommandParser {

    public enum CommandType {
        LIST, MARK, UNMARK, BYE, ADD, DELETEPROJECT, DELETETASK, UPDATE, HELP, SORT, FILTER, EXPORT, UNKNOWN, CREATE
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
        case "list-all":
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
            return new ParsedCommand(CommandType.ADD, arguments);
        case "create-project":
        case "create":
            return new ParsedCommand(CommandType.CREATE, arguments);
        case "delete-project":
            return new ParsedCommand(CommandType.DELETEPROJECT, arguments);
        case "delete-task":
            return new ParsedCommand(CommandType.DELETETASK , arguments);
        case "update":
            return new ParsedCommand(CommandType.UPDATE, arguments);
        case "help":
            return new ParsedCommand(CommandType.HELP, arguments);
        case "sort":
            return new ParsedCommand(CommandType.SORT, arguments);
        case "filter":
            return new ParsedCommand(CommandType.FILTER, arguments);
        case "export":
            return new ParsedCommand(CommandType.EXPORT, arguments);
        default:
            return new ParsedCommand(CommandType.UNKNOWN, arguments);
        }
    }

    public static Integer parseIndexOrNull(String indexText, int maxIndex)
            throws IndexOutOfRangeException, MissingIndexException {
        if (indexText == null) {
            throw new MissingIndexException();
        }

        int idx1 = Integer.parseInt(indexText);
        if (idx1 < 1 || idx1 > maxIndex) {
            throw new IndexOutOfRangeException(maxIndex);
        }
        return idx1 - 1;
    }
}
