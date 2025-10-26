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
            throw new InvalidArgumentException(
                    "Legacy export syntax is no longer supported. Use: export-tasks <filename>.txt [projectIndex] "
                            + "[filter-tasks --priority <low/medium/high>] "
                            + "[sort-tasks <--deadline/priority> <ascending/descending>]");
        }

        ExportParams params = parseParameters(trimmed);

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
            FilterResult filterResult = applyFiltering(tasks, params.filterType, params.filterValue);
            tasks = filterResult.tasks;
            params.filterValue = filterResult.resolvedValue;
        }

        if (params.sortField != null && params.sortOrder != null) {
            tasks = applySorting(tasks, params.sortField, params.sortOrder);
        }

        String header = buildExportHeader(baseDescriptor, params);
        TaskExporter.exportTasksToFile(tasks, params.filename, header);
        ui.showExportSuccess(params.filename, tasks.size());
    }

    private ExportParams parseParameters(String args) throws InvalidArgumentException {
        ExportParams params = new ExportParams();
        List<String> tokens = new ArrayList<>(Arrays.asList(args.split("\\s+")));
        if (tokens.isEmpty()) {
            throw invalidExportCommand();
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
                    throw invalidExportCommand();
                }

                String option = tokens.get(index);
                if (!option.startsWith("--")) {
                    throw invalidExportCommand();
                }
                params.filterType = option.substring(2).toLowerCase();
                if (!ValidationConstants.FILTER_TYPE_PRIORITY.equals(params.filterType)) {
                    throw new InvalidArgumentException(
                            "Invalid filter type. Use: filter-tasks --priority <low/medium/high>.");
                }
                index++;
                if (index >= tokens.size()) {
                    throw invalidExportCommand();
                }

                StringBuilder valueBuilder = new StringBuilder(tokens.get(index));
                index++;
                while (index < tokens.size()) {
                    String lookahead = tokens.get(index);
                    if (isSegmentBoundary(lookahead)) {
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
                    throw invalidExportCommand();
                }

                String option = tokens.get(index);
                if (!option.startsWith("--")) {
                    throw invalidExportCommand();
                }
                params.sortField = option.substring(2).toLowerCase();
                index++;
                if (index >= tokens.size()) {
                    throw invalidExportCommand();
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

            throw invalidExportCommand();
        }

        if ((params.filterType == null) != (params.filterValue == null)) {
            throw invalidExportCommand();
        }
        if ((params.sortField == null) != (params.sortOrder == null)) {
            throw invalidExportCommand();
        }
        if (params.forceAll && params.projectIndex != null) {
            throw new InvalidArgumentException("Specify either projectIndex or --all, not both.");
        }

        if (params.filterType != null) {
            params.filterValue = CommandValidator.validatePriority(params.filterValue);
        }

        if (params.sortField != null) {
            CommandValidator.validateSortField(params.sortField);
        }
        if (params.sortOrder != null) {
            CommandValidator.validateSortOrder(params.sortOrder);
        }

        return params;
    }

    private boolean isSegmentBoundary(String token) {
        return "filter-tasks".equals(token) || "sort-tasks".equals(token) || "--all".equalsIgnoreCase(token);
    }

    private boolean isPositiveInteger(String token) {
        try {
            return Integer.parseInt(token) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String buildExportHeader(String baseDescriptor, ExportParams params) {
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

    private InvalidArgumentException invalidExportCommand() {
        return new InvalidArgumentException("Invalid export command. Use: export-tasks <filename>.txt [projectIndex] "
                + "[filter-tasks --priority <low/medium/high>] "
                + "[sort-tasks <--deadline/priority> <ascending/descending>]");
    }

    /**
     * Applies filtering to tasks.
     */
    private FilterResult applyFiltering(List<TaskWithProject> tasks, String filterType, String filterValue)
            throws Exception {
        CommandValidator.validateFilterType(filterType);

        String resolvedValue = CommandValidator.validatePriority(filterValue);
        TaskFilter filter = new TaskFilter(tasks, resolvedValue, null);
        return new FilterResult(filter.getFilteredTasks(), resolvedValue);
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

    private static class FilterResult {
        final List<TaskWithProject> tasks;
        final String resolvedValue;

        FilterResult(List<TaskWithProject> tasks, String resolvedValue) {
            this.tasks = tasks;
            this.resolvedValue = resolvedValue;
        }
    }

    private static class ExportParams {
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
