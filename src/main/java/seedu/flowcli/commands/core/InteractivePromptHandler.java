package seedu.flowcli.commands.core;

import java.util.Scanner;
import java.util.logging.Logger;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

/**
 * Handles interactive prompting for commands that require user guidance.
 * Provides conversational interface with numbered options and validation.
 * Uses tsundere personality in prompts as specified in plan.md.
 */
public class InteractivePromptHandler {
    private static final Logger logger = Logger.getLogger(InteractivePromptHandler.class.getName());

    private final ConsoleUi ui;
    private final ProjectList projects;
    private final Scanner scanner;

    /**
     * Creates an InteractivePromptHandler with the given UI, projects, and input scanner.
     *
     * @param ui The console UI for displaying messages
     * @param projects The project list for accessing available projects
     * @param scanner The scanner for reading user input
     */
    public InteractivePromptHandler(ConsoleUi ui, ProjectList projects, Scanner scanner) {
        assert ui != null : "ConsoleUi cannot be null";
        assert projects != null : "ProjectList cannot be null";
        assert scanner != null : "Scanner cannot be null";

        this.ui = ui;
        this.projects = projects;
        this.scanner = scanner;

        logger.info("InteractivePromptHandler initialized");
    }

    /**
     * Handles interactive prompting for the add command.
     * Guides user through project selection, description, priority, and deadline.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleAddCommand() {
        logger.info("Starting interactive add command flow");

        // Step 1: Project selection
        String projectName = promptForProject(true); // true = allow creating new project
        if (projectName == null) {
            return null; // Cancelled
        }

        // Step 2: Task description
        System.out.println("Enter task description:");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty. Going back...");
            return null;
        }

        // Step 3: Priority selection
        String priority = promptForPriority();
        if (priority == null) {
            return null; // Cancelled
        }

        // Step 4: Deadline (optional)
        String deadline = promptForDeadline();
        if (deadline == null) {
            return null; // Cancelled
        }

        // Construct arguments
        StringBuilder args = new StringBuilder();
        args.append("\"").append(projectName).append("\" ");
        args.append("\"").append(description).append("\"");

        if (!priority.equals("medium")) { // medium is default, don't include
            args.append(" --priority ").append(priority);
        }

        if (!deadline.isEmpty()) {
            args.append(" --deadline ").append(deadline);
        }

        String result = args.toString();
        logger.info("Add command arguments constructed: " + result);
        return result;
    }

    /**
     * Handles interactive prompting for the create command.
     * Prompts for project name.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleCreateCommand() {
        logger.info("Starting interactive create command flow");

        String projectName = promptForNewProjectName();
        if (projectName == null) {
            return null; // Cancelled
        }

        String result = "\"" + projectName + "\"";
        logger.info("Create command arguments constructed: " + result);
        return result;
    }

    /**
     * Handles interactive prompting for the list command.
     * Prompts for project selection or all projects.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleListCommand() {
        logger.info("Starting interactive list command flow");

        System.out.println("What do you want to list, forgetful one?");
        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }
        System.out.println((projects.getProjectListSize() + 1) +
                           ". All projects [default, this will always be last option]");

        while (true) {
            System.out.print("Enter choice (1-" + (projects.getProjectListSize() + 1) + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return ""; // Default to all projects
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= projects.getProjectListSize()) {
                    return "\"" + projects.getProjectList().get(choice - 1).getProjectName() + "\"";
                } else if (choice == projects.getProjectListSize() + 1) {
                    return ""; // All projects
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles interactive prompting for the mark command.
     * Prompts for project and task selection.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleMarkCommand() {
        logger.info("Starting interactive mark command flow");

        String projectName = promptForProject(false); // Don't allow creating new project
        if (projectName == null) {
            return null; // Cancelled
        }

        // Find the project
        var project = projects.getProjectList().stream()
                .filter(p -> p.getProjectName().equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null || project.size() == 0) {
            System.out.println("No tasks in this project. Going back...");
            return null;
        }

        System.out.println("Hmph, which tasks do you want to mark as done in " + projectName + ":");
        for (int i = 0; i < project.size(); i++) {
            System.out.println((i + 1) + ". [ ] " + project.getProjectTasks().get(i).getDescription());
        }

        System.out.println("Enter task number to mark as done [for multiple tasks, separate by commas e.g. 1,2,3,4]:");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Going back...");
            return null;
        }

        // Parse task indices
        String[] indices = input.split(",");
        StringBuilder args = new StringBuilder();
        args.append("\"").append(projectName).append("\"");

        for (String indexStr : indices) {
            try {
                int index = Integer.parseInt(indexStr.trim());
                if (index < 1 || index > project.size()) {
                    System.out.println("Invalid task number: " + index);
                    return null;
                }
                args.append(" ").append(index);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + indexStr);
                return null;
            }
        }

        String result = args.toString();
        logger.info("Mark command arguments constructed: " + result);
        return result;
    }

    /**
     * Handles interactive prompting for the unmark command.
     * Similar to mark command but for unmarking tasks.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleUnmarkCommand() {
        logger.info("Starting interactive unmark command flow");

        String projectName = promptForProject(false);
        if (projectName == null) {
            return null;
        }

        var project = projects.getProjectList().stream()
                .filter(p -> p.getProjectName().equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null || project.size() == 0) {
            System.out.println("No tasks in this project. Going back...");
            return null;
        }

        System.out.println("Hmph, which tasks do you want to mark as not done in " + projectName + ":");
        for (int i = 0; i < project.size(); i++) {
            System.out.println((i + 1) + ". [x] " + project.getProjectTasks().get(i).getDescription());
        }

        System.out.println("Enter task number to mark as not done " +
                           "[for multiple tasks, separate by commas e.g. 1,2,3,4]:");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Going back...");
            return null;
        }

        String[] indices = input.split(",");
        StringBuilder args = new StringBuilder();
        args.append("\"").append(projectName).append("\"");

        for (String indexStr : indices) {
            try {
                int index = Integer.parseInt(indexStr.trim());
                if (index < 1 || index > project.size()) {
                    System.out.println("Invalid task number: " + index);
                    return null;
                }
                args.append(" ").append(index);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + indexStr);
                return null;
            }
        }

        String result = args.toString();
        logger.info("Unmark command arguments constructed: " + result);
        return result;
    }

    /**
     * Handles interactive prompting for the delete command.
     * Prompts for what to delete (project or task) and selections.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleDeleteCommand() {
        logger.info("Starting interactive delete command flow");

        System.out.println("What do you want to delete, forgetful one?");
        System.out.println("1. A project");
        System.out.println("2. A task");
        System.out.println("3. Sorry, my fault.");

        while (true) {
            System.out.print("Enter choice (1-3): ");
            String input = scanner.nextLine().trim();

            if (input.equals("3") || input.isEmpty()) {
                return null; // Cancelled
            }

            switch (input) {
            case "1":
                return handleDeleteProject();
            case "2":
                return handleDeleteTask();
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Handles project deletion with confirmation.
     */
    private String handleDeleteProject() {
        if (projects.getProjectListSize() == 0) {
            System.out.println("No projects to delete. Going back...");
            return null;
        }

        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }

        while (true) {
            System.out.print("Enter project number to delete: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null; // Cancelled
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= projects.getProjectListSize()) {
                    String projectName = projects.getProjectList().get(choice - 1).getProjectName();
                    System.out.print("Are you sure you want to delete project \"" + projectName + "\"? (y/n): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("y") || confirm.equals("yes")) {
                        return "\"" + projectName + "\"";
                    } else {
                        System.out.println("Delete cancelled.");
                        return null;
                    }
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles task deletion with confirmation.
     */
    private String handleDeleteTask() {
        String projectName = promptForProject(false);
        if (projectName == null) {
            return null;
        }

        var project = projects.getProjectList().stream()
                .filter(p -> p.getProjectName().equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null || project.size() == 0) {
            System.out.println("No tasks in this project. Going back...");
            return null;
        }

        System.out.println("Tasks in " + projectName + ":");
        for (int i = 0; i < project.size(); i++) {
            System.out.println((i + 1) + ". " + project.getProjectTasks().get(i).getDescription());
        }

        while (true) {
            System.out.print("Enter task number to delete: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= project.size()) {
                    String taskDesc = project.getProjectTasks().get(index - 1).getDescription();
                    System.out.print("Are you sure you want to delete this task? (y/n): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("y") || confirm.equals("yes")) {
                        return "\"" + projectName + "\" " + index;
                    } else {
                        System.out.println("Delete cancelled.");
                        return null;
                    }
                } else {
                    System.out.println("Invalid task number. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles interactive prompting for the update command.
     * Complex flow with recursive field updates.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleUpdateCommand() {
        logger.info("Starting interactive update command flow");

        System.out.println("Hmph, what project contains the task?");
        if (projects.getProjectListSize() == 0) {
            System.out.println("No projects available. Going back...");
            return null;
        }

        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }
        System.out.println((projects.getProjectListSize() + 1) + ". Sorry, my fault.");

        while (true) {
            System.out.print("Enter choice (1-" + (projects.getProjectListSize() + 1) + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty() || input.equals(String.valueOf(projects.getProjectListSize() + 1))) {
                return null; // Cancelled
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= projects.getProjectListSize()) {
                    String projectName = projects.getProjectList().get(choice - 1).getProjectName();
                    return handleUpdateTaskInProject(projectName);
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles task selection and field updates within a project.
     */
    private String handleUpdateTaskInProject(String projectName) {
        var project = projects.getProjectList().stream()
                .filter(p -> p.getProjectName().equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null || project.size() == 0) {
            System.out.println("No tasks in this project. Going back...");
            return null;
        }

        System.out.println("Tasks in " + projectName + ":");
        for (int i = 0; i < project.size(); i++) {
            var task = project.getProjectTasks().get(i);
            System.out.println((i + 1) + ". " + task.getDescription() +
                             " - Priority: " + task.getPriority() +
                             " - Deadline: " + (task.getDeadline() != null ? task.getDeadline() : "None"));
        }

        while (true) {
            System.out.print("Enter task number to update: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null; // Go back to project selection
            }

            try {
                int taskIndex = Integer.parseInt(input);
                if (taskIndex >= 1 && taskIndex <= project.size()) {
                    return handleUpdateTaskFields(projectName, taskIndex);
                } else {
                    System.out.println("Invalid task number. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles the recursive field update loop for a specific task.
     */
    private String handleUpdateTaskFields(String projectName, int taskIndex) {
        StringBuilder args = new StringBuilder();
        args.append("\"").append(projectName).append("\" ").append(taskIndex);

        boolean continueUpdating = true;
        while (continueUpdating) {
            System.out.println("What do you want to update this task? Make up your mind!");
            System.out.println("1. Description");
            System.out.println("2. Priority");
            System.out.println("3. Deadline");
            System.out.println("4. Reselect task");
            System.out.println("5. Reselect project");
            System.out.println("6. Done");

            System.out.print("Enter choice (1-6): ");
            String input = scanner.nextLine().trim();

            switch (input) {
            case "1":
                String newDesc = promptForNewDescription();
                if (newDesc == null) {
                    continue; // Stay in loop
                }
                args.append(" --description \"").append(newDesc).append("\"");
                System.out.println("Description updated!");
                break;
            case "2":
                String newPriority = promptForNewPriority();
                if (newPriority == null) {
                    continue;
                }
                args.append(" --priority ").append(newPriority);
                System.out.println("Priority updated!");
                break;
            case "3":
                String newDeadline = promptForNewDeadline();
                if (newDeadline == null) {
                    continue;
                }
                if (!newDeadline.isEmpty()) {
                    args.append(" --deadline ").append(newDeadline);
                } else {
                    args.append(" --deadline none");
                }
                System.out.println("Deadline updated!");
                break;
            case "4":
                return handleUpdateTaskInProject(projectName); // Reselect task
            case "5":
                return handleUpdateCommand(); // Reselect project
            case "6":
                continueUpdating = false;
                break;
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }

        String result = args.toString();
        logger.info("Update command arguments constructed: " + result);
        return result;
    }

    /**
     * Prompts for new task description.
     */
    private String promptForNewDescription() {
        System.out.println("Enter new description:");
        String desc = scanner.nextLine().trim();
        if (desc.isEmpty()) {
            System.out.println("Description cannot be empty. Staying in update menu...");
            return null;
        }
        return desc;
    }

    /**
     * Prompts for new priority.
     */
    private String promptForNewPriority() {
        System.out.println("What priority level? I think everything is high priority!");
        System.out.println("1. High");
        System.out.println("2. Medium");
        System.out.println("3. Low");
        System.out.println("4. Keep current");

        while (true) {
            System.out.print("Enter choice (1-4): ");
            String input = scanner.nextLine().trim();

            switch (input) {
            case "1":
                return "high";
            case "2":
                return "medium";
            case "3":
                return "low";
            case "4":
                return null; // Keep current (don't add to args)
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Prompts for new deadline.
     */
    private String promptForNewDeadline() {
        System.out.println("Enter new deadline (YYYY-MM-DD, 'none', or press Enter to skip):");
        String deadline = scanner.nextLine().trim();

        if (deadline.isEmpty()) {
            return null; // Skip
        }

        if (deadline.equalsIgnoreCase("none")) {
            return ""; // Set to none
        }

        // Basic validation
        if (deadline.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return deadline;
        } else {
            System.out.println("Invalid date format. Staying in update menu...");
            return null;
        }
    }

    /**
     * Handles interactive prompting for the sort command.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleSortCommand() {
        logger.info("Starting interactive sort command flow");

        System.out.println("Hmph, sort tasks by what? Don't make me guess!");
        System.out.println("1. Deadline");
        System.out.println("2. Priority");

        while (true) {
            System.out.print("Enter choice (1-2): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            String field;
            switch (input) {
            case "1":
                field = "deadline";
                break;
            case "2":
                field = "priority";
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            // Now get order
            System.out.println("Sort order? Ascending or descending!");
            System.out.println("1. Ascending");
            System.out.println("2. Descending");

            while (true) {
                System.out.print("Enter choice (1-2): ");
                String orderInput = scanner.nextLine().trim();

                String order;
                switch (orderInput) {
                case "1":
                    order = "ascending";
                    break;
                case "2":
                    order = "descending";
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }

                String result = "tasks by " + field + " " + order;
                logger.info("Sort command arguments constructed: " + result);
                return result;
            }
        }
    }

    /**
     * Handles interactive prompting for the filter command.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleFilterCommand() {
        logger.info("Starting interactive filter command flow");

        System.out.println("Filter by what? Priority or project, make up your mind!");
        System.out.println("1. Priority");
        System.out.println("2. Project");

        while (true) {
            System.out.print("Enter choice (1-2): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            switch (input) {
            case "1":
                return handleFilterByPriority();
            case "2":
                return handleFilterByProject();
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Handles filtering by priority.
     */
    private String handleFilterByPriority() {
        System.out.println("What priority level? High, medium, or low - don't pick wrong!");
        System.out.println("1. High");
        System.out.println("2. Medium");
        System.out.println("3. Low");

        while (true) {
            System.out.print("Enter choice (1-3): ");
            String input = scanner.nextLine().trim();

            switch (input) {
            case "1":
                return "tasks by priority high";
            case "2":
                return "tasks by priority medium";
            case "3":
                return "tasks by priority low";
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Handles filtering by project.
     */
    private String handleFilterByProject() {
        if (projects.getProjectListSize() == 0) {
            System.out.println("No projects available. Going back...");
            return null;
        }

        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }

        while (true) {
            System.out.print("Enter project number: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= projects.getProjectListSize()) {
                    String projectName = projects.getProjectList().get(choice - 1).getProjectName();
                    return "tasks by project \"" + projectName + "\"";
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles interactive prompting for the export command.
     * Complex flow with multiple options.
     *
     * @return The constructed command arguments string, or null if cancelled
     */
    public String handleExportCommand() {
        logger.info("Starting interactive export command flow");

        // Step 1: Filename
        System.out.println("Hmph, enter filename (without .txt extension):");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = "my_tasks"; // Default
        }

        // Step 2: What to export
        System.out.println("What do you want to export? Don't take forever deciding!");
        System.out.println("1. All tasks");
        System.out.println("2. Specific project");
        System.out.println("3. Filtered tasks");
        System.out.println("4. Sorted tasks");
        System.out.println("5. Filtered and sorted tasks");
        System.out.println("6. Sorry, my fault.");

        while (true) {
            System.out.print("Enter choice (1-6): ");
            String input = scanner.nextLine().trim();

            if (input.equals("6") || input.isEmpty()) {
                return null; // Cancelled
            }

            StringBuilder args = new StringBuilder();
            args.append("tasks to ").append(filename).append(".txt");

            switch (input) {
            case "1":
                args.append(" --all");
                break;
            case "2":
                String projectName = promptForProject(false);
                if (projectName == null) {
                    return null;
                }
                args.append(" \"").append(projectName).append("\"");
                break;
            case "3":
                String filterArgs = handleFilterCommand();
                if (filterArgs == null) {
                    return null;
                }
                args.append(" ").append(filterArgs);
                break;
            case "4":
                String sortArgs = handleSortCommand();
                if (sortArgs == null) {
                    return null;
                }
                args.append(" ").append(sortArgs);
                break;
            case "5":
                String filterArgs2 = handleFilterCommand();
                if (filterArgs2 == null) {
                    return null;
                }
                String sortArgs2 = handleSortCommand();
                if (sortArgs2 == null) {
                    return null;
                }
                args.append(" ").append(filterArgs2).append(" ").append(sortArgs2);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            // Confirmation
            System.out.print("Ready to export tasks to " + filename + ".txt. Confirm? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("yes")) {
                String result = args.toString();
                logger.info("Export command arguments constructed: " + result);
                return result;
            } else {
                System.out.println("Export cancelled.");
                return null;
            }
        }
    }

    /**
     * Prompts user to select a project or create a new one.
     *
     * @param allowCreateNew Whether to include "Create new project" option
     * @return The selected project name, or null if cancelled
     */
    private String promptForProject(boolean allowCreateNew) {
        System.out.println("Hmph, fine... What project should this task be for? " +
                           "Don't think I care which one you pick!");

        if (projects.getProjectListSize() == 0) {
            if (!allowCreateNew) {
                System.out.println("No projects available. Going back...");
                return null;
            }
            // When no projects exist, directly offer to create new project
            System.out.println("Available projects:");
            System.out.println("1. Create new project");

            while (true) {
                System.out.print("Enter choice (1): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Going back...");
                    return null;
                }

                if (input.equals("1")) {
                    return promptForNewProjectName();
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }

        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }

        if (allowCreateNew) {
            System.out.println((projects.getProjectListSize() + 1) + ". Create new project");
        }

        while (true) {
            System.out.print("Enter choice (1-" +
                             (allowCreateNew ? projects.getProjectListSize() + 1 : projects.getProjectListSize()) +
                             "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Going back...");
                return null;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= projects.getProjectListSize()) {
                    return projects.getProjectList().get(choice - 1).getProjectName();
                } else if (allowCreateNew && choice == projects.getProjectListSize() + 1) {
                    return promptForNewProjectName();
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Prompts for a new project name.
     *
     * @return The project name, or null if cancelled
     */
    private String promptForNewProjectName() {
        while (true) {
            System.out.println("Hmph, enter project name already:");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Going back...");
                return null;
            }

            // Check if project already exists
            boolean exists = projects.getProjectList().stream()
                    .anyMatch(p -> p.getProjectName().equalsIgnoreCase(name));

            if (exists) {
                System.out.println("Project already exists. Choose from existing projects or try a different name.");
                return null; // Go back to project selection
            }

            return name;
        }
    }

    /**
     * Prompts for priority level.
     *
     * @return The priority string, or null if cancelled
     */
    private String promptForPriority() {
        System.out.println("What priority level? Default is Medium [skip by pressing Enter]");
        System.out.println("1. High");
        System.out.println("2. Medium");
        System.out.println("3. Low");

        while (true) {
            System.out.print("Enter choice (1-3): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return "medium"; // Default
            }

            switch (input) {
            case "1":
                return "high";
            case "2":
                return "medium";
            case "3":
                return "low";
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Prompts for deadline (optional).
     *
     * @return The deadline string (empty if none), or null if cancelled
     */
    private String promptForDeadline() {
        System.out.println("Do you want to set a deadline? [default is no, it's not like I mind either way]");
        System.out.println("1. Yes");
        System.out.println("2. No");

        while (true) {
            System.out.print("Enter choice (1-2): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return ""; // Default to no deadline
            }

            switch (input) {
            case "1":
                return promptForDeadlineDate();
            case "2":
                return "";
            default:
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Prompts for deadline date with validation.
     *
     * @return The validated deadline string
     */
    private String promptForDeadlineDate() {
        while (true) {
            System.out.println("Enter deadline (YYYY-MM-DD):");
            String date = scanner.nextLine().trim();

            if (date.isEmpty()) {
                System.out.println("Going back...");
                return null;
            }

            // Basic date format validation
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return date;
            } else {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
}
