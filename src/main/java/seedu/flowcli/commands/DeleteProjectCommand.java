package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
import seedu.flowcli.exceptions.InvalidIndexFormatException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.MissingIndexException;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

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
        String[] parts = trimmedArgs.split("\\s+");
        String indexToken = parts[0];

        int zeroBasedIndex;
        try {
            zeroBasedIndex = CommandParser.parseIndexOrNull(indexToken, projects.getProjectListSize());
        } catch (MissingIndexException e) {
            throw new MissingArgumentException();
        } catch (NumberFormatException e) {
            throw new InvalidIndexFormatException(indexToken, "project");
        }

        boolean confirmed = false;
        for (int i = 1; i < parts.length; i++) {
            if ("--confirm".equalsIgnoreCase(parts[i])) {
                confirmed = true;
                break;
            }
        }

        if (!confirmed) {
            throw new InvalidCommandSyntaxException("Confirm project deletion with --confirm.");
        }

        Project deletedProject = projects.delete(zeroBasedIndex);
        context.getUi().showDeletedProject(deletedProject);
        return true;
    }
}
