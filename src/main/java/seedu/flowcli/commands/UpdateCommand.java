package seedu.flowcli.commands;

import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Logger;

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
//@@author zeeeing
public class UpdateCommand extends Command {
    private static final Logger logger = Logger.getLogger(UpdateCommand.class.getName());

    public UpdateCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws Exception {
        logger.fine(() -> "UpdateCommand.execute() called with args=\"" + arguments + "\"");

        ArgumentParser parsedArgument = new ArgumentParser(arguments, context.getProjects());
        parsedArgument.validateProjectIndex();
        Project targetProject = parsedArgument.getTargetProject();

        String remainingArgument = parsedArgument.getRemainingArgument();
        if (remainingArgument == null || remainingArgument.trim().isEmpty()) {
            logger.warning(() -> "Missing task index or update options in input: \"" + arguments + "\"");
            throw new MissingArgumentException();
        }

        String trimmed = remainingArgument.trim();
        String[] indexAndOptions = trimmed.split("\\s+", 2);
        String indexText = indexAndOptions[0];

        Integer taskIndex = CommandParser.parseIndexOrNull(indexText, targetProject.size());

        String options = indexAndOptions.length > 1 ? indexAndOptions[1].trim() : "";
        if (options.isEmpty()) {
            logger.warning(() -> "No update options provided for task index \"" + indexText + "\"");
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
                String invalidToken = token;
                logger.warning(() -> "Unknown option token: \"" + invalidToken + "\"");
                throw new InvalidArgumentException(
                        "Unknown option: " + token + ". Use --description, --deadline, or --priority.");
            }

            switch (token) {
            case "--description":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    logger.warning(() -> "Empty description provided for task update");
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
                    logger.warning(() -> "Empty description provided for task update after trimming");
                    throw new InvalidArgumentException("Description cannot be empty.");
                }
                continue;
            case "--deadline":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    logger.warning(() -> "Empty deadline value provided for task update");
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
                    newDeadline = CommandValidator.validateAndParseDate(deadlineValue);
                }
                continue;
            case "--priority":
                i++;
                if (i >= tokens.length || tokens[i].startsWith("--")) {
                    logger.warning(() -> "Empty priority value provided for task update");
                    throw new InvalidArgumentException("Priority cannot be empty. Use low, medium, or high.");
                }
                updatePriority = true;
                String priorityValue = tokens[i];
                i++;
                String validatedPriority = CommandValidator.validatePriority(priorityValue);
                newPriority = CommandValidator.priorityToInt(validatedPriority);
                continue;
            default:
                String unsupportedToken = token;
                logger.warning(() -> "Unsupported flag encountered: \"" + unsupportedToken + "\"");
                throw new InvalidArgumentException(
                        "Unknown option: " + token + ". Use --description, --deadline, or --priority.");
            }
        }

        if (!updateDescription && !updateDeadline && !updatePriority) {
            logger.warning(() -> "No update fields specified for task index \"" + indexText + "\"");
            throw new InvalidArgumentException(
                    "No fields provided to update. Use --description, --deadline, or --priority.");
        }

        Task taskToUpdate = targetProject.getProjectTasks().get(taskIndex);
        String originalDescription = taskToUpdate.getDescription();
        LocalDate originalDeadline = taskToUpdate.getDeadline();
        int originalPriority = taskToUpdate.getPriority();

        Task updatedTask = targetProject.updateTask(taskIndex, newDescription, updateDescription, newDeadline,
                updateDeadline, newPriority, updatePriority);

        final int updatedTaskNumber = taskIndex + 1;
        final String updatedProjectName = targetProject.getProjectName();
        final boolean finalUpdateDescription = updateDescription;
        final boolean finalUpdateDeadline = updateDeadline;
        final boolean finalUpdatePriority = updatePriority;

        logger.fine(() -> String.format("Task %d in \"%s\" updated: desc=%s deadline=%s priority=%s", updatedTaskNumber,
                updatedProjectName, finalUpdateDescription, finalUpdateDeadline, finalUpdatePriority));

        assert !updateDescription || !Objects.equals(originalDescription,
                updatedTask.getDescription()) : "Description unchanged after update";
        assert !updateDeadline
                || !Objects.equals(originalDeadline, updatedTask.getDeadline()) : "Deadline unchanged after update";
        assert !updatePriority || originalPriority != updatedTask.getPriority() : "Priority unchanged after update";

        logger.info(() -> String.format("Task %d in project \"%s\" updated successfully", updatedTaskNumber,
                updatedProjectName));
        context.getUi().showUpdatedTask(targetProject, updatedTask);
        return true;
    }
}
