package seedu.FlowCLI.command;

public class Command {
    public final CommandParser.Type type;
    public final String arg;

    public Command(CommandParser.Type type, String arg) {
        this.type = type;
        this.arg = arg;
    }
}
