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
    private Project multiWordProject;

    @BeforeEach
    void setUp() {
        // Configure logging to show FINE level for this test class
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINE);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.FINE);
        }

        logger.info("Setting up test fixtures for ArgumentParser tests");

        projects = new ProjectList();

        project1 = new Project("Project1");
        multiWordProject = new Project("My Project");

        projects.getProjectList().add(project1);
        projects.getProjectList().add(multiWordProject);

        logger.info("Test setup completed with " + projects.getProjectList().size() + " projects");
    }

    @Test
    @DisplayName("parseArgument_simpleProjectName_findsProjectAndRemainingArgs")
    void testParseSimpleProjectName() {
        logger.fine("Testing simple project name parsing");

        ArgumentParser parser = new ArgumentParser("Project1 task description", projects);

        assertAll("Simple project name parsing",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertEquals("task description", parser.getRemainingArgument()));

        logger.info("Simple project name parsing test passed");
    }

    @Test
    @DisplayName("parseArgument_projectNameOnly_findsProjectWithNullRemaining")
    void testParseProjectNameOnly() {
        logger.fine("Testing project name without arguments");

        ArgumentParser parser = new ArgumentParser("Project1", projects);

        assertAll("Project name without arguments",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertNull(parser.getRemainingArgument()));

        logger.info("Project name without arguments test passed");
    }

    @Test
    @DisplayName("parseArgument_quotedProjectName_handlesMultiWordProject")
    void testParseQuotedProjectName() {
        logger.fine("Testing quoted project name parsing");

        ArgumentParser parser = new ArgumentParser("\"My Project\" task1", projects);

        assertAll("Quoted project name",
                () -> assertEquals(multiWordProject, parser.getTargetProject()),
                () -> assertEquals("task1", parser.getRemainingArgument()));

        logger.info("Quoted project name parsing test passed");
    }

    @Test
    @DisplayName("parseArgument_escapedCharacters_handlesCorrectly")
    void testParseEscapedCharacters() {
        logger.fine("Testing escaped character handling");

        Project escapedProject = new Project("Project \"Special\"");
        projects.getProjectList().add(escapedProject);

        ArgumentParser parser = new ArgumentParser("\"Project \\\"Special\\\"\" task", projects);

        assertAll("Escaped quotes",
                () -> assertEquals(escapedProject, parser.getTargetProject()),
                () -> assertEquals("task", parser.getRemainingArgument()));

        logger.info("Escaped character handling test passed");
    }

    @Test
    @DisplayName("parseArgument_nonExistentProject_returnsNullProjectWithFullString")
    void testParseNonExistentProject() {
        logger.fine("Testing non-existent project handling");

        ArgumentParser parser = new ArgumentParser("NonExistent task description", projects);

        assertAll("Non-existent project",
                () -> assertNull(parser.getTargetProject()),
                () -> assertEquals("NonExistent task description", parser.getRemainingArgument()));

        logger.info("Non-existent project handling test passed");
    }

    @Test
    @DisplayName("parseArgument_caseInsensitive_findsProjectRegardlessOfCase")
    void testParseCaseInsensitive() {
        logger.fine("Testing case-insensitive project matching");

        assertAll("Case insensitive matching",
                () -> assertEquals(project1,
                        new ArgumentParser("PROJECT1 task", projects).getTargetProject()),
                () -> assertEquals(project1,
                        new ArgumentParser("project1 task", projects).getTargetProject()),
                () -> assertEquals(multiWordProject,
                        new ArgumentParser("\"my project\" task", projects).getTargetProject()));

        logger.info("Case-insensitive project matching test passed");
    }

    @Test
    @DisplayName("parseArgument_emptyOrWhitespace_returnsNulls")
    void testParseEmptyInput() {
        logger.fine("Testing empty and whitespace input handling");

        ArgumentParser emptyParser = new ArgumentParser("", projects);
        ArgumentParser whitespaceParser = new ArgumentParser("   ", projects);

        assertAll("Empty input handling",
                () -> assertNull(emptyParser.getTargetProject()),
                () -> assertNull(emptyParser.getRemainingArgument()),
                () -> assertNull(whitespaceParser.getTargetProject()),
                () -> assertNull(whitespaceParser.getRemainingArgument()));

        logger.info("Empty input handling test passed");
    }

    @Test
    @DisplayName("parseArgument_specialCharactersInArgs_preservesCorrectly")
    void testParseSpecialCharacters() {
        logger.fine("Testing special character preservation in arguments");

        ArgumentParser parser = new ArgumentParser("Project1 task --priority high --deadline 2025-12-31", projects);

        assertAll("Special characters preservation",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertEquals("task --priority high --deadline 2025-12-31",
                        parser.getRemainingArgument()));

        logger.info("Special character preservation test passed");
    }
}
