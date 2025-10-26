package seedu.flowcli.unused;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.AddCommand;
import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.InvalidArgumentException;
import seedu.flowcli.exceptions.MissingDescriptionException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.ui.ConsoleUi;

/**
 * Legacy add command test.
 *
 * @author xylonc
 */
// @@author xylonc
@Disabled("Retired test")
public final class AddCommandTest {

    private static final Logger logger = Logger.getLogger(AddCommandTest.class.getName());

    static class SpyUi extends ConsoleUi {
        boolean addedProjectShown = false;
        boolean addedTaskShown = false;
        Project lastProject;
        Task lastTask;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showAddedProject() {
            addedProjectShown = true;
        }

        @Override
        public void showAddedTask(Project targetProject) {
            addedTaskShown = true;
            lastProject = targetProject;
            if (!targetProject.getProjectTasks().isEmpty()) {
                var tasks = targetProject.getProjectTasks().getTasks();
                lastTask = tasks.get(tasks.size() - 1);
            }
        }
    }

    @BeforeEach
    void setUpLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.FINE);
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.FINE);
        }
        logger.info("Setting up AddCommand tests");
    }

    private CommandContext makeContext(ProjectList projects, ConsoleUi ui) {
        return new CommandContext(projects, ui, null);
    }

    @Test @DisplayName("execute_existingProject_addsTaskAndCallsUi")
    void execute_existingProject_addsTaskAndCallsUi() throws Exception {
        // Arrange
        ProjectList projects = new ProjectList();
        projects.addProject("Alpha");
        Project alpha = projects.getProject("Alpha");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("Alpha Finish docs --priority high --deadline 2024-12-25");

        boolean ok = cmd.execute(ctx);

        // Assert
        assertAll("Success path (existing project)", () -> assertTrue(ok, "execute() should return true"),
                () -> assertTrue(ui.addedTaskShown, "UI.showAddedTask() should be called"),
                () -> assertFalse(ui.addedProjectShown, "UI.showAddedProject() should not be called"),
                () -> assertEquals(1, alpha.size(), "Project should now have one task"),
                () -> assertEquals("Finish docs", alpha.getProjectTasks().get(0).getDescription()),
                () -> assertEquals(LocalDate.of(2024, 12, 25), alpha.getProjectTasks().get(0).getDeadline()),
                () -> assertEquals(3, alpha.getProjectTasks().get(0).getPriority()));
        logger.info("Existing-project success case passed");
    }

    @Test @DisplayName("execute_existingProject_missingDescription_throwsMissingDescriptionException")
    void execute_missingDescription_throws() {
        // Arrange
        ProjectList projects = new ProjectList();
        projects.addProject("Alpha");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("Alpha");

        // Act & Assert
        assertThrows(MissingDescriptionException.class, () -> cmd.execute(ctx),
                "Should throw when description is missing for existing project");
        assertFalse(ui.addedTaskShown, "UI.showAddedTask() must not be called on failure");
        logger.info("Missing-description case passed");
    }

    @Test @DisplayName("execute_existingProject_invalidPriority_throwsInvalidArgumentException")
    void execute_invalidPriority_throws() {
        // Arrange
        ProjectList projects = new ProjectList();
        projects.addProject("Alpha");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("Alpha Do it --priority urgent");

        // Act & Assert
        assertThrows(InvalidArgumentException.class, () -> cmd.execute(ctx), "Should throw for invalid priority value");
        assertFalse(ui.addedTaskShown, "UI.showAddedTask() must not be called on failure");
        logger.info("Invalid-priority case passed");
    }

    @Test @DisplayName("execute_existingProject_invalidDeadline_throwsInvalidArgumentException")
    void execute_invalidDeadline_throws() {
        // Arrange
        ProjectList projects = new ProjectList();
        projects.addProject("Alpha");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("Alpha Do it --deadline 2024/12/25");

        // Act & Assert
        assertThrows(InvalidArgumentException.class, () -> cmd.execute(ctx),
                "Should throw for invalid deadline format");
        assertFalse(ui.addedTaskShown, "UI.showAddedTask() must not be called on failure");
        logger.info("Invalid-deadline case passed");
    }

    @Test @DisplayName("execute_nonExistingProject_createsProject_addsTask_andCallsUi")
    void execute_nonExistingProject_createsProject() throws Exception {
        // Arrange
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("\"Beta\" \"Write tests\" --priority low --deadline 2025-01-01");

        // Act
        boolean ok = cmd.execute(ctx);

        // Assert
        Project beta = projects.getProject("Beta");
        assertAll("Success path (non-existing project)", () -> assertTrue(ok, "execute() should return true"),
                () -> assertTrue(ui.addedProjectShown,
                        "UI.showAddedProject() should be called when project is created"),
                () -> assertTrue(ui.addedTaskShown, "UI.showAddedTask() should be called"),
                () -> assertEquals("Beta", beta.getProjectName()),
                () -> assertEquals(1, beta.size(), "New project should contain the added task"),
                () -> assertEquals("Write tests", beta.getProjectTasks().get(0).getDescription()),
                () -> assertEquals(LocalDate.of(2025, 1, 1), beta.getProjectTasks().get(0).getDeadline()),
                () -> assertEquals(2, beta.getProjectTasks().get(0).getPriority()));
        logger.info("Non-existing-project success case passed");
    }

    @Test @DisplayName("execute_nonExistingProject_missingProjectName_throwsInvalidArgumentException")
    void execute_missingProjectName_throws() {
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        AddCommand cmd = new AddCommand("\"\" \"Some task\"");

        // Act & Assert
        assertThrows(InvalidArgumentException.class, () -> cmd.execute(ctx),
                "Should throw when no project name is specified");
        assertFalse(ui.addedProjectShown, "UI.showAddedProject() must not be called on failure");
        assertFalse(ui.addedTaskShown, "UI.showAddedTask() must not be called on failure");
        logger.info("Missing-project-name case passed");
    }
}
