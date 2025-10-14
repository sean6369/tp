package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;

import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectAlreadyExistsException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;

public class CreateCommand extends Command {

    public CreateCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        if(targetProject !=null){
            throw new ProjectAlreadyExistsException();
        }
        if (parsedArgument.getRemainingArgument() == null) {
            throw new MissingArgumentException();
        }
        context.getProjects().addProject(parsedArgument.getRemainingArgument());
        context.getUi().showAddedProject();
        return true;
    }
}
