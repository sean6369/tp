package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.UnknownInputException;

public class UnknownCommand extends Command {

    public UnknownCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws UnknownInputException {
        throw new UnknownInputException();
    }
}
