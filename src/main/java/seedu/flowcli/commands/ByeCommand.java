package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;

public class ByeCommand extends Command {

    public ByeCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) {
        context.getUi().bye();
        return false;
    }
}
