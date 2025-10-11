package seedu.flowcli.command;

import seedu.flowcli.exception.FlowCLIExceptions;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.export.TaskExporter;
import seedu.flowcli.tools.TaskFilter;
import seedu.flowcli.tools.TaskSorter;
import seedu.flowcli.ui.ConsoleUi;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles export command logic and view state tracking for export functionality.
 */
public class ExportCommandHandler {
    private final ProjectList projects;
    private final ConsoleUi ui;

    // View state tracking for export functionality
    private final List<TaskWithProject> lastDisplayedTasks;
    private ViewType lastViewType;
    private String lastViewMetadata;

    public enum ViewType {
        NONE, SORTED, FILTERED, PROJECT
    }

    public ExportCommandHandler(ProjectList projects, ConsoleUi ui) {
        this.projects = projects;
        this.ui = ui;
        this.lastDisplayedTasks = new ArrayList<>();
        this.lastViewType = ViewType.NONE;
        this.lastViewMetadata = "";
    }

    /**
     * Updates the view state tracking for export functionality.
     */
    public void updateViewState(List<TaskWithProject> tasks, ViewType viewType, String metadata) {
        lastDisplayedTasks.clear();
        lastViewType = viewType;
        lastViewMetadata = metadata;
        lastDisplayedTasks.addAll(tasks);
    }

    /**
     * Handles the export command with various parameter combinations.
     */
    public void handleExport(String args) throws Exception {
        if (args == null || args.trim().isEmpty()) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                    "Invalid export command. Use: export tasks to <filename> [<project>] "
                            + "[filter by <type> <value>] [sort by <field> <order>]");
        }

        // Parse the command: "tasks to <filename> [<project>] [filter by <type> <value>] [sort by <field> <order>]"
        String[] parts = args.trim().split("\\s+");

        if (parts.length < 3 || !"tasks".equals(parts[0]) || !"to".equals(parts[1])) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                    "Invalid export command. Use: export tasks to <filename> [<project>] "
                            + "[filter by <type> <value>] [sort by <field> <order>]");
        }

        String filename = parts[2];
        if (!filename.endsWith(".txt")) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                    "Export filename must end with .txt extension. Use: " + filename + ".txt");
        }

        // Check if we have additional parameters
        String remainingArgs = args.substring(args.indexOf(filename) + filename.length()).trim();

        List<TaskWithProject> tasksToExport;
        String header;

        if (remainingArgs.isEmpty()) {
            // No additional parameters - check if we have a last view
            if (lastViewType != ViewType.NONE && !lastDisplayedTasks.isEmpty()) {
                // Export last displayed view
                tasksToExport = new ArrayList<>(lastDisplayedTasks);
                header = "Exported tasks (" + lastViewMetadata + ")";
            } else {
                // No last view - export all tasks from all projects
                tasksToExport = getAllTasks();
                header = "Exported all tasks";
            }
        } else {
            // Parse additional parameters
            // Check if parameter is "--all"
            if (remainingArgs.trim().equals("--all")) {
                // Force export all tasks, ignore last view
                tasksToExport = getAllTasks();
                header = "Exported all tasks";
            } else {
                // Normal parameter parsing (project, filter, sort)
                tasksToExport = parseExportParameters(remainingArgs);
                header = "Exported tasks (" + remainingArgs + ")";
            }
        }

        // Export to file
        TaskExporter.exportTasksToFile(tasksToExport, filename, header);
        ui.showExportSuccess(filename, tasksToExport.size());
    }

    /**
     * Parses export parameters and returns the appropriate tasks.
     */
    private List<TaskWithProject> parseExportParameters(String args) throws Exception {
        List<TaskWithProject> tasks = new ArrayList<>();

        // Check for project-specific export
        String[] parts = args.split("\\s+");
        String projectName = null;
        String filterType = null;
        String filterValue = null;
        String sortField = null;
        String sortOrder = null;

        // Early validation for incomplete filter/sort commands
        for (int j = 0; j < parts.length; j++) {
            if ("filter".equals(parts[j])) {
                // Check if we have "filter by <type> <value>"
                if (j + 3 >= parts.length) {
                    throw new FlowCLIExceptions.InvalidArgumentException(
                            "Incomplete filter command. Use: filter by <type> <value>");
                }
                if (!"by".equals(parts[j + 1])) {
                    throw new FlowCLIExceptions.InvalidArgumentException(
                            "Incomplete filter command. Use: filter by <type> <value>");
                }
            }
            if ("sort".equals(parts[j])) {
                // Check if we have "sort by <field> <order>"
                if (j + 3 >= parts.length) {
                    throw new FlowCLIExceptions.InvalidArgumentException(
                            "Incomplete sort command. Use: sort by <field> <order>");
                }
                if (!"by".equals(parts[j + 1])) {
                    throw new FlowCLIExceptions.InvalidArgumentException(
                            "Incomplete sort command. Use: sort by <field> <order>");
                }
            }
        }

        int i = 0;
        while (i < parts.length) {
            String current = parts[i];

            if ("filter".equals(current) && i + 3 < parts.length && "by".equals(parts[i + 1])) {
                filterType = parts[i + 2];
                filterValue = parts[i + 3];
                i += 4;
            } else if ("sort".equals(current) && i + 3 < parts.length && "by".equals(parts[i + 1])) {
                sortField = parts[i + 2];
                sortOrder = parts[i + 3];
                i += 4;
            } else if (!"filter".equals(current) && !"sort".equals(current) && !"by".equals(current)) {
                // Likely a project name (take first non-keyword token)
                if (projectName == null) {
                    projectName = current;
                }
                i++;
            } else {
                // Skip standalone keywords that don't form valid patterns
                i++;
            }
        }

        // Get base tasks
        if (projectName != null) {
            Project project = projects.getProject(projectName);
            if (project == null) {
                throw new FlowCLIExceptions.InvalidArgumentException("Project not found: " + projectName);
            }
            for (Task task : project.getProjectTasks().getTasks()) {
                tasks.add(new TaskWithProject(projectName, task));
            }
        } else {
            // All tasks from all projects
            for (Project project : projects.getProjectList()) {
                for (Task task : project.getProjectTasks().getTasks()) {
                    tasks.add(new TaskWithProject(project.getProjectName(), task));
                }
            }
        }

        // Apply filter if specified
        if (filterType != null && filterValue != null) {
            // Validate filter type
            if (!("priority".equals(filterType) || "project".equals(filterType))) {
                throw new FlowCLIExceptions.InvalidArgumentException(
                        "Invalid filter type: " + filterType + ". Use priority or project");
            }

            // Validate priority value if filtering by priority
            if ("priority".equals(filterType)) {
                String normalizedPriority = filterValue.toLowerCase();
                if (!normalizedPriority.equals("low") &&
                        !normalizedPriority.equals("medium") &&
                        !normalizedPriority.equals("high")) {
                    throw new FlowCLIExceptions.InvalidArgumentException(
                            "Invalid priority: " + filterValue + ". Use low, medium, or high.");
                }
            }

            // Use TaskFilter for filtering
            String priorityParam = "priority".equals(filterType) ? filterValue : null;
            String projectParam = "project".equals(filterType) ? filterValue : null;
            TaskFilter filter = new TaskFilter(projects, priorityParam, projectParam);
            tasks = filter.getFilteredTasks();
        }

        // Apply sort if specified
        if (sortField != null && sortOrder != null) {
            // Validate sort field
            if (!("deadline".equals(sortField) || "priority".equals(sortField))) {
                throw new FlowCLIExceptions.InvalidArgumentException(
                        "Invalid sort field: " + sortField + ". Use deadline or priority");
            }

            // Validate sort order
            if (!("ascending".equals(sortOrder) || "descending".equals(sortOrder))) {
                throw new FlowCLIExceptions.InvalidArgumentException(
                        "Invalid sort order: " + sortOrder + ". Use ascending or descending");
            }

            boolean ascending = "ascending".equals(sortOrder);

            // Use TaskSorter for sorting
            TaskSorter sorter = new TaskSorter(projects, sortField, ascending);
            tasks = sorter.getSortedTasks();
        }

        return tasks;
    }

    /**
     * Retrieves all tasks from all projects.
     */
    private List<TaskWithProject> getAllTasks() {
        List<TaskWithProject> tasks = new ArrayList<>();
        for (Project project : projects.getProjectList()) {
            for (Task task : project.getProjectTasks().getTasks()) {
                tasks.add(new TaskWithProject(project.getProjectName(), task));
            }
        }
        return tasks;
    }

    /**
     * Clears the current filter/sort view state.
     */
    public void clearViewState() {
        lastDisplayedTasks.clear();
        lastViewType = ViewType.NONE;
        lastViewMetadata = "";
    }
}