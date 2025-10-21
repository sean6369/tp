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
        
        String description;
        int priority = 2;
        LocalDate deadline = null;

        if (targetProject != null) {
            // Normal case: project exists
            if (parsedArgument.getRemainingArgument() == null) {
                throw new MissingDescriptionException();
            }
            String args = parsedArgument.getRemainingArgument();
            String[] parts = args.split(" --");
            description = parts[0].trim();

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
        } else {
            // Project doesn't exist: parse from interactive mode format
            // Format: "projectName" "description" --options
            String fullArgs = arguments.trim();
            
            // Extract description (second quoted string)
            int firstQuoteEnd = fullArgs.indexOf('"', 1);
            if (firstQuoteEnd == -1) {
                throw new InvalidArgumentException("Invalid argument format");
            }
            int secondQuoteStart = fullArgs.indexOf('"', firstQuoteEnd + 1);
            if (secondQuoteStart == -1) {
                throw new InvalidArgumentException("Missing task description");
            }
            int secondQuoteEnd = fullArgs.indexOf('"', secondQuoteStart + 1);
            if (secondQuoteEnd == -1) {
                throw new InvalidArgumentException("Invalid argument format");
            }
            
            description = fullArgs.substring(secondQuoteStart + 1, secondQuoteEnd).trim();
            if (description.isEmpty()) {
                throw new MissingDescriptionException();
            }
            
            // Parse options after the description
            String optionsPart = fullArgs.substring(secondQuoteEnd + 1).trim();
            if (!optionsPart.isEmpty()) {
                String[] parts = optionsPart.split(" --");
                for (int i = 1; i < parts.length; i++) { // Start from 1 to skip empty first part
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
                            throw new InvalidArgumentException("Invalid deadline format: " + dateStr +
                                    ". Use YYYY-MM-DD.");
                        }
                    } else {
                        throw new InvalidArgumentException("Unknown option: " + option +
                                ". Use --priority or --deadline.");
                    }
                }
            }
            
            // Create the new project
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new InvalidArgumentException("No project specified");
            }
            context.getProjects().addProject(projectName);
            targetProject = context.getProjects().getProject(projectName);
            context.getUi().showAddedProject();
        }

        targetProject.addTask(description, deadline, priority);
        context.getUi().showAddedTask(targetProject);
        return true;
    }
}
