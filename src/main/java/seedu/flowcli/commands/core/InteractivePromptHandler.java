package seedu.flowcli.commands.core;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

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
     * Prompts user to select a project or create a new one.
     *
     * @param allowCreateNew Whether to include "Create new project" option
     * @return The selected project name, or null if cancelled
     */
    private String promptForProject(boolean allowCreateNew) {
        System.out.println("Hmph, fine... What project should this task be for? Don't think I care which one you pick!");

        if (projects.getProjectListSize() == 0) {
            if (!allowCreateNew) {
                System.out.println("No projects available. Going back...");
                return null;
            }
            return promptForNewProjectName();
        }

        System.out.println("Available projects:");
        for (int i = 0; i < projects.getProjectListSize(); i++) {
            System.out.println((i + 1) + ". " + projects.getProjectList().get(i).getProjectName());
        }

        if (allowCreateNew) {
            System.out.println((projects.getProjectListSize() + 1) + ". Create new project");
        }

        while (true) {
            System.out.print("Enter choice (1-" + (allowCreateNew ? projects.getProjectListSize() + 1 : projects.getProjectListSize()) + "): ");
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