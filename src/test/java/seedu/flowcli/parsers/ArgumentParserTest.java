package seedu.flowcli.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

@DisplayName("ArgumentParser Unit Tests")
class ArgumentParserTest {

    private static final Logger logger = Logger.getLogger(ArgumentParserTest.class.getName());

    private ProjectList projects;
    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINE);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.FINE);
        }

        projects = new ProjectList();
        project1 = new Project("Project Alpha");
        project2 = new Project("Project Beta");
        projects.getProjectList().add(project1);
        projects.getProjectList().add(project2);

        logger.info("Initialised test projects: " + projects.getProjectListSize());
    }

    @Test
    @DisplayName("parseArgument_validIndex_setsProjectAndRemaining")
    void testParseValidIndex() {
        ArgumentParser parser = new ArgumentParser("1 build docs", projects);

        assertAll("Valid index parsing",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertEquals("build docs", parser.getRemainingArgument()),
                () -> assertEquals(0, parser.getTargetProjectIndex()));
    }

    @Test
    @DisplayName("parseArgument_onlyIndex_setsProjectWithNullRemainder")
    void testParseIndexOnly() {
        ArgumentParser parser = new ArgumentParser("2", projects);

        assertAll("Index-only parsing",
                () -> assertEquals(project2, parser.getTargetProject()),
                () -> assertNull(parser.getRemainingArgument()),
                () -> assertEquals(1, parser.getTargetProjectIndex()));
    }

    @Test
    @DisplayName("parseArgument_outOfRangeIndex_recordsIndexWithoutProject")
    void testParseOutOfRangeIndex() {
        ArgumentParser parser = new ArgumentParser("5 extra args", projects);

        assertAll("Out-of-range index handling",
                () -> assertNull(parser.getTargetProject()),
                () -> assertEquals("extra args", parser.getRemainingArgument()),
                () -> assertEquals(4, parser.getTargetProjectIndex()));
    }

    @Test
    @DisplayName("parseArgument_nonNumeric_preservesTokensForManualHandling")
    void testParseNonNumeric() {
        ArgumentParser parser = new ArgumentParser("ProjectAlpha build", projects);

        assertAll("Non-numeric handling",
                () -> assertNull(parser.getTargetProject()),
                () -> assertEquals("build", parser.getRemainingArgument()),
                () -> assertNull(parser.getTargetProjectIndex()),
                () -> assertEquals("ProjectAlpha", parser.getParsedProjectName()));
    }

    @Test
    @DisplayName("parseArgument_emptyInput_returnsNulls")
    void testParseEmpty() {
        ArgumentParser empty = new ArgumentParser("", projects);
        ArgumentParser whitespace = new ArgumentParser("   ", projects);

        assertAll("Empty input handling",
                () -> assertNull(empty.getTargetProject()),
                () -> assertNull(empty.getRemainingArgument()),
                () -> assertNull(whitespace.getTargetProject()),
                () -> assertNull(whitespace.getRemainingArgument()));
    }
}
