package seedu.flowcli.commands;

import java.time.LocalDate;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
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

        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        String description;
        int priority = 2;
        LocalDate deadline = null;

        String remaining = parsedArgument.getRemainingArgument();
        if (remaining == null || remaining.trim().isEmpty()) {
            throw new MissingDescriptionException();
        }

        String[] parts = remaining.split(" --");
        description = parts[0].trim();
        if (description.isEmpty()) {
            throw new MissingDescriptionException();
        }

        // Collect all validation errors first
        StringBuilder errors = new StringBuilder();
        
        for (int i = 1; i < parts.length; i++) {
            String option = parts[i].trim();
            if (option.startsWith("priority ")) {
                String priStr = option.substring(9).trim();
                String validatedPriority = CommandValidator.validatePriority(priStr);
                priority = CommandValidator.priorityToInt(validatedPriority);
            } else if (option.startsWith("deadline ")) {
                String dateStr = option.substring(9).trim();
                deadline = CommandValidator.validateAndParseDate(dateStr);
            } else if (option.equals("priority")) {
                if (errors.length() > 0) {
                    errors.append("\n");
                }
                errors.append("Priority value is empty. Use --priority <low|medium|high>.");
            } else if (option.equals("deadline")) {
                if (errors.length() > 0) {
                    errors.append("\n");
                }
                errors.append("Deadline value is empty. Use --deadline <yyyy-mm-dd>.");
            } else {
                if (errors.length() > 0) {
                    errors.append("\n");
                }
                errors.append("Unknown option: ").append(option).append(". Use --priority or --deadline.");
            }
        }
        
        // Throw exception if any errors were found
        if (errors.length() > 0) {
            throw new InvalidCommandSyntaxException(errors.toString());
        }

        targetProject.addTask(description, deadline, priority);
        context.getUi().showAddedTask(targetProject);
        return true;
    }
}
