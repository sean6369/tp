package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;

public class UnmarkCommand extends Command {

    public UnmarkCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        if (targetProject == null) {
            throw new MissingArgumentException();
        }

        Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

        targetProject.getProjectTasks().unmark(idx);
        context.getUi().showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), true);
        return true;
    }
}
