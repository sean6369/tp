package seedu.FlowCLI.command;

import seedu.FlowCLI.exception.FlowcliExceptions;
import seedu.FlowCLI.exception.FlowcliExceptions.MissingArgumentException;
import seedu.FlowCLI.exception.FlowcliExceptions.MissingDescriptionException;
import seedu.FlowCLI.project.Project;
import seedu.FlowCLI.project.ProjectList;
import seedu.FlowCLI.task.Task;
import seedu.FlowCLI.ui.ConsoleUi;

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

                    Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

                    targetProject.getProjectTasks().mark(idx);
                    ui.showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), true);
                    break;
                }

                case UNMARK: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        throw new MissingArgumentException();
                    }

                    Integer idx = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());

                    targetProject.getProjectTasks().unmark(idx);
                    ui.showMarked(targetProject.getProjectName(), targetProject.getProjectTasks().get(idx), true);
                    break;
                }

                case ADD: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        if(parsedArgument.getRemainingArgument() == null) {
                            throw new MissingArgumentException();
                        }
                        projects.addProject(parsedArgument.getRemainingArgument());
                        ui.showAddedProject();
                        break;
                    }

                    if (parsedArgument.getRemainingArgument() == null) {
                        throw new MissingDescriptionException();
                    }

                    targetProject.addTask(parsedArgument.getRemainingArgument());
                    ui.showAddedTask(targetProject);
                    break;

                }

                case DELETE: {
                    ArgumentParser parsedArgument = new ArgumentParser(parsedCommand.getCommand().arg, projects);
                    Project targetProject = parsedArgument.getTargetProject();
                    if (targetProject == null) {
                        throw new MissingArgumentException();


                    }

                    //deleting projects
                    if (parsedArgument.getRemainingArgument() == null) {
                        Project deletedProject = projects.deleteProject(targetProject);
                        ui.showDeletedProject(deletedProject);
                        break;
                    }

                    //deleting tasks
                    Integer index = CommandParser.parseIndexOrNull(parsedArgument.getRemainingArgument(), targetProject.size());
                    Task deletedTask = targetProject.deleteTask(index);
                    ui.showDeletedTask(targetProject, deletedTask);
                    break;



                }

                case UNKNOWN: {
                    throw new FlowcliExceptions.UnknownInputException();
                }



                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
                ui.printLine();
            }
        }

    }
}
