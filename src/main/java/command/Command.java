package command;

import project.ProjectList;
import project.Project;

public class Command {
    public final CommandParser.Type type;
    public final String arg;

    public Command(CommandParser.Type type, String arg) {
        this.type = type;
        this.arg = arg;
    }
}
