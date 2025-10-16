package seedu.flowcli.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.MissingIndexException;
import seedu.flowcli.parsers.CommandParser.CommandType;
import seedu.flowcli.parsers.CommandParser.ParsedCommand;

@DisplayName("CommandParser Unit Tests")
class CommandParserTest {

    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    @DisplayName("parse_validCommands_returnsCorrectCommandTypes")
    void testParseValidCommands() {
        assertAll("Valid command parsing",
                () -> assertEquals(CommandType.LIST, parser.parse("list").getType()),
                () -> assertEquals(CommandType.MARK, parser.parse("mark Project1 1").getType()),
                () -> assertEquals(CommandType.ADD, parser.parse("add Project1 Task").getType()),
                () -> assertEquals(CommandType.DELETE, parser.parse("delete Project1").getType()),
                () -> assertEquals(CommandType.UPDATE, parser.parse("update Project1 1").getType()),
                () -> assertEquals(CommandType.SORT, parser.parse("sort tasks by deadline").getType()),
                () -> assertEquals(CommandType.FILTER, parser.parse("filter tasks by priority").getType()));
    }

    @Test
    @DisplayName("parse_commandWithArguments_preservesArguments")
    void testParseCommandWithArguments() {
        ParsedCommand result = parser.parse("add Project1 New Task --priority high");

        assertAll("Arguments preservation",
                () -> assertEquals(CommandType.ADD, result.getType()),
                () -> assertEquals("Project1 New Task --priority high", result.getArguments()));
    }

    @Test
    @DisplayName("parse_commandCaseInsensitive_recognizesCorrectly")
    void testParseCaseInsensitive() {
        assertAll("Case insensitive parsing",
                () -> assertEquals(CommandType.LIST, parser.parse("LIST").getType()),
                () -> assertEquals(CommandType.MARK, parser.parse("MaRk arg").getType()),
                () -> assertEquals(CommandType.ADD, parser.parse("ADD arg").getType()));
    }

    @Test
    @DisplayName("parse_unknownCommand_returnsUnknownType")
    void testParseUnknownCommand() {
        ParsedCommand result = parser.parse("invalidcommand");

        assertEquals(CommandType.UNKNOWN, result.getType());
    }

    @Test
    @DisplayName("parse_emptyOrWhitespace_returnsUnknownType")
    void testParseEmptyInput() {
        assertAll("Empty input handling",
                () -> assertEquals(CommandType.UNKNOWN, parser.parse("").getType()),
                () -> assertEquals(CommandType.UNKNOWN, parser.parse("   ").getType()));
    }

    @Test
    @DisplayName("parseIndexOrNull_validIndex_returnsZeroBasedIndex")
    void testParseIndexOrNullValid() throws Exception {
        assertAll("Valid index parsing",
                () -> assertEquals(0, CommandParser.parseIndexOrNull("1", 10)),
                () -> assertEquals(2, CommandParser.parseIndexOrNull("3", 10)),
                () -> assertEquals(9, CommandParser.parseIndexOrNull("10", 10)));
    }

    @Test
    @DisplayName("parseIndexOrNull_nullInput_throwsMissingIndexException")
    void testParseIndexOrNullWithNull() {
        assertThrows(MissingIndexException.class,
                () -> CommandParser.parseIndexOrNull(null, 5));
    }

    @Test
    @DisplayName("parseIndexOrNull_outOfRange_throwsIndexOutOfRangeException")
    void testParseIndexOrNullOutOfRange() {
        assertAll("Out of range indices",
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("0", 5)),
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("11", 10)),
                () -> assertThrows(IndexOutOfRangeException.class,
                        () -> CommandParser.parseIndexOrNull("-1", 5)));
    }

    @Test
    @DisplayName("parseIndexOrNull_invalidFormat_throwsNumberFormatException")
    void testParseIndexOrNullInvalidFormat() {
        assertThrows(NumberFormatException.class,
                () -> CommandParser.parseIndexOrNull("abc", 5));
    }
}