package seedu.flowcli.commands.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.flowcli.commands.utility.TaskCollector;
import seedu.flowcli.commands.utility.TaskExporter;
import seedu.flowcli.commands.utility.TaskFilter;
import seedu.flowcli.commands.utility.TaskSorter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.ui.ConsoleUi;

/**
 * Handles export command logic and view state tracking for export
 * functionality.
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
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid export command. Use: export-tasks <filename>.txt [projectIndex] "
                            + "[filter-tasks --priority <low/medium/high>] "
                            + "[sort-tasks <--deadline/priority> <ascending/descending>]");
        }

        if (trimmed.startsWith("tasks to ")) {
            handleLegacyExport(trimmed);
        } else {
            handleModernExport(trimmed);
        }
    }

    /**
     * Parses export parameters and returns the appropriate tasks.
     */
    private void handleLegacyExport(String args) throws Exception {
        String[] parts = args.trim().split("\\s+");

        if (parts.length < 3 || !"tasks".equals(parts[0]) || !"to".equals(parts[1])) {
            throw new InvalidArgumentException(
                    "Invalid export command. Use: export tasks to <filename>.txt [<project>] "
                            + "[filter by <type> <value>] [sort by <field> <order>]");
        }

        String filename = parts[2];
        if (!filename.endsWith(".txt")) {
            throw new InvalidArgumentException(
                    "Export filename must end with .txt extension. Use: " + filename + ".txt");
        }

        String remainingArgs = args.substring(args.indexOf(filename) + filename.length()).trim();

        List<TaskWithProject> tasksToExport;
        String header;

        if (remainingArgs.isEmpty()) {
            if (lastViewType != ViewType.NONE && !lastDisplayedTasks.isEmpty()) {
                tasksToExport = new ArrayList<>(lastDisplayedTasks);
                header = "Exported tasks (" + lastViewMetadata + ")";
            } else {
                tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
                header = "Exported all tasks";
            }
        } else if ("--all".equals(remainingArgs.trim())) {
            tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
            header = "Exported all tasks";
        } else {
            tasksToExport = parseExportParameters(remainingArgs);
            header = "Exported tasks (" + remainingArgs + ")";
        }

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

    private void handleModernExport(String args) throws Exception {
        ModernParams params = parseModernParameters(args);

        List<TaskWithProject> tasks;
        String baseDescriptor;

        if (params.forceAll) {
            tasks = TaskCollector.getAllTasksWithProjects(projects);
            baseDescriptor = "all tasks";
        } else if (params.projectIndex != null) {
            if (params.projectIndex < 0 || params.projectIndex >= projects.getProjectListSize()) {
                throw new IndexOutOfRangeException(projects.getProjectListSize());
            }
            Project project = projects.getProjectByIndex(params.projectIndex);
            tasks = TaskCollector.getTasksFromProject(project);
            baseDescriptor = "project " + project.getProjectName();
        } else if (!params.hasFilterOrSort() && lastViewType != ViewType.NONE && !lastDisplayedTasks.isEmpty()) {
            tasks = new ArrayList<>(lastDisplayedTasks);
            baseDescriptor = "last view: " + lastViewMetadata;
        } else {
            tasks = TaskCollector.getAllTasksWithProjects(projects);
            baseDescriptor = "all tasks";
        }

        if (params.filterType != null && params.filterValue != null) {
            tasks = applyFiltering(tasks, params.filterType, params.filterValue);
        }

        if (params.sortField != null && params.sortOrder != null) {
            tasks = applySorting(tasks, params.sortField, params.sortOrder);
        }

        String header = buildModernHeader(baseDescriptor, params);
        TaskExporter.exportTasksToFile(tasks, params.filename, header);
        ui.showExportSuccess(params.filename, tasks.size());
    }

    private ModernParams parseModernParameters(String args) throws InvalidArgumentException {
        ModernParams params = new ModernParams();
        List<String> tokens = new ArrayList<>(Arrays.asList(args.split("\\s+")));
        if (tokens.isEmpty()) {
            throw invalidModernCommand();
        }

        params.filename = tokens.get(0);
        if (!params.filename.endsWith(".txt")) {
            throw new InvalidArgumentException(
                    "Export filename must end with .txt extension. Use: " + params.filename + ".txt");
        }

        int index = 1;
        while (index < tokens.size()) {
            String token = tokens.get(index);

            if ("--all".equalsIgnoreCase(token)) {
                if (params.forceAll) {
                    throw new InvalidArgumentException("Duplicate --all flag detected.");
                }
                params.forceAll = true;
                index++;
                continue;
            }

            if ("filter-tasks".equals(token)) {
                if (params.filterType != null) {
                    throw new InvalidArgumentException("Only one filter condition is supported.");
                }
                index++;
                if (index >= tokens.size()) {
                    throw invalidModernCommand();
                }

                String option = tokens.get(index);
                if (!option.startsWith("--")) {
                    throw invalidModernCommand();
                }
                params.filterType = option.substring(2).toLowerCase();
                index++;
                if (index >= tokens.size()) {
                    throw invalidModernCommand();
                }

                StringBuilder valueBuilder = new StringBuilder(tokens.get(index));
                index++;
                while (index < tokens.size()) {
                    String lookahead = tokens.get(index);
                    if (isModernSegmentBoundary(lookahead)) {
                        break;
                    }
                    valueBuilder.append(" ").append(lookahead);
                    index++;
                }
                params.filterValue = stripQuotes(valueBuilder.toString().trim());
                continue;
            }

            if ("sort-tasks".equals(token)) {
                if (params.sortField != null) {
                    throw new InvalidArgumentException("Only one sort condition is supported.");
                }
                index++;
                if (index >= tokens.size()) {
                    throw invalidModernCommand();
                }

                String option = tokens.get(index);
                if (!option.startsWith("--")) {
                    throw invalidModernCommand();
                }
                params.sortField = option.substring(2).toLowerCase();
                index++;
                if (index >= tokens.size()) {
                    throw invalidModernCommand();
                }

                params.sortOrder = tokens.get(index).toLowerCase();
                index++;
                continue;
            }

            if (params.projectIndex == null && isPositiveInteger(token)) {
                params.projectIndex = Integer.parseInt(token) - 1;
                index++;
                continue;
            }

            throw invalidModernCommand();
        }

        if ((params.filterType == null) != (params.filterValue == null)) {
            throw invalidModernCommand();
        }
        if ((params.sortField == null) != (params.sortOrder == null)) {
            throw invalidModernCommand();
        }
        if (params.forceAll && params.projectIndex != null) {
            throw new InvalidArgumentException("Specify either projectIndex or --all, not both.");
        }

        if (params.filterType != null) {
            if (ValidationConstants.FILTER_TYPE_PRIORITY.equals(params.filterType)) {
                params.filterValue = CommandValidator.validatePriority(params.filterValue);
            } else if (ValidationConstants.FILTER_TYPE_PROJECT.equals(params.filterType)) {
                if (params.filterValue == null || params.filterValue.isEmpty()) {
                    throw new InvalidArgumentException("Project name for filter cannot be empty.");
                }
            } else {
                throw new InvalidArgumentException("Invalid filter type. Use --priority or --project.");
            }
        }

        if (params.sortField != null) {
            CommandValidator.validateSortField(params.sortField);
        }
        if (params.sortOrder != null) {
            CommandValidator.validateSortOrder(params.sortOrder);
        }

        return params;
    }

    private boolean isModernSegmentBoundary(String token) {
        return "filter-tasks".equals(token) || "sort-tasks".equals(token) || "--all".equalsIgnoreCase(token);
    }

    private boolean isPositiveInteger(String token) {
        try {
            return Integer.parseInt(token) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String buildModernHeader(String baseDescriptor, ModernParams params) {
        List<String> parts = new ArrayList<>();
        if (baseDescriptor != null && !baseDescriptor.isEmpty()) {
            parts.add(baseDescriptor);
        }
        if (params.filterType != null && params.filterValue != null) {
            parts.add("filter " + params.filterType + " " + params.filterValue);
        }
        if (params.sortField != null && params.sortOrder != null) {
            parts.add("sort " + params.sortField + " " + params.sortOrder);
        }

        if (parts.isEmpty()) {
            return "Exported tasks";
        }
        if (parts.size() == 1 && "all tasks".equals(parts.get(0))) {
            return "Exported all tasks";
        }
        return "Exported tasks (" + String.join(", ", parts) + ")";
    }

    private InvalidArgumentException invalidModernCommand() {
        return new InvalidArgumentException("Invalid export command. Use: export-tasks <filename>.txt [projectIndex] "
                + "[filter-tasks --priority <low/medium/high>] "
                + "[sort-tasks <--deadline/priority> <ascending/descending>]");
    }

    /**
     * Validates that filter and sort commands are complete.
     */
    private void validateFilterSortCommands(String[] parts) throws InvalidArgumentException {
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

            if (ValidationConstants.KEYWORD_FILTER.equals(current) && i + 3 < parts.length
                    && ValidationConstants.KEYWORD_BY.equals(parts[i + 1])) {
                params.filterType = parts[i + 2];
                params.filterValue = parts[i + 3];
                i += 4;
            } else if (ValidationConstants.KEYWORD_SORT.equals(current) && i + 3 < parts.length
                    && ValidationConstants.KEYWORD_BY.equals(parts[i + 1])) {
                params.sortField = parts[i + 2];
                params.sortOrder = parts[i + 3];
                i += 4;
            } else if (!ValidationConstants.KEYWORD_FILTER.equals(current)
                    && !ValidationConstants.KEYWORD_SORT.equals(current)
                    && !ValidationConstants.KEYWORD_BY.equals(current)) {
                if (params.projectName == null) {
                    params.projectName = stripQuotes(current);
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
    private List<TaskWithProject> collectBaseTasks(String projectName) throws InvalidArgumentException {
        if (projectName != null) {
            Project project = projects.getProject(projectName);
            if (project == null) {
                throw new InvalidArgumentException("Project not found: " + projectName);
            }
            return TaskCollector.getTasksFromProject(project);
        } else {
            return TaskCollector.getAllTasksWithProjects(projects);
        }
    }

    /**
     * Applies filtering to tasks.
     */
    private List<TaskWithProject> applyFiltering(List<TaskWithProject> tasks, String filterType, String filterValue)
            throws Exception {
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
    private List<TaskWithProject> applySorting(List<TaskWithProject> tasks, String sortField, String sortOrder)
            throws Exception {
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

    private static class ModernParams {
        String filename;
        Integer projectIndex;
        boolean forceAll;
        String filterType;
        String filterValue;
        String sortField;
        String sortOrder;

        boolean hasFilterOrSort() {
            boolean hasFilter = filterType != null && filterValue != null;
            boolean hasSort = sortField != null && sortOrder != null;
            return hasFilter || hasSort;
        }
    }

    /**
     * Clears the current view state.
     */
    public void clearViewState() {
        lastDisplayedTasks.clear();
        lastViewType = ViewType.NONE;
        lastViewMetadata = "";
    }

    /**
     * Strips surrounding quotes from a string if present.
     */
    private String stripQuotes(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }
}
