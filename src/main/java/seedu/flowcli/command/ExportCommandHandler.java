package seedu.flowcli.command;

import seedu.flowcli.exception.FlowCLIExceptions;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
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
    private final List<TaskExporter.ExportableTask> lastDisplayedTasks;
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
    public void updateViewState(List<?> tasks, ViewType viewType, String metadata) {
        lastDisplayedTasks.clear();
        lastViewType = viewType;
        lastViewMetadata = metadata;

        // Convert tasks to ExportableTask format
        for (Object task : tasks) {
            if (task instanceof TaskSorter.SortedTask sortedTask) {
                lastDisplayedTasks.add(new TaskExporter.ExportableTask(sortedTask.getProjectName(),
                        sortedTask.getTask()));
            } else if (task instanceof TaskFilter.FilteredTask filteredTask) {
                lastDisplayedTasks.add(new TaskExporter.ExportableTask(filteredTask.getProjectName(),
                        filteredTask.getTask()));
            }
        }
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

        List<TaskExporter.ExportableTask> tasksToExport;
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
    private List<TaskExporter.ExportableTask> parseExportParameters(String args) throws Exception {
        List<TaskExporter.ExportableTask> tasks = new ArrayList<>();

        // Check for project-specific export
        String[] parts = args.split("\\s+");
        String projectName = null;
        String filterType = null;
        String filterValue = null;
        String sortField = null;
        String sortOrder = null;

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
                tasks.add(new TaskExporter.ExportableTask(projectName, task));
            }
        } else {
            // All tasks from all projects
            for (Project project : projects.getProjectList()) {
                for (Task task : project.getProjectTasks().getTasks()) {
                    tasks.add(new TaskExporter.ExportableTask(project.getProjectName(), task));
                }
            }
        }

        // Apply filter if specified
        if (filterType != null && filterValue != null) {
            tasks = applyFilter(tasks, filterType, filterValue);
        }

        // Apply sort if specified
        if (sortField != null && sortOrder != null) {
            applySort(tasks, sortField, sortOrder);
        }

        return tasks;
    }

    /**
     * Applies filter to the list of tasks.
     */
    private List<TaskExporter.ExportableTask> applyFilter(List<TaskExporter.ExportableTask> tasks,
                                                          String filterType, String filterValue) {
        List<TaskExporter.ExportableTask> filteredTasks = new ArrayList<>();

        for (TaskExporter.ExportableTask exportableTask : tasks) {
            Task task = exportableTask.getTask();

            if ("priority".equals(filterType)) {
                if (task.getPriorityString().equalsIgnoreCase(filterValue)) {
                    filteredTasks.add(exportableTask);
                }
            } else if ("project".equals(filterType)) {
                if (exportableTask.getProjectName().equalsIgnoreCase(filterValue)) {
                    filteredTasks.add(exportableTask);
                }
            }
        }

        return filteredTasks;
    }

    /**
     * Applies sort to the list of tasks.
     */
    private void applySort(List<TaskExporter.ExportableTask> tasks,
                           String sortField, String sortOrder) {
        boolean ascending = "ascending".equals(sortOrder);

        tasks.sort((t1, t2) -> {
            Task task1 = t1.getTask();
            Task task2 = t2.getTask();
            int comparison = 0;

            if ("deadline".equals(sortField)) {
                if (task1.getDeadline() != null && task2.getDeadline() != null) {
                    // Both have deadlines - compare them
                    comparison = task1.getDeadline().compareTo(task2.getDeadline());
                } else if (task1.getDeadline() == null && task2.getDeadline() != null) {
                    comparison = 1; // null sorts after non-null
                } else if (task1.getDeadline() != null && task2.getDeadline() == null) {
                    comparison = -1; // non-null sorts before null
                }
                // else both null, comparison = 0 (already initialized)
            }

            return ascending ? comparison : -comparison;
        });
    }

    /**
     * Retrieves all tasks from all projects.
     */
    private List<TaskExporter.ExportableTask> getAllTasks() {
        List<TaskExporter.ExportableTask> tasks = new ArrayList<>();
        for (Project project : projects.getProjectList()) {
            for (Task task : project.getProjectTasks().getTasks()) {
                tasks.add(new TaskExporter.ExportableTask(project.getProjectName(), task));
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