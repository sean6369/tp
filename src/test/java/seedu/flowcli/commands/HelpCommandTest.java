package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

@DisplayName("HelpCommand Unit Tests")
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
    @DisplayName("execute_showsHelp")
    void executeShowsHelp() {
        HelpCommand cmd = new HelpCommand("");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertTrue(ui.helpShown, "UI should show help message");
    }

    @Test
    @DisplayName("execute_withArguments_stillShowsHelp")
    void executeWithArgumentsShowsHelp() {
        HelpCommand cmd = new HelpCommand("some random arguments");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true regardless of arguments");
        assertTrue(ui.helpShown, "UI should show help message");
    }

    @Test
    @DisplayName("execute_alwaysReturnsTrue")
    void executeAlwaysReturnsTrue() {
        HelpCommand cmd1 = new HelpCommand("");
        HelpCommand cmd2 = new HelpCommand("arg1 arg2");
        HelpCommand cmd3 = new HelpCommand("--flag");

        boolean result1 = cmd1.execute(ctx);
        boolean result2 = cmd2.execute(ctx);
        boolean result3 = cmd3.execute(ctx);

        assertTrue(result1 && result2 && result3, "Help command should always return true");
    }

    @Test
    @DisplayName("execute_withEmptyProjectList_stillShowsHelp")
    void executeWithEmptyProjectList() {
        assertEquals(0, projects.getProjectListSize(), "Project list should be empty");
        
        HelpCommand cmd = new HelpCommand("");
        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should work with empty project list");
        assertTrue(ui.helpShown, "UI should show help message");
    }
}

