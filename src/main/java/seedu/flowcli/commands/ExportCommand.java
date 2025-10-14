package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;

public class ExportCommand extends Command {

    public ExportCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        context.getExportHandler().handleExport(arguments);
        return true;
    }
}
