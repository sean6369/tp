package seedu.flowcli.commands;

import java.time.LocalDate;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingDescriptionException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;

public class AddCommand extends Command {

    public AddCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        String projectName = parsedArgument.getParsedProjectName();
        
        // Validate arguments first
        if (parsedArgument.getRemainingArgument() == null) {
            throw new MissingDescriptionException();
        }

        String args = parsedArgument.getRemainingArgument();
        String[] parts = args.split(" --");
        String description = parts[0].trim();
        int priority = 2;
        LocalDate deadline = null;

        for (int i = 1; i < parts.length; i++) {
            String option = parts[i].trim();
            if (option.startsWith("priority ")) {
                String priStr = option.substring(9).trim();
                String validatedPriority = CommandValidator.validatePriority(priStr);
                priority = CommandValidator.priorityToInt(validatedPriority);
            } else if (option.startsWith("deadline ")) {
                String dateStr = option.substring(9).trim();
                try {
                    deadline = LocalDate.parse(dateStr);
                } catch (Exception e) {
                    throw new InvalidArgumentException("Invalid deadline format: " + dateStr + ". Use YYYY-MM-DD.");
                }
            } else {
                throw new InvalidArgumentException("Unknown option: " + option + ". Use --priority or --deadline.");
            }
        }

        // If project doesn't exist, create it (for interactive mode)
        if (targetProject == null) {
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new InvalidArgumentException("No project specified");
            }
            // Create the new project
            context.getProjects().addProject(projectName);
            targetProject = context.getProjects().getProject(projectName);
            context.getUi().showAddedProject();
        }

        targetProject.addTask(description, deadline, priority);
        context.getUi().showAddedTask(targetProject);
        return true;
    }
}
