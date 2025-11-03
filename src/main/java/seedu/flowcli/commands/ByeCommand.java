package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.ExtraArgumentException;

public class ByeCommand extends Command {

    public ByeCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws ExtraArgumentException {
        // Validate that no parameters are provided
        if (arguments != null && !arguments.trim().isEmpty()) {
            throw new ExtraArgumentException("The 'bye' command does not accept any parameters.");
        }
        context.getUi().printByeMessage();
        return false;
    }
}
