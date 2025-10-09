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

import java.time.LocalDate;
import java.util.Scanner;

public class CommandHandler {
    private ProjectList projects;
    private ConsoleUi ui;

    public CommandHandler(ProjectList projects, ConsoleUi ui) {
        this.projects = projects;
        this.ui = ui;
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
                    ui.showGlobalSortedTasks(sorter.getSortedTasks(), field, order);
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
                        ui.showGlobalFilteredTasks(filter.getFilteredTasks(), type, value);
                    } else if ("project".equals(type)) {
                        TaskFilter filter = new TaskFilter(projects, null, value);
                        ui.showGlobalFilteredTasks(filter.getFilteredTasks(), type, value);
                    } else {
                        throw new FlowCLIExceptions.InvalidArgumentException(
                                "Invalid filter type. Use: priority or project");
                    }
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
