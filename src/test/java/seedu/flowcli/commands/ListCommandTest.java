package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.EmptyTaskListException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

//@@author sean6369
@DisplayName("ListCommand Unit Tests")
class ListCommandTest {

    static class SpyUi extends ConsoleUi {
        String lastAction = null;
        Project lastProject = null;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showProjectList() {
            lastAction = "showProjectList";
        }

        @Override
        public void showTaskList(Project project) {
            lastAction = "showTaskList";
            lastProject = project;
        }
    }

    static class SpyExportHandler extends ExportCommandHandler {
        boolean viewStateCleared = false;

        SpyExportHandler(ProjectList projects, ConsoleUi ui) {
            super(projects, ui);
        }

        @Override
        public void clearViewState() {
            viewStateCleared = true;
        }
    }

    private ProjectList projects;
    private SpyUi ui;
    private SpyExportHandler exportHandler;
    private CommandContext ctx;

    @BeforeEach
    void setUp() throws Exception {
        projects = new ProjectList();
        ui = new SpyUi(projects);
        exportHandler = new SpyExportHandler(projects, ui);
        ctx = new CommandContext(projects, ui, exportHandler, null);
    }

    @Test
    @DisplayName("execute_withAllFlag_showsProjectList")
    void executeWithAllFlag() throws Exception {
        projects.addProject("Project1");
        projects.addProject("Project2");
        ListCommand cmd = new ListCommand("--all");

        boolean result = cmd.execute(ctx);

        assertAll("List all projects",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showProjectList", ui.lastAction),
                () -> assertTrue(exportHandler.viewStateCleared, "View state should be cleared")
        );
    }

    @Test
    @DisplayName("execute_withValidProjectIndex_showsTaskList")
    void executeWithValidIndex() throws Exception {
        projects.addProject("TestProject");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task1");
        project.addTask("Task2");
        ListCommand cmd = new ListCommand("1");

        boolean result = cmd.execute(ctx);

        assertAll("List tasks in project",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showTaskList", ui.lastAction),
                () -> assertEquals(project, ui.lastProject),
                () -> assertTrue(exportHandler.viewStateCleared, "View state should be cleared")
        );
    }

    @Test
    @DisplayName("execute_emptyArguments_throwsMissingArgumentException")
    void executeEmptyArguments() {
        ListCommand cmd = new ListCommand("");

        assertThrows(MissingArgumentException.class, () -> cmd.execute(ctx),
                "Should throw when arguments are empty");
    }

    @Test
    @DisplayName("execute_allFlagWithEmptyProjects_throwsEmptyProjectListException")
    void executeAllFlagEmptyProjects() {
        ListCommand cmd = new ListCommand("--all");

        assertThrows(EmptyProjectListException.class, () -> cmd.execute(ctx),
                "Should throw when project list is empty");
    }

    @Test
    @DisplayName("execute_projectWithNoTasks_throwsEmptyTaskListException")
    void executeProjectWithNoTasks() {
        projects.addProject("EmptyProject");
        ListCommand cmd = new ListCommand("1");

        assertThrows(EmptyTaskListException.class, () -> cmd.execute(ctx),
                "Should throw when project has no tasks");
    }

    @Test
    @DisplayName("execute_invalidProjectIndex_throwsIndexOutOfRangeException")
    void executeInvalidProjectIndex() {
        projects.addProject("Project1");
        ListCommand cmd = new ListCommand("5");

        assertThrows(Exception.class, () -> cmd.execute(ctx),
                "Should throw when project index is invalid");
    }

    @Test
    @DisplayName("execute_allFlagCaseInsensitive_showsProjectList")
    void executeAllFlagCaseInsensitive() throws Exception {
        projects.addProject("Project1");
        ListCommand cmdUpper = new ListCommand("--ALL");
        ListCommand cmdMixed = new ListCommand("--AlL");

        boolean resultUpper = cmdUpper.execute(ctx);
        boolean resultMixed = cmdMixed.execute(ctx);

        assertTrue(resultUpper && resultMixed, "All flag should be case insensitive");
    }
}
//@@author

