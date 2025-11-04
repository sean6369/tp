package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.EmptyProjectListException;
import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.InvalidIndexFormatException;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

@DisplayName("StatusCommand Unit Tests")
class StatusCommandTest {

    private static final Logger logger = Logger.getLogger(StatusCommandTest.class.getName());

    static class SpyUi extends ConsoleUi {
        String lastAction = null;
        Project lastProject = null;
        ProjectList lastProjectList = null;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showProjectStatus(Project project) {
            lastAction = "showProjectStatus";
            lastProject = project;
        }

        @Override
        public void showAllProjectsStatus(ProjectList projectList) {
            lastAction = "showAllProjectsStatus";
            lastProjectList = projectList;
        }
    }

    private CommandContext makeContext(ProjectList projects, ConsoleUi ui) {
        return new CommandContext(projects, ui, null, null);
    }

    @BeforeEach
    void setUpLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.FINE);
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.FINE);
        }
        logger.info("Setting up StatusCommand tests");
    }

    @Test
    @DisplayName("execute_withAllFlag_showsAllProjectsStatus")
    void executeWithAllFlag() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Project1");
        projects.addProject("Project2");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("--all");

        boolean result = cmd.execute(ctx);

        assertAll("All flag success path",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showAllProjectsStatus", ui.lastAction),
                () -> assertEquals(projects, ui.lastProjectList)
        );
    }

    @Test
    @DisplayName("execute_withValidProjectIndex_showsProjectStatus")
    void executeWithValidIndex() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("TestProject");
        Project project = projects.getProjectByIndex(0);
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("1");

        boolean result = cmd.execute(ctx);

        assertAll("Valid index success path",
                () -> assertTrue(result, "execute() should return true"),
                () -> assertEquals("showProjectStatus", ui.lastAction),
                () -> assertEquals(project, ui.lastProject)
        );
    }

    @Test
    @DisplayName("execute_emptyArguments_throwsMissingArgumentException")
    void executeEmptyArguments() {
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("");

        assertThrows(MissingArgumentException.class, () -> cmd.execute(ctx),
                "Should throw when arguments are empty");
    }

    @Test
    @DisplayName("execute_emptyProjectList_throwsEmptyProjectListException")
    void executeEmptyProjectList() {
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("1");

        assertThrows(EmptyProjectListException.class, () -> cmd.execute(ctx),
                "Should throw when project list is empty");
    }

    @Test
    @DisplayName("execute_invalidIndex_throwsIndexOutOfRangeException")
    void executeInvalidIndex() {
        ProjectList projects = new ProjectList();
        projects.addProject("Project1");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("5");

        assertThrows(IndexOutOfRangeException.class, () -> cmd.execute(ctx),
                "Should throw when project index is out of range");
    }

    @Test
    @DisplayName("execute_nonNumericInput_throwsInvalidIndexFormatException")
    void executeNonNumericInput() {
        ProjectList projects = new ProjectList();
        projects.addProject("Project1");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmd = new StatusCommand("abc");

        assertThrows(InvalidIndexFormatException.class, () -> cmd.execute(ctx),
                "Should throw when input is non-numeric");
    }

    @Test
    @DisplayName("execute_allFlagCaseInsensitive_showsAllProjectsStatus")
    void executeAllFlagCaseInsensitive() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Project1");
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        StatusCommand cmdUpper = new StatusCommand("--ALL");
        StatusCommand cmdMixed = new StatusCommand("--AlL");

        boolean resultUpper = cmdUpper.execute(ctx);
        boolean resultMixed = cmdMixed.execute(ctx);

        assertTrue(resultUpper && resultMixed, "All flags should be case insensitive");
    }
}

