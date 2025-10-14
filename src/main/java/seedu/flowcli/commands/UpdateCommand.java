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

        String[] parts = options.split(" --");
        for (String part : parts) {
            String option = part.trim();
            if (option.isEmpty()) {
                continue;
            }

            if (option.startsWith("description ")) {
                updateDescription = true;
                newDescription = option.substring("description ".length()).trim();
                if (newDescription.isEmpty()) {
                    throw new InvalidArgumentException("Description cannot be empty.");
                }
            } else if ("description".equals(option)) {
                throw new InvalidArgumentException("Description cannot be empty.");
            } else if (option.startsWith("deadline ")) {
                updateDeadline = true;
                String deadlineValue = option.substring("deadline ".length()).trim();
                if (deadlineValue.isEmpty()) {
                    throw new InvalidArgumentException(
                            "Deadline cannot be empty. Use YYYY-MM-DD or 'none' to clear it.");
                }

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
            } else if ("deadline".equals(option)) {
                throw new InvalidArgumentException(
                        "Deadline cannot be empty. Use YYYY-MM-DD or 'none' to clear it.");
            } else if (option.startsWith("priority ")) {
                updatePriority = true;
                String priorityValue = option.substring("priority ".length()).trim();
                String validatedPriority = CommandValidator.validatePriority(priorityValue);
                newPriority = CommandValidator.priorityToInt(validatedPriority);
            } else if ("priority".equals(option)) {
                throw new InvalidArgumentException("Priority cannot be empty. Use low, medium, or high.");
            } else {
                throw new InvalidArgumentException(
                        "Unknown option: " + option + ". Use --description, --deadline, or --priority.");
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

        assert !updateDescription || !Objects.equals(originalDescription, updatedTask.getDescription())
                : "Description unchanged after update";
        assert !updateDeadline || !Objects.equals(originalDeadline, updatedTask.getDeadline())
                : "Deadline unchanged after update";
        assert !updatePriority || originalPriority != updatedTask.getPriority()
                : "Priority unchanged after update";
        context.getUi().showUpdatedTask(targetProject, updatedTask);
        return true;
    }
}
