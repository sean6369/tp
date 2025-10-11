package seedu.flowcli.command;

import seedu.flowcli.exception.FlowCLIExceptions;
import seedu.flowcli.exception.FlowCLIExceptions.MissingArgumentException;
import seedu.flowcli.exception.FlowCLIExceptions.MissingDescriptionException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.ui.ConsoleUi;
import seedu.flowcli.tools.TaskSorter;
import seedu.flowcli.tools.TaskFilter;
import seedu.flowcli.export.TaskExporter;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class CommandHandler {
    private ProjectList projects;
    private ConsoleUi ui;

    // View state tracking for export functionality
    private List<TaskExporter.ExportableTask> lastDisplayedTasks;
    private ViewType lastViewType;
    private String lastViewMetadata;

    public enum ViewType {
        NONE, SORTED, FILTERED, PROJECT
    }

    public CommandHandler(ProjectList projects, ConsoleUi ui) {
        this.projects = projects;
        this.ui = ui;
        this.lastDisplayedTasks = new ArrayList<>();
        this.lastViewType = ViewType.NONE;
        this.lastViewMetadata = "";
    }

    public void handleCommands() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();
            CommandParser parsedCommand = new CommandParser(line, projects);

            try {
                switch (parsedCommand.getCommand().type) {
                case BYE:
                    ui.bye();

                    return;

                case LIST: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    if (parsedArgument.getTargetProject() == null) {
                        ui.showProjectList();
                        clearViewState();  // Clear filter/sort state when listing all projects
                        break;
                    } else {
                        ui.showTaskList(parsedArgument.getTargetProject());
                    }

                    break;
                }

                case MARK: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        throw new MissingArgumentException();
                    }

                    Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(),
                            targetProject.size());

                    targetProject.getProjectTasks().mark(idx);
                    ui.showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx),
                            true);
                    break;
                }

                case UNMARK: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        throw new MissingArgumentException();
                    }

                    Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(),
                            targetProject.size());

                    targetProject.getProjectTasks().unmark(idx);
                    ui.showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx),
                            true);
                    break;
                }

                case ADD: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        if (parsedArgument.getRemainingArgument() == null) {
                            throw new MissingArgumentException();
                        }
                        projects.addProject(parsedArgument.getRemainingArgument());
                        ui.showAddedProject();
                        break;
                    }

                    if (parsedArgument.getRemainingArgument() == null) {
                        throw new MissingDescriptionException();
                    }

                    // Parse task arguments: description [--priority value] [--deadline value]
                    String args = parsedArgument.getRemainingArgument();
                    String[] parts = args.split(" --");
                    String description = parts[0].trim();
                    int priority = 2; // default medium
                    LocalDate deadline = null;

                    for (int i = 1; i < parts.length; i++) {
                        String option = parts[i].trim();
                        if (option.startsWith("priority ")) {
                            String priStr = option.substring(9).trim().toLowerCase();
                            switch (priStr) {
                            case "low":
                                priority = 1;
                                break;
                            case "medium":
                                priority = 2;
                                break;
                            case "high":
                                priority = 3;
                                break;
                            default:
                                throw new FlowCLIExceptions.InvalidArgumentException(
                                        "Invalid priority: " + priStr + ". Use low, medium, or high.");
                            }
                        } else if (option.startsWith("deadline ")) {
                            String dateStr = option.substring(9).trim();
                            try {
                                deadline = LocalDate.parse(dateStr);
                            } catch (Exception e) {
                                throw new FlowCLIExceptions.InvalidArgumentException(
                                        "Invalid deadline format: " + dateStr + ". Use YYYY-MM-DD.");
                            }
                        } else {
                            throw new FlowCLIExceptions.InvalidArgumentException(
                                    "Unknown option: " + option + ". Use --priority or --deadline.");
                        }
                    }

                    targetProject.addTask(description, deadline, priority);
                    ui.showAddedTask(targetProject);
                    break;

                }

                case DELETE: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        throw new MissingArgumentException();

                    }

                    // deleting projects
                    if (parsedArgument.getRemainingArgument() == null) {
                        Project deletedProject = projects.deleteProject(targetProject);
                        ui.showDeletedProject(deletedProject);
                        break;
                    }

                    // deleting tasks
                    Integer index = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(),
                            targetProject.size());
                    Task deletedTask = targetProject.deleteTask(index);
                    ui.showDeletedTask(targetProject, deletedTask);
                    break;

                }

                case HELP: {
                    ui.showHelp();
                    break;
                }

                case SORT: {
                    String args = parsedCommand.getCommand().arg;
                    if (args == null || !args.startsWith("tasks by")) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid sort command. Use: sort tasks by deadline/priority ascending/descending");
                    }

                    String[] parts = args.split("\\s+");
                    if (parts.length < 4) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid sort command. Use: sort tasks by deadline/priority ascending/descending");
                    }

                    String field = parts[2];
                    String order = parts[3];
                    boolean ascending = "ascending".equals(order);

                    if (!("deadline".equals(field) || "priority".equals(field))) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid sort field. Use: deadline or priority");
                    }

                    if (!("ascending".equals(order) || "descending".equals(order))) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid sort order. Use: ascending or descending");
                    }

                    TaskSorter sorter = new TaskSorter(projects, field, ascending);
                    List<TaskSorter.SortedTask> sortedTasks = sorter.getSortedTasks();
                    ui.showGlobalSortedTasks(sortedTasks, field, order);

                    // Update view state for export
                    updateViewState(sortedTasks, ViewType.SORTED, "sorted by " + field + " " + order);
                    break;
                }

                case FILTER: {
                    String args = parsedCommand.getCommand().arg;
                    if (args == null || !args.startsWith("tasks by")) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid filter command. Use: filter tasks by priority <value> or project <name>");
                    }

                    String[] parts = args.split("\\s+");
                    if (parts.length < 4) {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid filter command. Use: filter tasks by priority <value> or project <name>");
                    }

                    String type = parts[2];
                    String value = parts[3];

                    if ("priority".equals(type)) {
                        TaskFilter filter = new TaskFilter(projects, value, null);
                        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();
                        ui.showGlobalFilteredTasks(filteredTasks, type, value);

                        // Update view state for export
                        updateViewState(filteredTasks, ViewType.FILTERED, "filtered by " + type + " " + value);
                    } else if ("project".equals(type)) {
                        TaskFilter filter = new TaskFilter(projects, null, value);
                        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();
                        ui.showGlobalFilteredTasks(filteredTasks, type, value);

                        // Update view state for export
                        updateViewState(filteredTasks, ViewType.FILTERED, "filtered by " + type + " " + value);
                    } else {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid filter type. Use: priority or project");
                    }
                    break;
                }

                case EXPORT: {
                    handleExportCommand(parsedCommand.getCommand().arg);
                    break;
                }

                case UNKNOWN: {
                    throw new FlowCLIExceptions.UnknownInputException();
                }

                default:

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                ui.printLine();
            }
        }

    }

    /**
     * Updates the view state tracking for export functionality.
     */
    private void updateViewState(List<?> tasks, ViewType viewType, String metadata) {
        lastDisplayedTasks.clear();
        lastViewType = viewType;
        lastViewMetadata = metadata;

        // Convert tasks to ExportableTask format
        for (Object task : tasks) {
            if (task instanceof TaskSorter.SortedTask) {
                TaskSorter.SortedTask sortedTask = (TaskSorter.SortedTask) task;
                lastDisplayedTasks.add(new TaskExporter.ExportableTask(sortedTask.getProjectName(),
                        sortedTask.getTask()));
            } else if (task instanceof TaskFilter.FilteredTask) {
                TaskFilter.FilteredTask filteredTask = (TaskFilter.FilteredTask) task;
                lastDisplayedTasks.add(new TaskExporter.ExportableTask(filteredTask.getProjectName(),
                        filteredTask.getTask()));
            }
        }
    }

    /**
     * Handles the export command with various parameter combinations.
     */
    private void handleExportCommand(String args) throws Exception {
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
        String header = "";

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
            if (!parts[i].equals("filter") && !parts[i].equals("sort") && !parts[i].equals("by")) {
                // This might be a project name
                projectName = parts[i];
                i++;
            } else if (i < parts.length - 2 && "filter".equals(parts[i]) && "by".equals(parts[i + 1])) {
                filterType = parts[i + 2];
                if (i + 3 < parts.length) {
                    filterValue = parts[i + 3];
                }
                i += 4;
            } else if (i < parts.length - 2 && "sort".equals(parts[i]) && "by".equals(parts[i + 1])) {
                sortField = parts[i + 2];
                if (i + 3 < parts.length) {
                    sortOrder = parts[i + 3];
                }
                i += 4;
            } else {
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
            tasks = applySort(tasks, sortField, sortOrder);
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
                if (task.getPriorityString().toLowerCase().equals(filterValue.toLowerCase())) {
                    filteredTasks.add(exportableTask);
                }
            } else if ("project".equals(filterType)) {
                if (exportableTask.getProjectName().toLowerCase().equals(filterValue.toLowerCase())) {
                    filteredTasks.add(exportableTask);
                }
            }
        }

        return filteredTasks;
    }

    /**
     * Applies sort to the list of tasks.
     */
    private List<TaskExporter.ExportableTask> applySort(List<TaskExporter.ExportableTask> tasks,
                                                        String sortField, String sortOrder) {
        boolean ascending = "ascending".equals(sortOrder);

        tasks.sort((t1, t2) -> {
            Task task1 = t1.getTask();
            Task task2 = t2.getTask();
            int comparison = 0;

            if ("deadline".equals(sortField)) {
                // Handle null deadlines
                if (task1.getDeadline() == null && task2.getDeadline() == null) {
                    comparison = 0;
                } else if (task1.getDeadline() == null) {
                    comparison = 1; // task1 after task2
                } else if (task2.getDeadline() == null) {
                    comparison = -1; // task1 before task2
                } else {
                    comparison = task1.getDeadline().compareTo(task2.getDeadline());
                }
            } else if ("priority".equals(sortField)) {
                comparison = Integer.compare(task1.getPriority(), task2.getPriority());
            }

            return ascending ? comparison : -comparison;
        });

        return tasks;
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
    private void clearViewState() {
        lastDisplayedTasks.clear();
        lastViewType = ViewType.NONE;
        lastViewMetadata = "";
    }
}
