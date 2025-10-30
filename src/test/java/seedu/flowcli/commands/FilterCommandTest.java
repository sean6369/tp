package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

@DisplayName("FilterCommand Unit Tests")
class FilterCommandTest {

    static class SpyUi extends ConsoleUi {
        String lastAction = null;
        List<TaskWithProject> lastTasks = null;
        String lastType = null;
        String lastValue = null;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showGlobalFilteredTasks(List<TaskWithProject> tasks, String type, String value) {
            lastAction = "showGlobalFilteredTasks";
            lastTasks = tasks;
            lastType = type;
            lastValue = value;
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
        ctx = new CommandContext(projects, ui, exportHandler);
    }

    @Test
    @DisplayName("execute_filterByHighPriority_filtersCorrectly")
    void executeFilterByHighPriority() throws Exception {
        projects.addProject("Project1");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1", null, 3); // high
        project.addTask("Task2", null, 1); // low
        
        FilterCommand cmd = new FilterCommand("--priority high");

        boolean result = cmd.execute(ctx);

        assertAll("Filter by high priority",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showGlobalFilteredTasks", ui.lastAction),
                () -> assertEquals("priority", ui.lastType),
                () -> assertEquals("high", ui.lastValue),
                () -> assertTrue(exportHandler.viewStateUpdated, "View state should be updated")
        );
    }

    @Test
    @DisplayName("execute_filterByMediumPriority_filtersCorrectly")
    void executeFilterByMediumPriority() throws Exception {
        projects.addProject("Project1");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1", null, 2); // medium
        project.addTask("Task2", null, 3); // high
        
        FilterCommand cmd = new FilterCommand("--priority medium");

        boolean result = cmd.execute(ctx);

        assertAll("Filter by medium priority",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showGlobalFilteredTasks", ui.lastAction),
                () -> assertEquals("priority", ui.lastType),
                () -> assertEquals("medium", ui.lastValue)
        );
    }

    @Test
    @DisplayName("execute_filterByLowPriority_filtersCorrectly")
    void executeFilterByLowPriority() throws Exception {
        projects.addProject("Project1");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1", null, 1); // low
        
        FilterCommand cmd = new FilterCommand("--priority low");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertEquals("low", ui.lastValue);
    }

    @Test
    @DisplayName("execute_emptyArguments_throwsInvalidCommandSyntaxException")
    void executeEmptyArguments() {
        FilterCommand cmd = new FilterCommand("");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when arguments are empty");
    }

    @Test
    @DisplayName("execute_emptyProjectList_throwsEmptyProjectListException")
    void executeEmptyProjectList() {
        FilterCommand cmd = new FilterCommand("--priority high");

        assertThrows(EmptyProjectListException.class, () -> cmd.execute(ctx),
                "Should throw when project list is empty");
    }

    @Test
    @DisplayName("execute_noMatchingTasks_throwsEmptyTaskListException")
    void executeNoMatchingTasks() throws Exception {
        projects.addProject("Project1");
        projects.getProjectByIndex(0).addTask("Task1", null, 1); // low priority
        FilterCommand cmd = new FilterCommand("--priority high"); // filtering for high

        assertThrows(EmptyTaskListException.class, () -> cmd.execute(ctx),
                "Should throw when no tasks match filter");
    }

    @Test
    @DisplayName("execute_missingFlagPrefix_throwsInvalidCommandSyntaxException")
    void executeMissingFlagPrefix() {
        projects.addProject("Project1");
        FilterCommand cmd = new FilterCommand("priority high"); // missing --

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when flag prefix -- is missing");
    }

    @Test
    @DisplayName("execute_missingValue_throwsInvalidCommandSyntaxException")
    void executeMissingValue() {
        projects.addProject("Project1");
        FilterCommand cmd = new FilterCommand("--priority");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when priority value is missing");
    }

    @Test
    @DisplayName("execute_emptyValue_throwsInvalidCommandSyntaxException")
    void executeEmptyValue() {
        projects.addProject("Project1");
        FilterCommand cmd = new FilterCommand("--priority   ");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when priority value is empty");
    }

    @Test
    @DisplayName("execute_invalidPriority_throwsException")
    void executeInvalidPriority() throws Exception {
        projects.addProject("Project1");
        projects.getProjectByIndex(0).addTask("Task1");
        FilterCommand cmd = new FilterCommand("--priority invalid");

        assertThrows(Exception.class, () -> cmd.execute(ctx),
                "Should throw when priority value is invalid");
    }

    @Test
    @DisplayName("execute_invalidFilterType_throwsException")
    void executeInvalidFilterType() throws Exception {
        projects.addProject("Project1");
        projects.getProjectByIndex(0).addTask("Task1");
        FilterCommand cmd = new FilterCommand("--invalid value");

        assertThrows(Exception.class, () -> cmd.execute(ctx),
                "Should throw when filter type is invalid");
    }

    @Test
    @DisplayName("execute_projectsWithNoTasks_throwsEmptyTaskListException")
    void executeProjectsWithNoTasks() {
        projects.addProject("EmptyProject");
        FilterCommand cmd = new FilterCommand("--priority high");

        assertThrows(EmptyTaskListException.class, () -> cmd.execute(ctx),
                "Should throw when all projects have no tasks");
    }
}

