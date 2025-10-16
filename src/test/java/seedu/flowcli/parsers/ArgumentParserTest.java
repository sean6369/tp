package seedu.flowcli.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

@DisplayName("ArgumentParser Unit Tests")
class ArgumentParserTest {

    private ProjectList projects;
    private Project project1;
    private Project multiWordProject;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();

        project1 = new Project("Project1");
        multiWordProject = new Project("My Project");

        projects.getProjectList().add(project1);
        projects.getProjectList().add(multiWordProject);
    }

    @Test
    @DisplayName("parseArgument_simpleProjectName_findsProjectAndRemainingArgs")
    void testParseSimpleProjectName() {
        ArgumentParser parser = new ArgumentParser("Project1 task description", projects);

        assertAll("Simple project name parsing",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertEquals("task description", parser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_projectNameOnly_findsProjectWithNullRemaining")
    void testParseProjectNameOnly() {
        ArgumentParser parser = new ArgumentParser("Project1", projects);

        assertAll("Project name without arguments",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertNull(parser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_quotedProjectName_handlesMultiWordProject")
    void testParseQuotedProjectName() {
        ArgumentParser parser = new ArgumentParser("\"My Project\" task1", projects);

        assertAll("Quoted project name",
                () -> assertEquals(multiWordProject, parser.getTargetProject()),
                () -> assertEquals("task1", parser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_escapedCharacters_handlesCorrectly")
    void testParseEscapedCharacters() {
        Project escapedProject = new Project("Project \"Special\"");
        projects.getProjectList().add(escapedProject);

        ArgumentParser parser = new ArgumentParser("\"Project \\\"Special\\\"\" task", projects);

        assertAll("Escaped quotes",
                () -> assertEquals(escapedProject, parser.getTargetProject()),
                () -> assertEquals("task", parser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_nonExistentProject_returnsNullProjectWithFullString")
    void testParseNonExistentProject() {
        ArgumentParser parser = new ArgumentParser("NonExistent task description", projects);

        assertAll("Non-existent project",
                () -> assertNull(parser.getTargetProject()),
                () -> assertEquals("NonExistent task description", parser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_caseInsensitive_findsProjectRegardlessOfCase")
    void testParseCaseInsensitive() {
        assertAll("Case insensitive matching",
                () -> assertEquals(project1,
                        new ArgumentParser("PROJECT1 task", projects).getTargetProject()),
                () -> assertEquals(project1,
                        new ArgumentParser("project1 task", projects).getTargetProject()),
                () -> assertEquals(multiWordProject,
                        new ArgumentParser("\"my project\" task", projects).getTargetProject()));
    }

    @Test
    @DisplayName("parseArgument_emptyOrWhitespace_returnsNulls")
    void testParseEmptyInput() {
        ArgumentParser emptyParser = new ArgumentParser("", projects);
        ArgumentParser whitespaceParser = new ArgumentParser("   ", projects);

        assertAll("Empty input handling",
                () -> assertNull(emptyParser.getTargetProject()),
                () -> assertNull(emptyParser.getRemainingArgument()),
                () -> assertNull(whitespaceParser.getTargetProject()),
                () -> assertNull(whitespaceParser.getRemainingArgument()));
    }

    @Test
    @DisplayName("parseArgument_specialCharactersInArgs_preservesCorrectly")
    void testParseSpecialCharacters() {
        ArgumentParser parser = new ArgumentParser("Project1 task --priority high --deadline 2025-12-31", projects);

        assertAll("Special characters preservation",
                () -> assertEquals(project1, parser.getTargetProject()),
                () -> assertEquals("task --priority high --deadline 2025-12-31",
                        parser.getRemainingArgument()));
    }
}
