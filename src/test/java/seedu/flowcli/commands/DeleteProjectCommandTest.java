package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidCommandSyntaxException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

@DisplayName("DeleteProjectCommand Unit Tests")
class DeleteProjectCommandTest {

    static class SpyUi extends ConsoleUi {
        boolean deletedShown = false;
        Project deletedProject = null;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showDeletedProject(Project project) {
            deletedShown = true;
            deletedProject = project;
        }
    }

    private ProjectList projects;
    private SpyUi ui;
    private CommandContext ctx;

    private CommandContext makeContext(ProjectList projects, ConsoleUi ui) {
        return new CommandContext(projects, ui, null);
    }

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        projects.addProject("Project1");
        projects.addProject("Project2");
        ui = new SpyUi(projects);
        ctx = makeContext(projects, ui);
    }

    @Test
    @DisplayName("execute_withConfirmFlag_deletesProject")
    void executeWithConfirm() throws Exception {
        DeleteProjectCommand cmd = new DeleteProjectCommand("1 --confirm");
        Project projectToDelete = projects.getProjectByIndex(0);

        boolean result = cmd.execute(ctx);

        assertAll("Delete with confirm",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertTrue(ui.deletedShown, "UI should show deleted project"),
                () -> assertEquals(projectToDelete, ui.deletedProject),
                () -> assertEquals(1, projects.getProjectListSize(),
                        "Project list should have one less project")
        );
    }

    @Test
    @DisplayName("execute_withoutConfirmFlag_throwsInvalidCommandSyntaxException")
    void executeWithoutConfirm() {
        DeleteProjectCommand cmd = new DeleteProjectCommand("1");

        assertThrows(InvalidCommandSyntaxException.class, () -> cmd.execute(ctx),
                "Should throw when --confirm flag is missing");
        assertFalse(ui.deletedShown, "UI should not show deleted project");
        assertEquals(2, projects.getProjectListSize(), "Project list should remain unchanged");
    }

    @Test
    @DisplayName("execute_emptyArguments_throwsMissingArgumentException")
    void executeEmptyArguments() {
        DeleteProjectCommand cmd = new DeleteProjectCommand("");

        assertThrows(MissingArgumentException.class, () -> cmd.execute(ctx),
                "Should throw when arguments are empty");
    }

    @Test
    @DisplayName("execute_invalidIndex_throwsIndexOutOfRangeException")
    void executeInvalidIndex() {
        DeleteProjectCommand cmd = new DeleteProjectCommand("10 --confirm");

        assertThrows(IndexOutOfRangeException.class, () -> cmd.execute(ctx),
                "Should throw when index is out of range");
        assertEquals(2, projects.getProjectListSize(), "Project list should remain unchanged");
    }

    @Test
    @DisplayName("execute_zeroIndex_throwsIndexOutOfRangeException")
    void executeZeroIndex() {
        DeleteProjectCommand cmd = new DeleteProjectCommand("0 --confirm");

        assertThrows(IndexOutOfRangeException.class, () -> cmd.execute(ctx),
                "Should throw when index is zero");
    }

    @Test
    @DisplayName("execute_negativeIndex_throwsIndexOutOfRangeException")
    void executeNegativeIndex() {
        DeleteProjectCommand cmd = new DeleteProjectCommand("-1 --confirm");

        assertThrows(IndexOutOfRangeException.class, () -> cmd.execute(ctx),
                "Should throw when index is negative");
    }

    @Test
    @DisplayName("execute_confirmFlagCaseInsensitive_deletesProject")
    void executeConfirmFlagCaseInsensitive() throws Exception {
        projects.addProject("Project3");
        DeleteProjectCommand cmd = new DeleteProjectCommand("1 --CONFIRM");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "Confirm flag should be case insensitive");
        assertEquals(2, projects.getProjectListSize());
    }

    @Test
    @DisplayName("execute_confirmFlagWithMultipleArgs_deletesProject")
    void executeConfirmWithExtraArgs() throws Exception {
        DeleteProjectCommand cmd = new DeleteProjectCommand("1 extra --confirm words");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "Should work with extra arguments after confirm");
        assertEquals(1, projects.getProjectListSize());
    }
}

