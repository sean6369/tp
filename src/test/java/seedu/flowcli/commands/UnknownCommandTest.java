package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.UnknownInputException;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

//@@author sean6369
@DisplayName("UnknownCommand Unit Tests")
class UnknownCommandTest {

    private CommandContext ctx;

    @BeforeEach
    void setUp() {
        ProjectList projects = new ProjectList();
        ConsoleUi ui = new ConsoleUi(projects);
        ctx = new CommandContext(projects, ui, null, null);
    }

    @Test
    @DisplayName("execute_alwaysThrowsUnknownInputException")
    void executeAlwaysThrows() {
        UnknownCommand cmd = new UnknownCommand("");

        assertThrows(UnknownInputException.class, () -> cmd.execute(ctx),
                "UnknownCommand should always throw UnknownInputException");
    }

    @Test
    @DisplayName("execute_withArguments_stillThrowsUnknownInputException")
    void executeWithArgumentsThrows() {
        UnknownCommand cmd = new UnknownCommand("some invalid command");

        assertThrows(UnknownInputException.class, () -> cmd.execute(ctx),
                "UnknownCommand should throw regardless of arguments");
    }

    @Test
    @DisplayName("execute_withEmptyArguments_throwsUnknownInputException")
    void executeWithEmptyArgumentsThrows() {
        UnknownCommand cmd1 = new UnknownCommand("");
        UnknownCommand cmd2 = new UnknownCommand("   ");

        assertThrows(UnknownInputException.class, () -> cmd1.execute(ctx));
        assertThrows(UnknownInputException.class, () -> cmd2.execute(ctx));
    }

    @Test
    @DisplayName("execute_withVariousInputs_alwaysThrowsSameException")
    void executeWithVariousInputsThrowsSame() {
        UnknownCommand cmd1 = new UnknownCommand("asdf");
        UnknownCommand cmd2 = new UnknownCommand("123");
        UnknownCommand cmd3 = new UnknownCommand("--flag");
        UnknownCommand cmd4 = new UnknownCommand("multiple words here");

        assertThrows(UnknownInputException.class, () -> cmd1.execute(ctx));
        assertThrows(UnknownInputException.class, () -> cmd2.execute(ctx));
        assertThrows(UnknownInputException.class, () -> cmd3.execute(ctx));
        assertThrows(UnknownInputException.class, () -> cmd4.execute(ctx));
    }
}
//@@author

