package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;

public class HelpCommand extends Command {

    public HelpCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) {
        context.getUi().showHelp();
        return true;
    }
}
