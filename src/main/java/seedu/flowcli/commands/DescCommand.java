package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;


public class DescCommand extends Command {

    public DescCommand(String arguments){
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception{
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        if (targetProject == null) {
            throw new MissingArgumentException();
        }
        String description = parsedArgument.getRemainingArgument();
        targetProject.addProjectDescription(description);
        context.getUi().showAddedProjectDescription(targetProject);
        return true;
    }
}
