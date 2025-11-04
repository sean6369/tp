package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.EmptyTaskListException;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.TaskWithProject;
import seedu.flowcli.ui.ConsoleUi;

@DisplayName("SortCommand Unit Tests")
class SortCommandTest {

    static class SpyUi extends ConsoleUi {
        String lastAction = null;
        List<TaskWithProject> lastTasks = null;
        String lastField = null;
        String lastOrder = null;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showGlobalSortedTasks(List<TaskWithProject> tasks, String field, String order) {
            lastAction = "showGlobalSortedTasks";
            lastTasks = tasks;
            lastField = field;
            lastOrder = order;
        }
    }

    static class SpyExportHandler extends ExportCommandHandler {
        boolean viewStateUpdated = false;

        SpyExportHandler(ProjectList projects, ConsoleUi ui) {
            super(projects, ui);
        }

        @Override
        public void updateViewState(List<TaskWithProject> tasks, ViewType viewType, String description) {
            viewStateUpdated = true;
        }
    }

    private ProjectList projects;
    private SpyUi ui;
    private SpyExportHandler exportHandler;
    private CommandContext ctx;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        ui = new SpyUi(projects);
        exportHandler = new SpyExportHandler(projects, ui);
        ctx = new CommandContext(projects, ui, exportHandler, null);
    }

    @Test
    @DisplayName("execute_sortByDeadlineAscending_sortsCorrectly")
    void executeSortByDeadlineAscending() throws Exception {
        projects.addProject("Project1");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1", LocalDate.of(2024, 12, 31), 2);
        project.addTask("Task2", LocalDate.of(2024, 1, 1), 2);
        
        SortCommand cmd = new SortCommand("--deadline ascending");

        boolean result = cmd.execute(ctx);

        assertAll("Sort by deadline ascending",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showGlobalSortedTasks", ui.lastAction),
                () -> assertEquals("deadline", ui.lastField),
                () -> assertEquals("ascending", ui.lastOrder),
                () -> assertTrue(exportHandler.viewStateUpdated, "View state should be updated")
        );
    }

    @Test
    @DisplayName("execute_sortByPriorityDescending_sortsCorrectly")
    void executeSortByPriorityDescending() throws Exception {
        projects.addProject("Project1");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1", null, 1);
        project.addTask("Task2", null, 3);
        
        SortCommand cmd = new SortCommand("--priority descending");

        boolean result = cmd.execute(ctx);

        assertAll("Sort by priority descending",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showGlobalSortedTasks", ui.lastAction),
                () -> assertEquals("priority", ui.lastField),
                () -> assertEquals("descending", ui.lastOrder)
        );
    }

    @Test
    @DisplayName("execute_emptyArguments_throwsInvalidCommandSyntaxException")
    void executeEmptyArguments() {
        SortCommand cmd = new SortCommand("");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when arguments are empty");
    }

    @Test
    @DisplayName("execute_emptyProjectList_throwsEmptyProjectListException")
    void executeEmptyProjectList() {
        SortCommand cmd = new SortCommand("--deadline ascending");

        assertThrows(EmptyProjectListException.class, () -> cmd.execute(ctx),
                "Should throw when project list is empty");
    }

    @Test
    @DisplayName("execute_invalidFieldFormat_throwsInvalidCommandSyntaxException")
    void executeInvalidFieldFormat() {
        projects.addProject("Project1");
        SortCommand cmd = new SortCommand("deadline ascending"); // missing --

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when field doesn't start with --");
    }

    @Test
    @DisplayName("execute_invalidField_throwsException")
    void executeInvalidField() throws Exception {
        projects.addProject("Project1");
        projects.getProjectByIndex(0).addTask("Task1");
        SortCommand cmd = new SortCommand("--invalid ascending");

        assertThrows(Exception.class, () -> cmd.execute(ctx),
                "Should throw when field is invalid");
    }

    @Test
    @DisplayName("execute_invalidOrder_throwsException")
    void executeInvalidOrder() throws Exception {
        projects.addProject("Project1");
        projects.getProjectByIndex(0).addTask("Task1");
        SortCommand cmd = new SortCommand("--deadline invalid");

        assertThrows(Exception.class, () -> cmd.execute(ctx),
                "Should throw when order is invalid");
    }

    @Test
    @DisplayName("execute_tooFewArguments_throwsInvalidCommandSyntaxException")
    void executeTooFewArguments() {
        projects.addProject("Project1");
        SortCommand cmd = new SortCommand("--deadline");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when order is missing");
    }

    @Test
    @DisplayName("execute_tooManyArguments_throwsInvalidCommandSyntaxException")
    void executeTooManyArguments() {
        projects.addProject("Project1");
        SortCommand cmd = new SortCommand("--deadline ascending extra");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when too many arguments provided");
    }

    @Test
    @DisplayName("execute_projectsWithNoTasks_throwsEmptyTaskListException")
    void executeProjectsWithNoTasks() {
        projects.addProject("EmptyProject");
        SortCommand cmd = new SortCommand("--deadline ascending");

        assertThrows(EmptyTaskListException.class, () -> cmd.execute(ctx),
                "Should throw when all projects have no tasks");
    }
}

