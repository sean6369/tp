package seedu.flowcli.command;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.exception.FlowCLIExceptions.IndexOutOfRangeException;
import seedu.flowcli.exception.FlowCLIExceptions.MissingIndexException;

public class CommandParser {

    private Command command;

    public enum Type {
        LIST, MARK, UNMARK, BYE, ADD, DELETE, HELP, SORT, FILTER, EXPORT, UNKNOWN
    }

    public CommandParser(String line, ProjectList projects) {

        String[] parts = line.trim().split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
        case "list": {
            command = new Command(Type.LIST, arguments);
            break;
        }
        case "mark": {
            command = new Command(Type.MARK, arguments);
            break;
        }
        case "unmark": {
            command = new Command(Type.UNMARK, arguments);
            break;
        }
        case "bye": {
            command = new Command(Type.BYE, null);
            break;
        }
        case "add": {
            command = new Command(Type.ADD, arguments);
            break;
        }
        case "delete": {
            command = new Command(Type.DELETE, arguments);
            break;
        }
        case "help": {
            command = new Command(Type.HELP, arguments);
            break;
        }
        case "sort": {
            command = new Command(Type.SORT, arguments);
            break;
        }
        case "filter": {
            command = new Command(Type.FILTER, arguments);
            break;
        }
        case "export": {
            command = new Command(Type.EXPORT, arguments);
            break;
        }
        default: {
            command = new Command(Type.UNKNOWN, arguments);
            break;
        } // treat as add
        }
    }

    public Command getCommand() {
        return command;
    }

    public static Integer parseIndexOrNull(String indexText, int maxIndex) throws IndexOutOfRangeException,
            MissingIndexException {
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
