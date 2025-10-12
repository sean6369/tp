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
import seedu.flowcli.validation.ValidationConstants;
import seedu.flowcli.validation.CommandValidator;
import seedu.flowcli.tools.TaskCollector;

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
                    "Invalid export command. Use: export tasks to <filename>.txt [<project>] "
                            + "[filter by <type> <value>] [sort by <field> <order>]");
        }

        // Parse the command: "tasks to <filename> [<project>] [filter by <type> <value>] [sort by <field> <order>]"
        String[] parts = args.trim().split("\\s+");

        if (parts.length < 3 || !"tasks".equals(parts[0]) || !"to".equals(parts[1])) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                    "Invalid export command. Use: export tasks to <filename>.txt [<project>] "
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
                tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
                header = "Exported all tasks";
            }
        } else {
            // Parse additional parameters
            // Check if parameter is "--all"
            if (remainingArgs.trim().equals("--all")) {
                // Force export all tasks, ignore last view
                tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
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
        String[] parts = args.split("\\s+");
        
        // Validate incomplete filter/sort commands
        validateFilterSortCommands(parts);
        
        // Parse command tokens
        ParsedParams params = parseCommandTokens(parts);
        
        // Get base tasks
        List<TaskWithProject> tasks = collectBaseTasks(params.projectName);
        
        // Apply filter if specified
        if (params.filterType != null && params.filterValue != null) {
            tasks = applyFiltering(tasks, params.filterType, params.filterValue);
        }
        
        // Apply sort if specified
        if (params.sortField != null && params.sortOrder != null) {
            tasks = applySorting(tasks, params.sortField, params.sortOrder);
        }
        
        return tasks;
    }
    
    /**
     * Validates that filter and sort commands are complete.
     */
    private void validateFilterSortCommands(String[] parts) throws FlowCLIExceptions.InvalidArgumentException {
        for (int j = 0; j < parts.length; j++) {
            if (ValidationConstants.KEYWORD_FILTER.equals(parts[j])) {
                CommandValidator.validateFilterCommand(parts, j);
            }
            if (ValidationConstants.KEYWORD_SORT.equals(parts[j])) {
                CommandValidator.validateSortCommand(parts, j);
            }
        }
    }
    
    /**
     * Parses command tokens to extract parameters.
     */
    private ParsedParams parseCommandTokens(String[] parts) {
        ParsedParams params = new ParsedParams();
        
        int i = 0;
        while (i < parts.length) {
            String current = parts[i];
            
            if (ValidationConstants.KEYWORD_FILTER.equals(current) 
                    && i + 3 < parts.length 
                    && ValidationConstants.KEYWORD_BY.equals(parts[i + 1])) {
                params.filterType = parts[i + 2];
                params.filterValue = parts[i + 3];
                i += 4;
            } else if (ValidationConstants.KEYWORD_SORT.equals(current) 
                    && i + 3 < parts.length 
                    && ValidationConstants.KEYWORD_BY.equals(parts[i + 1])) {
                params.sortField = parts[i + 2];
                params.sortOrder = parts[i + 3];
                i += 4;
            } else if (!ValidationConstants.KEYWORD_FILTER.equals(current) 
                    && !ValidationConstants.KEYWORD_SORT.equals(current) 
                    && !ValidationConstants.KEYWORD_BY.equals(current)) {
                if (params.projectName == null) {
                    params.projectName = current;
                }
                i++;
            } else {
                i++;
            }
        }
        
        return params;
    }
    
    /**
     * Collects base tasks from specified project or all projects.
     */
    private List<TaskWithProject> collectBaseTasks(String projectName) 
            throws FlowCLIExceptions.InvalidArgumentException {
        if (projectName != null) {
            Project project = projects.getProject(projectName);
            if (project == null) {
                throw new FlowCLIExceptions.InvalidArgumentException("Project not found: " + projectName);
            }
            return TaskCollector.getTasksFromProject(project);
        } else {
            return TaskCollector.getAllTasksWithProjects(projects);
        }
    }
    
    /**
     * Applies filtering to tasks.
     */
    private List<TaskWithProject> applyFiltering(List<TaskWithProject> tasks, 
            String filterType, String filterValue) throws Exception {
        CommandValidator.validateFilterType(filterType);
        
        if (ValidationConstants.FILTER_TYPE_PRIORITY.equals(filterType)) {
            CommandValidator.validatePriority(filterValue);
        }
        
        String priorityParam = ValidationConstants.FILTER_TYPE_PRIORITY.equals(filterType) ? filterValue : null;
        String projectParam = ValidationConstants.FILTER_TYPE_PROJECT.equals(filterType) ? filterValue : null;
        TaskFilter filter = new TaskFilter(tasks, priorityParam, projectParam);
        return filter.getFilteredTasks();
    }
    
    /**
     * Applies sorting to tasks.
     */
    private List<TaskWithProject> applySorting(List<TaskWithProject> tasks, 
            String sortField, String sortOrder) throws Exception {
        CommandValidator.validateSortField(sortField);
        CommandValidator.validateSortOrder(sortOrder);
        
        boolean ascending = ValidationConstants.SORT_ORDER_ASCENDING.equals(sortOrder);
        TaskSorter sorter = new TaskSorter(tasks, sortField, ascending);
        return sorter.getSortedTasks();
    }
    
    /**
     * Helper class to hold parsed parameters.
     */
    private static class ParsedParams {
        String projectName = null;
        String filterType = null;
        String filterValue = null;
        String sortField = null;
        String sortOrder = null;
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
