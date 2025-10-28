package seedu.flowcli.unused;

import java.util.ArrayList;
import java.util.List;

import seedu.flowcli.commands.utility.TaskCollector;
import seedu.flowcli.commands.utility.TaskExporter;
import seedu.flowcli.commands.utility.TaskFilter;
import seedu.flowcli.commands.utility.TaskSorter;
import seedu.flowcli.commands.validation.CommandValidator;
import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.ui.ConsoleUi;

//@@author sean6369
@SuppressWarnings("unused")
public final class LegacyExportSupport {

    private LegacyExportSupport() {
    }

    public static void handleLegacyExport(ProjectList projects, ConsoleUi ui, List<TaskWithProject> lastDisplayedTasks,
            LegacyViewState state, String args) throws Exception {
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
            if (state.lastViewType != ViewType.NONE && !lastDisplayedTasks.isEmpty()) {
                tasksToExport = new ArrayList<>(lastDisplayedTasks);
                header = "Exported tasks (" + state.lastViewMetadata + ")";
            } else {
                tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
                header = "Exported all tasks";
            }
        } else if ("--all".equals(remainingArgs.trim())) {
            tasksToExport = TaskCollector.getAllTasksWithProjects(projects);
            header = "Exported all tasks";
        } else {
            tasksToExport = parseExportParameters(projects, remainingArgs);
            header = "Exported tasks (" + remainingArgs + ")";
        }

        TaskExporter.exportTasksToFile(tasksToExport, filename, header);
        ui.showExportSuccess(filename, tasksToExport.size());
    }

    private static List<TaskWithProject> parseExportParameters(ProjectList projects, String args) throws Exception {
        String[] parts = args.split("\\s+");

        validateFilterSortCommands(parts);

        ParsedParams params = parseCommandTokens(parts);

        List<TaskWithProject> tasks = collectBaseTasks(projects, params.projectName);

        if (params.filterType != null && params.filterValue != null) {
            FilterResult filterResult = applyFiltering(projects, tasks, params.filterType, params.filterValue);
            tasks = filterResult.tasks;
            params.filterValue = filterResult.resolvedValue;
        }

        if (params.sortField != null && params.sortOrder != null) {
            tasks = applySorting(tasks, params.sortField, params.sortOrder);
        }

        return tasks;
    }

    private static void validateFilterSortCommands(String[] parts) throws InvalidArgumentException {
        for (int j = 0; j < parts.length; j++) {
            if (ValidationConstants.KEYWORD_FILTER.equals(parts[j])) {
                CommandValidator.validateFilterCommand(parts, j);
            }
            if (ValidationConstants.KEYWORD_SORT.equals(parts[j])) {
                CommandValidator.validateSortCommand(parts, j);
            }
        }
    }

    private static ParsedParams parseCommandTokens(String[] parts) {
        ParsedParams params = new ParsedParams();

        int i = 0;
        while (i < parts.length) {
            String current = parts[i];

            if (ValidationConstants.KEYWORD_FILTER.equals(current) && i + 3 < parts.length
                    && ValidationConstants.KEYWORD_BY.equals(parts[i + 1])) {
                params.filterType = parts[i + 2];
                params.filterValue = stripQuotes(parts[i + 3]);
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

    private static List<TaskWithProject> collectBaseTasks(ProjectList projects, String projectName)
            throws InvalidArgumentException {
        if (projectName != null) {
            Project project = projects.getProject(projectName);
            if (project == null) {
                throw new InvalidArgumentException("Project not found: " + projectName);
            }
            return TaskCollector.getTasksFromProject(project);
        }
        return TaskCollector.getAllTasksWithProjects(projects);
    }

    private static FilterResult applyFiltering(ProjectList projects, List<TaskWithProject> tasks, String filterType,
            String filterValue) throws Exception {
        CommandValidator.validateFilterType(filterType);

        String resolvedValue = CommandValidator.validatePriority(filterValue);
        TaskFilter filter = new TaskFilter(tasks, resolvedValue, null);
        return new FilterResult(filter.getFilteredTasks(), resolvedValue);
    }

    private static List<TaskWithProject> applySorting(List<TaskWithProject> tasks, String sortField, String sortOrder)
            throws Exception {
        CommandValidator.validateSortField(sortField);
        CommandValidator.validateSortOrder(sortOrder);

        boolean ascending = ValidationConstants.SORT_ORDER_ASCENDING.equals(sortOrder);
        TaskSorter sorter = new TaskSorter(tasks, sortField, ascending);
        return sorter.getSortedTasks();
    }

    private static String stripQuotes(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    public static final class LegacyViewState {
        public ViewType lastViewType = ViewType.NONE;
        public String lastViewMetadata = "";
    }

    public enum ViewType {
        NONE, SORTED, FILTERED, PROJECT
    }

    private static final class ParsedParams {
        String projectName = null;
        String filterType = null;
        String filterValue = null;
        String sortField = null;
        String sortOrder = null;
    }

    private static final class FilterResult {
        final List<TaskWithProject> tasks;
        final String resolvedValue;

        FilterResult(List<TaskWithProject> tasks, String resolvedValue) {
            this.tasks = tasks;
            this.resolvedValue = resolvedValue;
        }
    }
}
