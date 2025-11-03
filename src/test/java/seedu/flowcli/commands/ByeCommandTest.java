package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

@DisplayName("ByeCommand Unit Tests")
class ByeCommandTest {

    private static final Logger logger = Logger.getLogger(ByeCommandTest.class.getName());

    static class SpyUi extends ConsoleUi {
        boolean byeShown = false;
        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void printByeMessage() {
            byeShown = true;
            // Do not call super to avoid printing in tests
        }
    }

    @BeforeEach
    void setUpLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.FINE);
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.FINE);
        }
        logger.info("Setting up ByeCommand tests");
    }

    /** Build a minimal CommandContext; ByeCommand only uses getUi(). */
    private CommandContext makeContext(ProjectList projects, ConsoleUi ui) {
        return new CommandContext(projects, ui, null);
    }

    @Test
    @DisplayName("execute_returnsFalseAndCallsUi")
    void executeReturnsFalseAndCallsUi() throws Exception {
        // Arrange
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        ByeCommand cmd = new ByeCommand("");

        // Act
        boolean shouldContinue = cmd.execute(ctx);

        // Assert
        assertAll("ByeCommand success path",
                () -> assertFalse(shouldContinue, "Bye should return false to stop main loop"),
                () -> assertTrue(ui.byeShown, "UI.printByeMessage() should be called")
        );
        logger.info("Basic bye flow passed");
    }

    @Test
    @DisplayName("execute_withExtraArgs_throwsException")
    void executeWithExtraArgsThrowsException() {
        // Arrange
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        ByeCommand cmd = new ByeCommand("   extra stuff   ");

        // Act & Assert
        assertThrows(MissingArgumentException.class, () -> cmd.execute(ctx),
                "Bye command should throw exception when extra arguments are provided");
        logger.info("Bye with extra arguments correctly throws exception");
    }

    @Test
    @DisplayName("execute_doesNotMutateProjects")
    void executeDoesNotMutateProjects() throws Exception {
        // Arrange: pre-populate projects and ensure size is unchanged after bye
        ProjectList projects = new ProjectList();
        projects.getProjectList().add(new Project("Alpha"));
        projects.getProjectList().add(new Project("Beta"));
        int before = projects.getProjectList().size();

        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        ByeCommand cmd = new ByeCommand("");

        // Act
        boolean shouldContinue = cmd.execute(ctx);

        // Assert
        assertAll("Projects remain unchanged",
                () -> assertFalse(shouldContinue, "Bye should return false"),
                () -> assertEquals(before, projects.getProjectList().size(),
                        "Project list size should not change after bye"),
                () -> assertTrue(ui.byeShown, "UI.printByeMessage() should be called")
        );
        logger.info("Projects immutability under Bye passed");
    }
}

