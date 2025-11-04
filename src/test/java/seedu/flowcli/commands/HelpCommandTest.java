package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.ExtraArgumentException;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

//@@author zeeeing
class HelpCommandTest {

    static class SpyUi extends ConsoleUi {
        boolean helpShown = false;

        SpyUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showHelp() {
            helpShown = true;
        }
    }

    private ProjectList projects;
    private SpyUi ui;
    private CommandContext ctx;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        ui = new SpyUi(projects);
        ctx = new CommandContext(projects, ui, null);
    }

    @Test
    void executeShowsHelp() throws Exception {
        HelpCommand cmd = new HelpCommand("");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertTrue(ui.helpShown, "UI should show help message");
    }

    @Test
    void executeWithArgumentsThrowsException() {
        HelpCommand cmd = new HelpCommand("some random arguments");

        ExtraArgumentException ex = assertThrows(ExtraArgumentException.class, () -> cmd.execute(ctx),
                "Should throw exception when arguments are provided");
        assertEquals("The 'help' command does not accept any parameters.", ex.getMessage(),
                "Exception message should explain that no parameters are accepted");
        assertFalse(ui.helpShown, "Help should not be displayed when extra arguments are provided");
    }

    @Test
    void executeWithValidArgumentsReturnsTrue() throws Exception {
        HelpCommand cmd1 = new HelpCommand("");
        HelpCommand cmd2 = new HelpCommand(null);
        HelpCommand cmd3 = new HelpCommand("   ");

        boolean result1 = cmd1.execute(ctx);
        boolean result2 = cmd2.execute(ctx);
        boolean result3 = cmd3.execute(ctx);

        assertTrue(result1 && result2 && result3, "Help command should return true with empty/null arguments");
    }

    @Test
    void executeWithEmptyProjectList() throws Exception {
        assertEquals(0, projects.getProjectListSize(), "Project list should be empty");

        HelpCommand cmd = new HelpCommand("");
        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should work with empty project list");
        assertTrue(ui.helpShown, "UI should show help message");
    }
}
