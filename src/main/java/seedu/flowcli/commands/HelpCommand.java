package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.ExtraArgumentException;

public class HelpCommand extends Command {

    public HelpCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws ExtraArgumentException {
        // Validate that no parameters are provided
        if (arguments != null && !arguments.trim().isEmpty()) {
            throw new ExtraArgumentException("The 'help' command does not accept any parameters.");
        }
        context.getUi().showHelp();
        return true;
    }
}
