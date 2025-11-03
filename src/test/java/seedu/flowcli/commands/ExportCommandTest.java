package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.commands.core.ExportCommandHandler;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

//@@author sean6369
@DisplayName("ExportCommand Unit Tests")
class ExportCommandTest {

    static class SpyExportHandler extends ExportCommandHandler {
        String lastArguments = null;
        boolean handleExportCalled = false;

        SpyExportHandler(ProjectList projects, ConsoleUi ui) {
            super(projects, ui);
        }

        @Override
        public void handleExport(String arguments) {
            handleExportCalled = true;
            lastArguments = arguments;
        }
    }

    private ProjectList projects;
    private ConsoleUi ui;
    private SpyExportHandler exportHandler;
    private CommandContext ctx;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        ui = new ConsoleUi(projects);
        exportHandler = new SpyExportHandler(projects, ui);
        ctx = new CommandContext(projects, ui, exportHandler, null);
    }

    @Test
    @DisplayName("execute_delegatesToExportHandler")
    void executeDelegates() throws Exception {
        ExportCommand cmd = new ExportCommand("output.txt");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertTrue(exportHandler.handleExportCalled, "Should delegate to export handler");
        assertEquals("output.txt", exportHandler.lastArguments, "Should pass arguments to handler");
    }

    @Test
    @DisplayName("execute_withEmptyArguments_stillDelegates")
    void executeWithEmptyArguments() throws Exception {
        ExportCommand cmd = new ExportCommand("");

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertTrue(exportHandler.handleExportCalled, "Should delegate to export handler");
        assertEquals("", exportHandler.lastArguments);
    }

    @Test
    @DisplayName("execute_withComplexArguments_passesAllArguments")
    void executeWithComplexArguments() throws Exception {
        String args = "--filename output.txt --sort deadline ascending";
        ExportCommand cmd = new ExportCommand(args);

        boolean result = cmd.execute(ctx);

        assertTrue(result, "execute() should return true");
        assertEquals(args, exportHandler.lastArguments, "Should pass all arguments");
    }

    @Test
    @DisplayName("execute_alwaysReturnsTrue")
    void executeAlwaysReturnsTrue() throws Exception {
        ExportCommand cmd1 = new ExportCommand("file1.txt");
        ExportCommand cmd2 = new ExportCommand("");
        ExportCommand cmd3 = new ExportCommand("--filename file.txt");

        boolean result1 = cmd1.execute(ctx);
        boolean result2 = cmd2.execute(ctx);
        boolean result3 = cmd3.execute(ctx);

        assertTrue(result1 && result2 && result3, "Export command should always return true");
    }
}
//@@author

