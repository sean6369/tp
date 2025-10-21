package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.parsers.ArgumentParser;

public class ListCommand extends Command {

    public ListCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        if (parsedArgument.getTargetProject() == null && "--all".equals(parsedArgument.getRemainingArgument())) {
            context.getUi().showAllTasksAcrossProjects();
            context.getExportHandler().clearViewState();
        } else if (parsedArgument.getTargetProject() == null) {
            context.getUi().showProjectList();
            context.getExportHandler().clearViewState();
        } else {
            context.getUi().showTaskList(parsedArgument.getTargetProject());
        }
        return true;
    }
}
