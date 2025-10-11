package seedu.flowcli.command;

import seedu.flowcli.exception.FlowCLIExceptions;
import seedu.flowcli.exception.FlowCLIExceptions.MissingArgumentException;
import seedu.flowcli.exception.FlowCLIExceptions.MissingDescriptionException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.ui.ConsoleUi;
import seedu.flowcli.tools.TaskSorter;
import seedu.flowcli.tools.TaskFilter;
import seedu.flowcli.validation.ValidationConstants;
import seedu.flowcli.validation.CommandValidator;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class CommandHandler {
    private ProjectList projects;
    private ConsoleUi ui;

    private ExportCommandHandler exportHandler;

    public CommandHandler(ProjectList projects, ConsoleUi ui) {
        this.projects = projects;
        this.ui = ui;
        this.exportHandler = new ExportCommandHandler(projects, ui);
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
                        exportHandler.clearViewState();  // Clear filter/sort state when listing all projects
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
                            String priStr = option.substring(9).trim();
                            String validatedPriority = CommandValidator.validatePriority(priStr);
                            priority = CommandValidator.priorityToInt(validatedPriority);
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
                    boolean ascending = ValidationConstants.SORT_ORDER_ASCENDING.equals(order);

                    CommandValidator.validateSortField(field);
                    CommandValidator.validateSortOrder(order);

                    TaskSorter sorter = new TaskSorter(projects, field, ascending);
                    List<TaskWithProject> sortedTasks = sorter.getSortedTasks();
                    ui.showGlobalSortedTasks(sortedTasks, field, order);

                    // Update view state for export
                    exportHandler.updateViewState(sortedTasks, ExportCommandHandler.ViewType.SORTED, 
                            "sorted by " + field + " " + order);
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

                    if (ValidationConstants.FILTER_TYPE_PRIORITY.equals(type)) {
                        CommandValidator.validatePriority(value);

                        TaskFilter filter = new TaskFilter(projects, value, null);
                        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
                        ui.showGlobalFilteredTasks(filteredTasks, type, value);

                        // Update view state for export
                        exportHandler.updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                                "filtered by " + type + " " + value);
                    } else if (ValidationConstants.FILTER_TYPE_PROJECT.equals(type)) {
                        TaskFilter filter = new TaskFilter(projects, null, value);
                        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();
                        ui.showGlobalFilteredTasks(filteredTasks, type, value);

                        // Update view state for export
                        exportHandler.updateViewState(filteredTasks, ExportCommandHandler.ViewType.FILTERED,
                                "filtered by " + type + " " + value);
                    } else {
                        CommandValidator.validateFilterType(type);
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid filter type. Use: priority or project");
                    }
                    break;
                }

                case EXPORT: {
                    exportHandler.handleExport(parsedCommand.getCommand().arg);
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
}
