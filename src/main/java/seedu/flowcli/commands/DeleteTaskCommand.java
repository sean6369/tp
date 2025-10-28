package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.task.Task;

public class DeleteTaskCommand extends Command {

    public DeleteTaskCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        Integer index = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());
        Task deletedTask = targetProject.deleteTask(index);
        context.getUi().showDeletedTask(targetProject, deletedTask);
        return true;
    }
}
