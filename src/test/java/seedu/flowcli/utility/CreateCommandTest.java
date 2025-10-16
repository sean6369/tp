package seedu.flowcli.utility;

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

import seedu.flowcli.commands.CreateCommand;
import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectAlreadyExistsException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;


@DisplayName("CreateCommand Unit Tests")
class CreateCommandTest {

    private static final Logger logger = Logger.getLogger(CreateCommandTest.class.getName());

    
    static class SpyUi extends ConsoleUi {
        boolean addedShown = false;
        SpyUi(ProjectList projects) {
            super(projects); 
        }
        @Override
        public void showAddedProject() {
            addedShown = true;
        }
    }

    @BeforeEach
    void setUpLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.FINE);
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.FINE);
        }
        logger.info("Setting up CreateCommand tests");
    }

    /** Build a minimal CommandContext; CreateCommand only uses getProjects()/getUi(). */
    private CommandContext makeContext(ProjectList projects, ConsoleUi ui) {
        // Your constructor is CommandContext(ProjectList, ConsoleUi, ExportCommandHandler)
        // CreateCommand doesn't use the export handler, so pass null.
        return new CommandContext(projects, ui, null);
    }

    @Test
    @DisplayName("execute successful and addsProject returnsTrue and then Calls Ui")
    void execute_success_addsProject_andCallsUi() throws Exception {
        // Arrange
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        CreateCommand cmd = new CreateCommand("MyNewProject"); // parser expects the name as the argument

        
        boolean ok = cmd.execute(ctx);
        assertAll("Success path",
                () -> assertTrue(ok, "execute() should return true"),
                () -> assertTrue(ui.addedShown, "UI.showAddedProject() should be called"),
                () -> assertEquals(1, projects.getProjectList().size(),
                        "Project list should contain exactly one project after creation")
        );
        logger.info("Success case passed");
    }

    @Test
    @DisplayName("execute_existingProject_throwsProjectAlreadyExistsException")
    void execute_existingProject_throwsAlreadyExists() {
        // Arrange
        ProjectList projects = new ProjectList();
        projects.getProjectList().add(new Project("Existing")); // pre-existing
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        CreateCommand cmd = new CreateCommand("Existing");

        // Act & Assert
        assertThrows(ProjectAlreadyExistsException.class, () -> cmd.execute(ctx),
                "Should throw when project name already exists");
        assertFalse(ui.addedShown, "UI.showAddedProject() must not be called on failure");
        // Ensure we didn't add duplicates
        assertEquals(1, projects.getProjectList().size(), "No new project should be added on failure");
        logger.info("Existing-project case passed");
    }

    @Test
    @DisplayName("execute_missingName_throwsMissingArgumentException")
    void execute_missingName_throwsMissingArgument() {
        // Arrange
        ProjectList projects = new ProjectList();
        SpyUi ui = new SpyUi(projects);
        CommandContext ctx = makeContext(projects, ui);
        CreateCommand cmd = new CreateCommand(""); // empty => parser should treat as missing

        // Act & Assert
        assertThrows(MissingArgumentException.class, () -> cmd.execute(ctx),
                "Should throw when project name is missing");
        assertFalse(ui.addedShown, "UI.showAddedProject() must not be called on failure");
        assertEquals(0, projects.getProjectList().size(), "No project should be created on failure");
        logger.info("Missing-argument case passed");
    }
}
