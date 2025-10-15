package seedu.flowcli.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import seedu.flowcli.commands.CreateCommand;
import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.MissingArgumentException;
import seedu.flowcli.exceptions.ProjectAlreadyExistsException;
import seedu.flowcli.parsers.ArgumentParser;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

@ExtendWith(MockitoExtension.class)
public class CreateCommandTest {

    /** Minimal JUL handler to capture log records. */
    private static class CapturingHandler extends Handler {
        final List<LogRecord> records = new ArrayList<>();
        @Override public void publish(LogRecord record) { records.add(record); }
        @Override public void flush() {}
        @Override public void close() throws SecurityException {}
        boolean hasLevel(Level level) {
            return records.stream().anyMatch(r -> r.getLevel().intValue() == level.intValue());
        }
    }

    @Test
    void execute_success_addsProject_showsUi_andLogsInfo() throws Exception {
        // Arrange
        CreateCommand cmd = new CreateCommand("create MyNewProject");
        CommandContext context = mock(CommandContext.class);
        ProjectList projectList = mock(ProjectList.class);
        ConsoleUi ui = mock(ConsoleUi.class);
        when(context.getProjects()).thenReturn(projectList);
        when(context.getUi()).thenReturn(ui);

        Logger logger = Logger.getLogger(CreateCommand.class.getName());
        Level original = logger.getLevel();
        CapturingHandler cap = new CapturingHandler();
        logger.addHandler(cap);
        logger.setLevel(Level.FINE);

        try (MockedConstruction<ArgumentParser> mocked =
                     mockConstruction(ArgumentParser.class, (parser, ctorCtx) -> {
                         when(parser.getTargetProject()).thenReturn(null);
                         when(parser.getRemainingArgument()).thenReturn("MyNewProject");
                     })) {

            // Act
            boolean ok = cmd.execute(context);

            // Assert behavior
            assertTrue(ok);
            verify(projectList).addProject("MyNewProject");
            verify(ui).showAddedProject();

            // Assert at least one INFO log occurred
            assertTrue(cap.hasLevel(Level.INFO), "Expected at least one INFO log on success");
        } finally {
            logger.removeHandler(cap);
            logger.setLevel(original);
        }
    }

    @Test
    void execute_projectAlreadyExists_logsWarning_andThrows() throws Exception {
        // Arrange
        CreateCommand cmd = new CreateCommand("create Existing");
        CommandContext context = mock(CommandContext.class);
        when(context.getProjects()).thenReturn(mock(ProjectList.class));
        Project existing = mock(Project.class);

        Logger logger = Logger.getLogger(CreateCommand.class.getName());
        Level original = logger.getLevel();
        CapturingHandler cap = new CapturingHandler();
        logger.addHandler(cap);
        logger.setLevel(Level.FINE);

        try (MockedConstruction<ArgumentParser> mocked =
                     mockConstruction(ArgumentParser.class, (parser, ctorCtx) -> {
                         when(parser.getTargetProject()).thenReturn(existing); // early throw
                     })) {

            // Act & Assert
            assertThrows(ProjectAlreadyExistsException.class, () -> cmd.execute(context));
            assertTrue(cap.hasLevel(Level.WARNING), "Expected a WARNING log when project exists");
        } finally {
            logger.removeHandler(cap);
            logger.setLevel(original);
        }
    }

    @Test
    void execute_missingArgument_logsWarning_andThrows() throws Exception {
        // Arrange
        CreateCommand cmd = new CreateCommand("create");
        CommandContext context = mock(CommandContext.class);
        when(context.getProjects()).thenReturn(mock(ProjectList.class));

        Logger logger = Logger.getLogger(CreateCommand.class.getName());
        Level original = logger.getLevel();
        CapturingHandler cap = new CapturingHandler();
        logger.addHandler(cap);
        logger.setLevel(Level.FINE);

        try (MockedConstruction<ArgumentParser> mocked =
                     mockConstruction(ArgumentParser.class, (parser, ctorCtx) -> {
                         when(parser.getTargetProject()).thenReturn(null);
                         when(parser.getRemainingArgument()).thenReturn(null); // triggers MissingArgumentException
                     })) {

            // Act & Assert
            assertThrows(MissingArgumentException.class, () -> cmd.execute(context));
            assertTrue(cap.hasLevel(Level.WARNING), "Expected a WARNING log for missing argument");
        } finally {
            logger.removeHandler(cap);
            logger.setLevel(original);
        }
    }
}
