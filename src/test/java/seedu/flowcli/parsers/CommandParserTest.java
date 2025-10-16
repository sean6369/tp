package seedu.flowcli.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.MissingIndexException;
import seedu.flowcli.parsers.CommandParser.CommandType;
import seedu.flowcli.parsers.CommandParser.ParsedCommand;

@DisplayName("CommandParser Unit Tests")
class CommandParserTest {

    private static final Logger logger = Logger.getLogger(CommandParserTest.class.getName());

    private CommandParser parser;

    @BeforeEach
    void setUp() {
        // Configure logging to show FINE level for this test class
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINE);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.FINE);
        }

        logger.info("Setting up test fixtures for CommandParser tests");

        parser = new CommandParser();

        logger.info("Test setup completed");
    }

    @Test
    @DisplayName("parse_validCommands_returnsCorrectCommandTypes")
    void testParseValidCommands() {
        logger.fine("Testing parsing of valid command types");

        assertAll("Valid command parsing",
                () -> assertEquals(CommandType.LIST, parser.parse("list").getType()),
                () -> assertEquals(CommandType.MARK, parser.parse("mark Project1 1").getType()),
                () -> assertEquals(CommandType.ADD, parser.parse("add Project1 Task").getType()),
                () -> assertEquals(CommandType.DELETE, parser.parse("delete Project1").getType()),
                () -> assertEquals(CommandType.UPDATE, parser.parse("update Project1 1").getType()),
                () -> assertEquals(CommandType.SORT, parser.parse("sort tasks by deadline").getType()),
                () -> assertEquals(CommandType.FILTER, parser.parse("filter tasks by priority").getType()));

        logger.info("Valid command types parsing test passed");
    }

    @Test
    @DisplayName("parse_commandWithArguments_preservesArguments")
    void testParseCommandWithArguments() {
        logger.fine("Testing command argument preservation");

        ParsedCommand result = parser.parse("add Project1 New Task --priority high");

        assertAll("Arguments preservation",
                () -> assertEquals(CommandType.ADD, result.getType()),
                () -> assertEquals("Project1 New Task --priority high", result.getArguments()));

        logger.info("Command argument preservation test passed");
    }

    @Test
    @DisplayName("parse_commandCaseInsensitive_recognizesCorrectly")
    void testParseCaseInsensitive() {
        logger.fine("Testing case-insensitive command parsing");

        assertAll("Case insensitive parsing",
                () -> assertEquals(CommandType.LIST, parser.parse("LIST").getType()),
                () -> assertEquals(CommandType.MARK, parser.parse("MaRk arg").getType()),
                () -> assertEquals(CommandType.ADD, parser.parse("ADD arg").getType()));

        logger.info("Case-insensitive command parsing test passed");
    }

    @Test
    @DisplayName("parse_unknownCommand_returnsUnknownType")
    void testParseUnknownCommand() {
        logger.fine("Testing unknown command handling");

        ParsedCommand result = parser.parse("invalidcommand");

        assertEquals(CommandType.UNKNOWN, result.getType());

        logger.info("Unknown command handling test passed");
    }

    @Test
    @DisplayName("parse_emptyOrWhitespace_returnsUnknownType")
    void testParseEmptyInput() {
        logger.fine("Testing empty and whitespace input handling");

        assertAll("Empty input handling",
                () -> assertEquals(CommandType.UNKNOWN, parser.parse("").getType()),
                () -> assertEquals(CommandType.UNKNOWN, parser.parse("   ").getType()));

        logger.info("Empty input handling test passed");
    }

    @Test
    @DisplayName("parseIndexOrNull_validIndex_returnsZeroBasedIndex")
    void testParseIndexOrNullValid() throws Exception {
        logger.fine("Testing valid index parsing");

        assertAll("Valid index parsing",
                () -> assertEquals(0, CommandParser.parseIndexOrNull("1", 10)),
                () -> assertEquals(2, CommandParser.parseIndexOrNull("3", 10)),
                () -> assertEquals(9, CommandParser.parseIndexOrNull("10", 10)));

        logger.info("Valid index parsing test passed");
    }

    @Test
    @DisplayName("parseIndexOrNull_nullInput_throwsMissingIndexException")
    void testParseIndexOrNullWithNull() {
        logger.fine("Testing null index input");

        assertThrows(MissingIndexException.class,
                () -> CommandParser.parseIndexOrNull(null, 5));

        logger.info("Null index input test passed");
    }

    @Test
    @DisplayName("parseIndexOrNull_outOfRange_throwsIndexOutOfRangeException")
    void testParseIndexOrNullOutOfRange() {
        logger.fine("Testing out-of-range index handling");

        assertAll("Out of range indices",
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("0", 5)),
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("11", 10)),
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("-1", 5)));

        logger.info("Out-of-range index handling test passed");
    }

    @Test
    @DisplayName("parseIndexOrNull_invalidFormat_throwsNumberFormatException")
    void testParseIndexOrNullInvalidFormat() {
        logger.fine("Testing invalid index format");

        assertThrows(NumberFormatException.class,
                () -> CommandParser.parseIndexOrNull("abc", 5));

        logger.info("Invalid index format test passed");
    }
}
