package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

//@@author zeeeing
public class DeleteProjectCommand extends Command {

    public DeleteProjectCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        String trimmedArgs = arguments.trim();
        if (trimmedArgs.isEmpty()) {
            throw new MissingArgumentException();
        }

        ProjectList projects = context.getProjects();
        ArgumentParser parsedArgument = new ArgumentParser(arguments, projects);
        parsedArgument.validateProjectIndex();

        // Check for --confirm flag in remaining arguments
        String remaining = parsedArgument.getRemainingArgument();
        boolean confirmed = remaining != null && remaining.toLowerCase().contains("--confirm");

        if (!confirmed) {
            throw new InvalidCommandSyntaxException("Confirm project deletion with --confirm.");
        }

        // Get the zero-based index from ArgumentParser
        int zeroBasedIndex = parsedArgument.getTargetProjectIndex();
        Project deletedProject = projects.delete(zeroBasedIndex);
        context.getUi().showDeletedProject(deletedProject);
        return true;
    }
}

