package seedu.flowcli.commands;

import java.time.LocalDate;
import java.util.Objects;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.task.Task;

/**
 * Updates attributes of an existing task within a project.
 */
public class UpdateCommand extends Command {

    public UpdateCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        Project targetProject = parsedArgument.getTargetProject();
        if (targetProject == null) {
            throw new MissingArgumentException();
        }

        String remainingArgument = parsedArgument.getRemainingArgument();
        if (remainingArgument == null || remainingArgument.trim().isEmpty()) {
            throw new MissingArgumentException();
        }

        String trimmed = remainingArgument.trim();
        String[] indexAndOptions = trimmed.split("\\s+", 2);
        String indexText = indexAndOptions[0];

        Integer taskIndex;
        try {
            taskIndex = CommandParser.parseIndexOrNull(indexText, targetProject.size());
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid task index: " + indexText);
        }

        String options = indexAndOptions.length > 1 ? indexAndOptions[1].trim() : "";
        if (options.isEmpty()) {
            throw new InvalidArgumentException(
                    "No fields provided to update. Use --description, --deadline, or --priority.");
        }

        boolean updateDescription = false;
        String newDescription = null;
        boolean updateDeadline = false;
        LocalDate newDeadline = null;
        boolean updatePriority = false;
        Integer newPriority = null;

        String[] tokens = options.split("\\s+");
        int i = 0;
        while (i < tokens.length) {
            String token = tokens[i];
            if (!token.startsWith("--")) {
                throw new InvalidArgumentException(
                        "Unknown option: " + token + ". Use --description, --deadline, or --priority.");
            }

            switch (token) {
            case "--description":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    throw new InvalidArgumentException("Description cannot be empty.");
                }
                updateDescription = true;
                StringBuilder descriptionBuilder = new StringBuilder(tokens[i]);
                i++;
                while (i < tokens.length && !tokens[i].startsWith("--")) {
                    descriptionBuilder.append(" ").append(tokens[i]);
                    i++;
                }
                newDescription = descriptionBuilder.toString().trim();
                if (newDescription.isEmpty()) {
                    throw new InvalidArgumentException("Description cannot be empty.");
                }
                continue;
            case "--deadline":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    throw new InvalidArgumentException(
                            "Deadline cannot be empty. Use YYYY-MM-DD or 'none' to clear it.");
                }
                updateDeadline = true;
                String deadlineValue = tokens[i];
                i++;
                String normalized = deadlineValue.toLowerCase();
                if ("none".equals(normalized) || "clear".equals(normalized)) {
                    newDeadline = null;
                } else {
                    try {
                        newDeadline = LocalDate.parse(deadlineValue);
                    } catch (Exception e) {
                        throw new InvalidArgumentException(
                                "Invalid deadline format: " + deadlineValue + ". Use YYYY-MM-DD or 'none'.");
                    }
                }
                continue;
            case "--priority":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    throw new InvalidArgumentException("Priority cannot be empty. Use low, medium, or high.");
                }
                updatePriority = true;
                String priorityValue = tokens[i];
                i++;
                String validatedPriority = CommandValidator.validatePriority(priorityValue);
                newPriority = CommandValidator.priorityToInt(validatedPriority);
                continue;
            default:
                throw new InvalidArgumentException(
                        "Unknown option: " + token + ". Use --description, --deadline, or --priority.");
            }
        }

        if (!updateDescription && !updateDeadline && !updatePriority) {
            throw new InvalidArgumentException(
                    "No fields provided to update. Use --description, --deadline, or --priority.");
        }

        Task taskToUpdate = targetProject.getProjectTasks().get(taskIndex);
        String originalDescription = taskToUpdate.getDescription();
        LocalDate originalDeadline = taskToUpdate.getDeadline();
        int originalPriority = taskToUpdate.getPriority();

        Task updatedTask = targetProject.updateTask(taskIndex, newDescription, updateDescription, newDeadline,
                updateDeadline, newPriority, updatePriority);

        assert !updateDescription || !Objects.equals(originalDescription,
                updatedTask.getDescription()) : "Description unchanged after update";
        assert !updateDeadline
                || !Objects.equals(originalDeadline, updatedTask.getDeadline()) : "Deadline unchanged after update";
        assert !updatePriority || originalPriority != updatedTask.getPriority() : "Priority unchanged after update";
        context.getUi().showUpdatedTask(targetProject, updatedTask);
        return true;
    }
}
